"use client";

import React from "react";
import { OwnCell } from "./OwnCell";
import { OwnCellState } from "@/lib/types";

interface OwnBoardProps {
    board: OwnCellState[][];
}

const LETTERS = ["A","B","C","D","E","F","G","H","I","J"];

export function OwnBoard({ board }: OwnBoardProps) {
    return (
        <div
            className="grid"
            style={{
                gridTemplateColumns: "3rem repeat(10, 3rem)",
                gap: "0px"
            }}
        >
            <div className="w-12 h-12" />

            {LETTERS.map(letter => (
                <div key={letter} className="w-12 h-12 flex items-center justify-center text-gray-400 font-bold">
                    {letter}
                </div>
            ))}

            {board.slice(1).map((row, rowIdx) => (
                <React.Fragment key={rowIdx}>
                    <div className="w-12 h-12 flex items-center justify-center text-gray-400 font-bold">
                        {rowIdx + 1}
                    </div>

                    {row.slice(1).map((cell, colIdx) => (
                        <OwnCell
                            key={`${rowIdx}-${colIdx}`}
                            state={cell}
                        />
                    ))}
                </React.Fragment>
            ))}
        </div>
    );
}
