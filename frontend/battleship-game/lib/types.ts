export type HomeMode = "IDLE" | "CREATING" | "WAITING" | "JOINING_INPUT" | "JOINING_CONNECTING";

export type Orientation = "HORIZONTAL" | "VERTICAL";

export type OwnCellState = "EMPTY" | "SHIP" | "HIT" | "MISS";

export type TargetCellState = "EMPTY" | "HIT" | "MISS";

export enum ShipName {
    CARRIER = "CARRIER",
    BATTLESHIP = "BATTLESHIP",
    DESTROYER = "DESTROYER",
    SUBMARINE = "SUBMARINE",
    PATROL_BOAT = "PATROL_BOAT"
}

export enum FireResultType {
    MISS = "MISS",
    HIT = "HIT",
    SANK_SHIP = "SANK_SHIP",
    GAME_OVER = "GAME_OVER"
}

export const SHIPS: Ship[] = [
    {name: ShipName.CARRIER,     size: 5, placed: false},
    {name: ShipName.BATTLESHIP,  size: 4, placed: false},
    {name: ShipName.DESTROYER,   size: 3, placed: false},
    {name: ShipName.SUBMARINE,   size: 3, placed: false},
    {name: ShipName.PATROL_BOAT, size: 2, placed: false},
];

export const GRID_SIZE = 10;

export interface Ship {
    name: ShipName;
    size: number;
    placed: boolean;
}

export interface Coordinate {
    row: number;
    col: number;
}

export interface FireResult {
    coordinate: Coordinate;
    fireResultType: FireResultType;
    sunkShipName: ShipName | null;
    currentPlayerUsername: string | null;
    winnerUsername: string | null;
}
