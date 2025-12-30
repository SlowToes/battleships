export type Orientation = "HORIZONTAL" | "VERTICAL";

export interface Ship {
    name: string;
    size: number;
    placed: boolean;
}

export interface Coordinate {
    row: number;
    col: number;
}

export const SHIPS: Ship[] = [
    {name: "CARRIER",     size: 5, placed: false},
    {name: "BATTLESHIP",  size: 4, placed: false},
    {name: "DESTROYER",   size: 3, placed: false},
    {name: "SUBMARINE",   size: 3, placed: false},
    {name: "PATROL_BOAT", size: 2, placed: false},
];

export const GRID_SIZE = 10;
