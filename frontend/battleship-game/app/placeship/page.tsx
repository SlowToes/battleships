"use client";

import { useEffect, useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import { Card, CardContent } from "@/components/ui/card";
import { Button } from "@/components/ui/button";

const GRID_SIZE = 10;

export default function PlaceShipPage() {
    const router = useRouter();
    const searchParams = useSearchParams();
    const gameId = searchParams.get('gameId');

    const [placementStatus, setPlacementStatus] = useState<"placing" | "ready">("placing");

    useEffect(() => {
        if (!gameId) {
            console.error("No Game ID found. Redirecting to game selection.");
            router.replace("/game");
        }
    }, [gameId, router]);

    const completePlacement = () => {
        setPlacementStatus("ready");
        console.log(`Ship placement complete for Game ID: ${gameId}`);
    };

    if (!gameId) {
        return (
            <main className="min-h-screen flex items-center justify-center bg-gray-900 text-white p-6">
                <p>Loading game...</p>
            </main>
        );
    }

    const renderGridPlaceholder = () => {
        const rows = Array(GRID_SIZE).fill(0);
        const cols = Array(GRID_SIZE).fill(0);

        return (
            <div className="grid grid-cols-10 grid-rows-10 w-96 h-96 border-2 border-blue-500">
                {rows.flatMap((_, rowIdx) =>
                    cols.map((__, colIdx) => (
                        <div
                            key={`${rowIdx}-${colIdx}`}
                            className="w-full h-full border border-gray-700 flex items-center justify-center text-xs opacity-50"
                        >
                        </div>
                    ))
                )}
            </div>
        );
    };

    return (
        <main className="min-h-screen flex flex-col items-center justify-center bg-gray-900 text-white p-6 gap-6">
            <h1 className="text-4xl font-bold text-center">
                Prepare for Battle!
            </h1>
            <Card className="bg-gray-800 text-white max-w-4xl w-full rounded-2xl shadow-lg p-8">
                <CardContent className="flex flex-col gap-6 items-center">
                    <h2 className="text-2xl font-semibold">
                        Game ID: <span className="text-blue-400 font-mono">{gameId}</span>
                    </h2>

                    {placementStatus === "placing" ? (
                        <>
                            <p className="text-lg text-center opacity-90">
                                Place your ships on the grid below.
                            </p>
                            {renderGridPlaceholder()}
                            <div className="flex gap-4 mt-4">
                                <Button
                                    onClick={completePlacement}
                                    className="bg-green-600 hover:bg-green-700 text-white px-8 py-3 rounded-xl shadow text-lg"
                                >
                                    Confirm Placement
                                </Button>
                                <Button
                                    variant="link"
                                    onClick={() => router.push("/game")}
                                    className="text-gray-400 underline hover:text-gray-300"
                                >
                                    Cancel Game
                                </Button>
                            </div>
                        </>
                    ) : (
                        <div className="text-center mt-4">
                            <h3 className="text-3xl text-yellow-400 font-bold mb-4">
                                Ships Placed. Awaiting Opponent...
                            </h3>
                            <p className="text-lg opacity-80">
                                The battle will begin shortly!
                            </p>
                        </div>
                    )}
                </CardContent>
            </Card>

        </main>
    );
}