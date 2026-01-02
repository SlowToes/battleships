"use client";

import React from "react";
import { TargetCell } from "./TargetCell";
import { TargetCellState } from "@/lib/types";

interface TargetBoardProps {
    board: TargetCellState[][];
    onFire: (row: number, col: number) => void;
    isMyTurn: boolean;
}

const LETTERS = ["A","B","C","D","E","F","G","H","I","J"];

export function TargetBoard({ board, onFire, isMyTurn }: TargetBoardProps) {
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
                        <TargetCell
                            key={`${rowIdx}-${colIdx}`}
                            row={rowIdx + 1}
                            col={colIdx + 1}
                            state={cell}
                            disabled={!isMyTurn}
                            onClick={onFire}
                        />
                    ))}
                </React.Fragment>
            ))}
        </div>
    );
}
