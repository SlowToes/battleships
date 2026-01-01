import { Coordinate, Orientation, GRID_SIZE } from "@/lib/types";

export function calculateCoordinates(
    start: Coordinate,
    size: number,
    orientation: Orientation
): Coordinate[] {
    const coords: Coordinate[] = [];

    for (let i = 0; i < size; i++) {
        coords.push({
            row: orientation === "VERTICAL" ? start.row + i : start.row,
            col: orientation === "HORIZONTAL" ? start.col + i : start.col,
        });
    }

    return coords;
}

export function isOutOfBounds(coords: Coordinate[]): boolean {
    return coords.some(
        c => c.row < 1 || c.row > GRID_SIZE || c.col < 1 || c.col > GRID_SIZE
    );
}

export function overlapsExisting(
    coords: Coordinate[],
    occupied: Set<string>
): boolean {
    return coords.some(c => occupied.has(`${c.row},${c.col}`));
}
