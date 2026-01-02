"use client";

import React from "react";
import { TargetCellState } from "@/lib/types";

interface TargetCellProps {
    row: number;
    col: number;
    state: TargetCellState;
    disabled: boolean;
    onClick: (row: number, col: number) => void;
}

export function TargetCell({ row, col, state, disabled, onClick }: TargetCellProps) {
    let bg = "bg-slate-700";

    if (state === "HIT") bg = "bg-red-600";
    if (state === "MISS") bg = "bg-gray-600";

    const isDisabled = disabled || state !== "EMPTY";

    return (
        <button
            disabled={isDisabled}
            className={`w-12 h-12 border border-gray-900 ${bg}
                ${!isDisabled ? "hover:bg-blue-600" : "cursor-not-allowed"}
            `}
            onClick={() => onClick(row, col)}
        >
            {state === "HIT" && "X"}
            {state === "MISS" && "•"}
        </button>
    );
}
