"use client";

import React from "react";
import GridCell from "./GridCell";
import ShipDock from "./ShipDock";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { usePlaceShipSocket } from "@/components/place-ship/usePlaceShipSocket";
import { useShipPlacement } from "@/components/place-ship/useShipPlacement";
import { GRID_SIZE } from "@/lib/types";

export default function ShipPlacementPage({ params }: {
    params: Promise<{ gameId: string }>;
}) {
    const { gameId } = React.use(params);

    usePlaceShipSocket(gameId);

    const {
        ships,
        selectedShip,
        orientation,
        occupied,
        errorMessage,
        setSelectedShip,
        rotate,
        handleHover,
        clearHover,
        handlePlace,
        handleReset,
        handleReady,
        cellHighlight
    } = useShipPlacement(gameId);

    return (
        <main className="min-h-screen flex flex-col items-center justify-center bg-gray-900 text-white p-6">
            <Card className="bg-gray-800 text-white w-full max-w-5xl rounded-2xl shadow-lg p-8 flex flex-col items-center gap-6">
                <h1 className="text-3xl font-bold text-center">Place Your Ships</h1>
                <p className="text-gray-300 text-center max-w-xl">
                    Select a ship, hover over the grid to preview placement, rotate if needed, then click to place. Place all ships before pressing <b>READY</b>.
                </p>

                <div className="flex gap-12 items-start justify-center">
                    <div
                        className="grid"
                        style={{
                            gridTemplateColumns: `repeat(${GRID_SIZE}, 3rem)`,
                            gap: "0px"
                        }}
                    >
                        {Array.from({ length: GRID_SIZE }, (_, r) =>
                            Array.from({ length: GRID_SIZE }, (_, c) => {
                                const row = r + 1;
                                const col = c + 1;

                                return (
                                    <GridCell
                                        key={`${row}-${col}`}
                                        row={row}
                                        col={col}
                                        occupied={occupied.has(`${row},${col}`)}
                                        highlight={cellHighlight(row, col)}
                                        onHover={handleHover}
                                        onLeave={clearHover}
                                        onClick={handlePlace}
                                    />
                                );
                            })
                        )}
                    </div>

                    <ShipDock
                        ships={ships}
                        selectedShip={selectedShip}
                        orientation={orientation}
                        onSelectShip={setSelectedShip}
                        onRotate={rotate}
                        onReset={handleReset}
                    />
                </div>

                <Button onClick={handleReady} className="mt-4 px-10 py-4 bg-green-600 hover:bg-green-700 text-lg font-bold rounded-xl hover: cursor-pointer">
                    READY
                </Button>

                {errorMessage && (<p className="text-red-400 text-center">{errorMessage}</p>)}
            </Card>
        </main>
    );
}
