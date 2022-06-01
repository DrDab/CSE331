// represents an edge in the line mapper w/ unique ID string, color string and
// start/end coordinates (x1,y1) and (x2,y2) in line-mapper coordinates, and
// cost, in arbitrary units.
export interface Edge 
{
    id: string;
    color: string;
    x1: number;
    y1: number;
    x2: number;
    y2: number;
    cost: number;
}

// represents a 2D point in line-mapper coordinates.
export interface Point
{
    x:number,
    y:number
}
