package sets;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Arrays;

/**
 * FiniteSetTest is a glassbox test of the FiniteSet class.
 */
public class FiniteSetTest {

  /** Test creating sets. */
  @Test
  public void testCreation() {
    assertEquals(Arrays.asList(),
        FiniteSet.of(new float[0]).getPoints());         // empty
    assertEquals(Arrays.asList(1f),
        FiniteSet.of(new float[] {1}).getPoints());      // one item
    assertEquals(Arrays.asList(1f, 2f),
        FiniteSet.of(new float[] {1, 2}).getPoints());   // two items
    assertEquals(Arrays.asList(1f, 2f),
        FiniteSet.of(new float[] {2, 1}).getPoints());   // two out-of-order
    assertEquals(Arrays.asList(-2f, 2f),
        FiniteSet.of(new float[] {2, -2}).getPoints());  // negative
  }

  // Some example sets used by the tests below.
  private static FiniteSet S0 = FiniteSet.of(new float[0]);
  private static FiniteSet S1 = FiniteSet.of(new float[] {1});
  private static FiniteSet S12 = FiniteSet.of(new float[] {1, 2});

  /** Test set equality. */
  @Test
  public void testEquals() {
    assertTrue(S0.equals(S0));
    assertFalse(S0.equals(S1));
    assertFalse(S0.equals(S12));

    assertFalse(S1.equals(S0));
    assertTrue(S1.equals(S1));
    assertFalse(S1.equals(S12));

    assertFalse(S12.equals(S0));
    assertFalse(S12.equals(S1));
    assertTrue(S12.equals(S12));
  }

  /** Test set size. */
  @Test
  public void testSize() {
    assertEquals(S0.size(), 0);
    assertEquals(S1.size(), 1);
    assertEquals(S12.size(), 2);
  }

  private static FiniteSet EMPTY_SET = FiniteSet.of(new float[]{});
  private static FiniteSet S2 = FiniteSet.of(new float[]{2});

  /** Tests forming the union of two finite sets. */
  @Test
  public void testUnion() {
    // (a) test union of two empty sets a = {}, b = {} (expected result: a union b = {})
    FiniteSet a = FiniteSet.of(new float[]{});
    FiniteSet b = FiniteSet.of(new float[]{});
    assertEquals(EMPTY_SET, a.union(b));

    // (b.1) test union of two empty sets a = {1}, b = {} (expected result: a union b = {1})
    a = S1;
    b = EMPTY_SET;
    assertEquals(S1, a.union(b));

    // (b.2) test union of two empty sets a = {}, b = {1} (expected result: a union b = {1})
    a = EMPTY_SET;
    b = S1;
    assertEquals(S1, a.union(b));

    // (b.3) test union of two empty sets a = {-1}, b = {} (expected result: a union b = {-1})
    a = FiniteSet.of(new float[]{-1});
    b = EMPTY_SET;
    assertEquals(a, a.union(b));

    // (b.4) test union of two empty sets a = {}, b = {-1} (expected result: a union b = {-1})
    a = EMPTY_SET;
    b = FiniteSet.of(new float[]{-1});
    assertEquals(b, a.union(b));

    // (c.1) test union of sets a = {1}, b = {2} (expected result: a union b = {1,2})
    a = S1;
    b = FiniteSet.of(new float[]{2});
    assertEquals(S12, a.union(b));

    // (c.2) test union of sets a = {-1}, b = {2} (expected result: a union b = {-1,2})
    a = FiniteSet.of(new float[]{-1});
    b = S2;
    assertEquals(FiniteSet.of(new float[]{2,-1}), a.union(b));

    // (c.3) test union of sets a = {-1}, b = {-2} (expected result: a union b = {-1,2})
    a = FiniteSet.of(new float[]{-1});
    b = FiniteSet.of(new float[]{-2});
    assertEquals(FiniteSet.of(new float[]{-2,-1}), a.union(b));

    // (d.1) test union of sets a = {1,2,4,8}, b = {}
    // (expected result: a union b = {1,2,4,8})
    a = FiniteSet.of(new float[]{8,4,2,1});
    b = EMPTY_SET;
    assertEquals(FiniteSet.of(new float[]{1,2,4,8}), a.union(b));

    // (d.2) test union of sets a = {}, b = {1,2,4,8}
    // (expected result: a union b = {1,2,4,8})
    a = EMPTY_SET;
    b = FiniteSet.of(new float[]{8,4,2,1});
    assertEquals(FiniteSet.of(new float[]{1,2,4,8}), a.union(b));

    // (e.1) test union of sets a = {1,3,5,7}, b = {2,4,6,8}
    // (expected result: a union b = {1,2,3,4,5,6,7,8})
    a = FiniteSet.of(new float[]{3,7,5,1});
    b = FiniteSet.of(new float[]{6,2,8,4});
    assertEquals(FiniteSet.of(new float[]{1,2,3,4,5,6,7,8}), a.union(b));

    // (e.2) test union of sets a = {1,3,5,7}, b = {2,4}
    // (expected result: a union b = {1,2,3,4,5,7})
    a = FiniteSet.of(new float[]{1,3,5,7});
    b = FiniteSet.of(new float[]{2,4});
    assertEquals(FiniteSet.of(new float[]{1,2,3,4,5,7}), a.union(b));

    // (e.3) test union of sets a = {-3,1,5,7}, b = {-4,2}
    // (expected result: a union b = {-3,-4,1,2,5,7})
    a = FiniteSet.of(new float[]{1,-3,5,7});
    b = FiniteSet.of(new float[]{2,-4});
    assertEquals(FiniteSet.of(new float[]{1,2,-3,-4,5,7}), a.union(b));

    // (f) test union of sets a = {-0.45, 0.38, 0.45, 3.14}, b = {-0.45, 0.45, 3.14}
    // (expected result: a union b = {-0.45, 0.38, 0.45, 3.14})
    a = FiniteSet.of(new float[]{-0.45f, 0.38f, 0.45f, 3.14f});
    b = FiniteSet.of(new float[]{3.14f, -0.45f, 0.45f});
    assertEquals(FiniteSet.of(new float[]{0.45f, -0.45f, 3.14f, 0.38f}), a.union(b));
  }

