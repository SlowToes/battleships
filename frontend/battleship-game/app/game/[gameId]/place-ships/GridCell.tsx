"use client";

interface GridCellProps {
    row: number;
    col: number;
    highlight: "VALID" | "INVALID" | "NONE";
    occupied: boolean;
    onHover: (row: number, col: number) => void;
    onLeave: () => void;
    onClick: (row: number, col: number) => void;
}

export default function GridCell({row, col, highlight, occupied, onHover, onLeave, onClick}: GridCellProps) {
    let bg = "bg-slate-700";

    if (occupied) bg = "bg-gray-500";
    if (highlight === "VALID") bg = "bg-blue-500";
    if (highlight === "INVALID") bg = "bg-red-500";

    return (
        <div
            className={`w-12 h-12 border border-gray-900 ${bg}`}
            onMouseEnter={() => onHover(row, col)}
            onMouseLeave={onLeave}
            onClick={() => onClick(row, col)}
        />
    );
}
