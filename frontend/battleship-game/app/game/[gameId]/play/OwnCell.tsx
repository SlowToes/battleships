"use client";

import React from "react";
import { OwnCellState } from "@/lib/types";

interface OwnCellProps {
    state: OwnCellState;
}

export function OwnCell({ state }: OwnCellProps) {
    let bg = "bg-slate-700";

    if (state === "SHIP") bg = "bg-blue-600";
    if (state === "HIT") bg = "bg-red-600";
    if (state === "MISS") bg = "bg-gray-600";

    return (
        <div className={`w-12 h-12 border border-gray-900 flex items-center justify-center font-bold`}>
            <div className={`${bg} w-full h-full flex items-center justify-center`}>
                {state === "HIT" && "X"}
                {state === "MISS" && "•"}
            </div>
        </div>
    );
}
