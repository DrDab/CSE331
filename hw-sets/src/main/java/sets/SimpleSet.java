package sets;

import java.util.List;

/**
 * Represents an immutable set of points on the real line that is easy to
 * describe, either because it is a finite set, e.g., {p1, p2, ..., pN}, or
 * because it excludes only a finite set, e.g., R \ {p1, p2, ..., pN}. As with
 * FiniteSet, each point is represented by a Java float with a non-infinite,
 * non-NaN value.
 */
public class SimpleSet {

  // Representation Invariant (RI): FiniteSet points != null and isComplement is true or false.
  //
  // Abstraction Function:
  //    AF(this) = FiniteSet containing all points included in this set iff isComplement is false
  //               FiniteSet containing all points excluded in this set iff isComplement is true
  private FiniteSet points;
  private boolean isComplement;

  /**
   * Creates a simple set containing only the given points.
   * @param vals Array containing the points to make into a SimpleSet
   * @spec.requires points != null and has no NaNs, no infinities, and no dups
   * @spec.effects this = {vals[0], vals[1], ..., vals[vals.length-1]}
   */
  public SimpleSet(float[] vals) {
    this.points = FiniteSet.of(vals);
    this.isComplement = false;
  }

  /**
   * Private constructor that directly fills in the fields above.
   * @param complement Whether this = points or this = R \ points
   * @param points List of points that are in the set or not in the set
   * @spec.requires points != null
   * @spec.effects this = R \ points if complement else points
   */
  private SimpleSet(boolean complement, FiniteSet points) {
    this.isComplement = complement;
    this.points = points;
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof SimpleSet))
      return false;

    SimpleSet other = (SimpleSet) o;

    if (this.isComplement != other.isComplement ||
            this.points.size() != other.points.size())
      return false;

    return this.points.equals(other.points);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  /**
   * Returns the number of points in this set.
   * @return N      if this = {p1, p2, ..., pN} and
   *         infty  if this = R \ {p1, p2, ..., pN}
   */
  public float size() {
    return isComplement ? Float.POSITIVE_INFINITY : points.size();
  }

  /**
   * Returns a string describing the points included in this set.
   * @return the string "R" if this contains every point,
   *     a string of the form "R \ {p1, p2, .., pN}" if this contains all
   *        but {@literal N > 0} points, or
   *     a string of the form "{p1, p2, .., pN}" if this contains
   *        {@literal N >= 0} points,
   *     where p1, p2, ... pN are replaced by the individual numbers.
   */
  public String toString() {
    StringBuilder sb = new StringBuilder();

    if (isComplement)
    {
      sb.append("R");

      if (points.size() == 0)
      {
        return sb.toString();
      }

      sb.append(" \\ ");
    }

    StringBuilder setStr = new StringBuilder();
    setStr.append("{");

    List<Float> pointList = points.getPoints();

    // note: - pl is shortland for pointList.
    //       - pl[i] refers to the string representation
    //         of the element positioned at the ith index of pointList.
    //       - n refers to pointList.size().
    //
    // Loop Invariant: (i != n && setStr = "{pl[0], pl[1], ..., pl[i-2], pl[i-1],") ||
    //                 (i == n && setStr = "{pl[0], pl[1], ..., pl[i-2], pl[i-1]")
    int i = 0;

    while (i != pointList.size())
    {
      // {{ Inv && i != n }}
      setStr.append(pointList.get(i));
      // {{ setStr = "{pl[0], pl[1], ..., pl[i-1], pl[i]" && i != n }}
      if (i != pointList.size() - 1)
      {
        setStr.append(", ");
      }
      // {{ (i != n - 1 && setStr = "{pl[0], pl[1], ..., pl[i-1], pl[i],")
      //    || (i == n - 1 && setStr = "{pl[0], pl[1], ..., pl[i-1], pl[i]") }}

      i += 1;

      // {{ (i != n && setStr = "{pl[0], pl[1], ..., pl[i-2], pl[i-1],")
      //    || (i == n && setStr = "{pl[0], pl[1], ..., pl[i-2], pl[i-1]") }}
    }

    // {{ setStr = "{pl[0], pl[1], ..., pl[n-1]" }}
    setStr.append("}");
    // {{ setStr = "{pl[0], pl[1], ..., pl[n-1]}" }}

    sb.append(setStr);

    return sb.toString();
  }

  /**
   * Returns a set representing the points R \ this.
   * @return R \ this
   */
  public SimpleSet complement() {
    // If this is a complement set, then copying the points and toggling the complement status gives the
    // complement of the complement set which is a non-complement set of points.
    //
    // If this is a non-complement set, copying the points and toggling the complement status gives the
    // complement of the non-complement set which is a complement set of points.

    return new SimpleSet(!isComplement, points);
  }

  /**
   * Returns the union of this and other.
   * @param other Set to union with this one.
   * @spec.requires other != null
   * @return this union other
   */
  public SimpleSet union(SimpleSet other) {
    // note: in explanations A refers to set this.points, B refers to the set other.points.
    // A^C refers to the complement of A and B^C refers to the complement of B.

    if (isComplement && other.isComplement)
    {
      // For the case that A and B are complement sets:
      // A^C union B^C = (A intersection B)^C by DeMorgan's Law.
      return new SimpleSet(true, points.intersection(other.points));
    }
    else if (!isComplement && !other.isComplement)
    {
      // For the case that A and B are non-complement sets:
      // Just union the internal set representation as normal.
      return new SimpleSet(false, points.union(other.points));
    }
    else
    {
      // If A is a complement set and B is not a complement set:
      //      A^C union B
      //    = (A intersection B^C)^C    [ DeMorgan's Law ]
      //    = (A \ B)^C                 [ Set Difference Law ]
      //      Therefore, A^C union B = (A \ B)^C,
      //      so taking the difference between A and B and setting
      //      new SimpleSet as a complement works.
      //
      // If A is not a complement set and B is a complement set:
      //      A union B^C
      //    = (A^C intersection B)^C    [ DeMorgan's Law ]
      //    = (B intersection A^C)^C    [ Commutativity ]
      //    = (B \ A)^C                 [ Set Difference Law ]
      //      Therefore, A union B^C = (B \ A)^C,
      //      so taking the difference between B and A and setting
      //      new SimpleSet as a complement works.
      return new SimpleSet(true,
              isComplement ? this.points.difference(other.points) :
                            other.points.difference(this.points));
    }
  }

  /**
   * Returns the intersection of this and other.
   * @param other Set to intersect with this one.
   * @spec.requires other != null
   * @return this intersect other
   */
  public SimpleSet intersection(SimpleSet other) {
    // note: in explanations A refers to set this.points, B refers to the set other.points.
    // A^C refers to the complement of A and B^C refers to the complement of B.

    if (isComplement && other.isComplement)
    {
      // For the case that A and B are complement sets:
      // A^C intersect B^C = (A union B)^C by DeMorgan's Law.
      return new SimpleSet(true, points.union(other.points));
    }
    else if (!isComplement && !other.isComplement)
    {
      // For the case that A and B are non-complement sets:
      // Just intersect the internal set representation as normal.
      return new SimpleSet(false, points.intersection(other.points));
    }
    else
    {
      // If A is a complement set and B is not a complement set:
      //    by Set Difference Law, A^C intersection B = B intersection A^C = B \ A
      // If A is not a complement set and B is a complement set:
      //    by Set Difference Law, A intersection B^C = A \ B
      return new SimpleSet(false, isComplement ?
              other.points.difference(this.points) : points.difference(other.points));
    }
  }

  /**
   * Returns the difference of this and other.
   * @param other Set to difference from this one.
   * @spec.requires other != null
   * @return this minus other
   */
  public SimpleSet difference(SimpleSet other) {
    // TODO:
    //       include sufficient comments to see why it is correct

    if (isComplement && other.isComplement)
    {
      // R \ {1,2,3,4,5} \ R \ {1,2,3} = {4,5}
      // R \ {1,2,3} \ R \ {1,2,3,4,5} = R \ ({1,2,3,4,5} \ {1,2,3})
      return new SimpleSet(false, other.points.difference(points));
    }
    else if (!isComplement && !other.isComplement)
    {
      return new SimpleSet(false, points.difference(other.points));
    }
    else
    {
      // {1,2,3,4,5} \ (R \ {1,2,3}) = {1,2,3}
      // foxes, wolves, fuskies - (all animals not fuskies,birds) = fuskies
      // R \ {1,2,3} \ {1,2,3,4,5} = R \ {1,2,3,4,5}
      return new SimpleSet(isComplement, isComplement ?
              other.points.union(this.points) : other.points.intersection(this.points));
    }
  }

}