  /** Tests forming the intersection of two finite sets. */
  @Test
  public void testIntersection() {
    // (a) test intersection of two empty sets a = {}, b = {} (expected result: a intersection b = {})
    FiniteSet a = FiniteSet.of(new float[]{});
    FiniteSet b = FiniteSet.of(new float[]{});
    assertEquals(EMPTY_SET, a.intersection(b));

    // (b.1) test intersection of sets a = {1}, b = {} (expected result: a intersection b = {})
    a = S1;
    b = EMPTY_SET;
    assertEquals(EMPTY_SET, a.intersection(b));

    // (b.2) test intersection of sets a = {}, b = {1} (expected result: a intersection b = {})
    a = EMPTY_SET;
    b = S1;
    assertEquals(EMPTY_SET, a.intersection(b));

    // (b.3) test intersection of sets a = {}, b = {-1} (expected result: a intersection b = {})
    a = EMPTY_SET;
    b = FiniteSet.of(new float[]{-1});
    assertEquals(EMPTY_SET, a.intersection(b));

    // (b.4) test intersection of sets a = {-1}, b = {} (expected result: a intersection b = {})
    a = FiniteSet.of(new float[]{-1});
    b = EMPTY_SET;
    assertEquals(EMPTY_SET, a.intersection(b));

    // (c.1) test intersection of sets a = {1}, b = {2} (expected result: a intersection b = {})
    a = S1;
    b = S2;
    assertEquals(EMPTY_SET, a.intersection(b));

    // (c.2) test intersection of sets a = {1}, b = {1} (expected result: a intersection b = {1})
    a = S1;
    b = FiniteSet.of(new float[]{1});
    assertEquals(S1, a.intersection(b));

    // (d.1) test intersection of sets a = {1,2}, b = {2} (expected result: a intersection b = {2})
    a = S12;
    b = S2;
    assertEquals(a.intersection(b), S2);

    // (d.2) test intersection of sets a = {-2,1}, b = {-2} (expected result: a intersection b = {-2})
    a = FiniteSet.of(new float[]{1,-2});
    b = FiniteSet.of(new float[]{-2});
    assertEquals(FiniteSet.of(new float[]{-2}), a.intersection(b));

    // (e) test intersection of sets a = {-4,0,4,8,15,16,23,42}, b = {-42,0,6,7,15,16,20,23,621}
    // (expected result: a intersection b = {15,16,23})
    a = FiniteSet.of(new float[]{-4,0,4,8,15,16,23,42});
    b = FiniteSet.of(new float[]{-42,0,6,7,15,16,20,23,621});
    assertEquals(FiniteSet.of(new float[]{0,15,16,23}), a.intersection(b));

    // (f) test intersection of sets a = {-0.45, 0.0, 0.38, 0.45}, b = {-0.45, 0.22, 0.45}
    // (expected result: a intersection b = {-0.45, 0.45})
    a = FiniteSet.of(new float[]{-0.45f, 0.38f, 0.45f, 0.0f});
    b = FiniteSet.of(new float[]{0.22f, -0.45f, 0.45f});
    assertEquals(FiniteSet.of(new float[]{0.45f, -0.45f}), a.intersection(b));
  }

