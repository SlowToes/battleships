"use client";

import { useState} from "react";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { useGameManagement } from "@/components/home/useGameManagement";
import { useAuth } from "@/components/auth/useAuth";
import { useHomeSocket } from "@/components/home/useHomeSocket";
import { HomeMode } from "@/lib/types";
import Image from "next/image";

export default function HomePage() {
    const [activeGameId, setActiveGameId] = useState<string | null>(null);
    const [mode, setMode] = useState<HomeMode>("IDLE");

    useHomeSocket(activeGameId);

    const {
        gameIdInput,
        setGameIdInput,
        errorMessage,
        setErrorMessage,
        loading,
        createGame,
        joinGame
    } = useGameManagement(setActiveGameId, setMode);

    const { logout } = useAuth();

    return (
        <main className="min-h-screen flex flex-col items-center justify-center bg-gray-900 text-white p-6 gap-6">
            <Card className="bg-gray-800 text-white max-w-3xl w-full rounded-2xl shadow-lg p-8">
                <CardContent className="flex gap-8">
                    <Image
                        src="https://cdn.dribbble.com/userupload/22555646/file/original-42564b58ad494881ba20ceedf3a6656b.gif"
                        width={1000}
                        height={1000}
                        alt="Battleships GIF"
                        className="w-1/2 h-auto rounded-xl"
                    />
                    <div className="flex flex-col w-1/2 justify-center items-center text-center gap-6">

                        {mode === "IDLE" && (
                            <>
                                <h1 className="text-3xl font-bold">Battleships Online</h1>
                                <Button className="w-full bg-blue-600 hover:cursor-pointer" onClick={createGame} disabled={loading}>
                                    Create Game
                                </Button>
                                <Button className="w-full bg-green-600 hover:cursor-pointer" onClick={() => {
                                        setErrorMessage("");
                                        setMode("JOINING_INPUT");
                                }}>
                                    Join Game
                                </Button>
                            </>
                        )}

                        {mode === "JOINING_INPUT" && (
                            <div className="flex flex-col w-full text-center gap-3">
                                <h2 className="text-2xl font-semibold">Join a Game</h2>
                                <Input
                                    placeholder="Enter Game ID"
                                    className="w-full px-3 py-2 rounded bg-gray-700"
                                    value={gameIdInput}
                                    onChange={(e) => setGameIdInput(e.target.value)}
                                />
                                {errorMessage && (<p className="text-red-400">{errorMessage}</p>)}
                                <Button className="bg-green-600 w-full hover: cursor-pointer" onClick={joinGame} disabled={loading}>
                                    Connect
                                </Button>
                                <Button className="text-white hover:text-gray-200 hover: cursor-pointer" variant="link" onClick={() => {
                                        setErrorMessage("");
                                        setMode("IDLE");
                                }}>
                                    Back
                                </Button>
                            </div>
                        )}

                        {mode === "WAITING" && activeGameId && (
                            <div className="flex flex-col text-center gap-4">
                                <h2 className="text-2xl font-semibold">Waiting for player...</h2>
                                <p className="opacity-80">Game ID:</p>
                                <div className="bg-gray-700 p-4 rounded-xl text-xl font-mono">{activeGameId}</div>
                            </div>
                        )}
                    </div>
                </CardContent>
            </Card>

            <Button className="text-shadow-md text-gray-300 underline hover:text-white hover:cursor-pointer" onClick={logout} variant="link">
                Logout
            </Button>
        </main>
    );
}
