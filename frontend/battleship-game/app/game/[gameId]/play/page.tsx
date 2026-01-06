"use client"

import React from 'react';
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { useRouter } from "next/navigation";
import { useGameplay } from "@/components/play/useGameplay";
import { OwnBoard } from "@/app/game/[gameId]/play/OwnBoard"
import { TargetBoard } from "@/app/game/[gameId]/play/TargetBoard";

export default function GameplayPage({ params }: {
    params: Promise<{ gameId: string }>;
}) {
    const { gameId } = React.use(params);
    const router = useRouter();

    const {
        statusMessage,
        errorMessage,
        myUsername,
        opponentUsername,
        winnerUsername,
        isMyTurn,
        isGameOver,
        ownBoard,
        targetBoard,
        fireAt
    } = useGameplay(gameId);

    const colorClass = winnerUsername === myUsername ? "text-7xl text-green-500" : "text-7xl text-red-600";
    const animationClass = winnerUsername === myUsername ? "animate-bounce" : "animate-pulse";

    return (
        <main className="min-h-screen flex flex-col items-center justify-center bg-gray-900 text-white p-6">
            <Card className="bg-gray-800 text-white w-full max-w-7xl rounded-2xl shadow-lg p-8 flex flex-col items-center gap-6">
                <h1 className="text-6xl font-bold">Battleships</h1>

                <p className={`text-3xl ${isMyTurn ? "text-green-400" : "text-yellow-400"}`}>{statusMessage}</p>

                <div className="flex gap-12">
                    <div className="flex flex-col items-center gap-2">
                        <h2 className="text-2xl font-semibold">Your Board ({myUsername})</h2>
                        <OwnBoard board={ownBoard} />
                    </div>

                    <div className="flex flex-col items-center gap-2">
                        <h2 className="text-2xl font-semibold">Target Board ({opponentUsername})</h2>
                        <TargetBoard
                            board={targetBoard}
                            onFire={fireAt}
                            isMyTurn={isMyTurn}
                        />
                    </div>
                </div>

                {isGameOver && (
                    <div className="text-center space-y-2">
                        <p className={`${colorClass} ${animationClass}`}>
                            {winnerUsername === myUsername ? "VICTORY" : "DEFEAT"}
                        </p>
                    </div>
                )}

                {isGameOver && (
                    <Button
                        onClick={() => router.replace("/home")}
                        className="px-6 py-2 bg-blue-600 hover:bg-blue-500 rounded-lg font-bold"
                    >
                        Return to Main Menu
                    </Button>
                )}

                {errorMessage && <p className="text-red-400">{errorMessage}</p>}
            </Card>
        </main>
    );
}