  /** Tests forming the difference of two finite sets. */
  @Test
  public void testDifference() {
    // (a) test difference of sets a = {}, b = {} (expected result: a \ b = {})
    FiniteSet a = EMPTY_SET;
    FiniteSet b = FiniteSet.of(new float[]{});
    FiniteSet diffResult = a.difference(b);
    assertEquals(a, diffResult);
    assertEquals(b, diffResult);
    assertEquals(EMPTY_SET, diffResult);

    // (b.1) test difference of sets a = {1}, b = {} (expected result: a \ b = {1})
    a = S1;
    b = EMPTY_SET;
    assertEquals(S1, a.difference(b));

    // (b.2) test difference of sets a = {}, b = {1} (expected result: a \ b = {})
    a = EMPTY_SET;
    b = S1;
    assertEquals(EMPTY_SET, a.difference(b));

    // (b.3) test difference of sets a = {-1}, b = {} (expected result: a \ b = {-1})
    a = FiniteSet.of(new float[]{-1});
    b = EMPTY_SET;
    assertEquals(a, a.difference(b));

    // (b.4) test difference of sets a = {}, b = {-1} (expected result: a \ b = {})
    a = EMPTY_SET;
    b = FiniteSet.of(new float[]{-1});
    assertEquals(EMPTY_SET, a.difference(b));

    // (c) test difference of sets a = {1,2}, b = {} (expected result: a \ b = {1,2})
    a = S12;
    b = EMPTY_SET;
    assertEquals(S12, a.difference(b));

    // (d.1) test difference of sets a = {1,2}, b = {2} (expected result: a \ b = {1})
    a = S12;
    b = S2;
    assertEquals(S1, a.difference(b));

    // (d.2) test difference of sets a = {1,2}, b = {1} (expected result: a \ b = {2})
    a = S12;
    b = S1;
    assertEquals(S2, a.difference(b));

    // (e.1) test difference of sets a = {1,2,3,4,5}, b = {1,2,3} (expected result: a \ b = {4,5})
    a = FiniteSet.of(new float[]{1,2,3,4,5});
    b = FiniteSet.of(new float[]{2,1,3});
    assertEquals(FiniteSet.of(new float[]{5,4}), a.difference(b));

    // (e.2) test difference of sets a = {-3,-1,2,4,5}, b = {-3,-1,2} (expected result: a \ b = {4,5})
    a = FiniteSet.of(new float[]{-1,2,-3,4,5});
    b = FiniteSet.of(new float[]{2,-1,-3});
    assertEquals(FiniteSet.of(new float[]{5,4}), a.difference(b));

    // (f) test difference of sets a = {-0.45, 0.0, 0.38, 0.45}, b = {-0.45, 0.22, 0.45}
    // (expected result: a \ b = {0.0, 0.38)
    a = FiniteSet.of(new float[]{-0.45f, 0.38f, 0.45f, 0.0f});
    b = FiniteSet.of(new float[]{0.22f, -0.45f, 0.45f});
    assertEquals(FiniteSet.of(new float[]{0.38f, 0.0f}), a.difference(b));
  }

}
