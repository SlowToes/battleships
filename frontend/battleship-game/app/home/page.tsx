"use client";

import { useEffect, useState, useCallback } from "react";
import { useRouter } from "next/navigation";
import { Client, IMessage } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import {Card, CardContent} from "@/components/ui/card";
import {Button} from "@/components/ui/button";
import Image from "next/image";

interface GameStatusMessage {
    gameId: string;
    status: "GAME_READY" | "PLAYER_JOINED" | "PLACING_SHIPS" | string;
    message: string;
}

const API_BASE_URL = "http://localhost:8080";
const STOMP_ENDPOINT = `${API_BASE_URL}/gameplay`;

const getInitialUsername = (): string | null => {
    if (typeof window !== 'undefined') {
        return localStorage.getItem('currentUser');
    }
    return null;
};

const useGameSocket = (
    gameId: string | null,
    onGameReady: (id: string) => void
) => {
    const router = useRouter();

    useEffect(() => {
        if (!gameId) return;

        const client = new Client({
            webSocketFactory: () => new SockJS(STOMP_ENDPOINT),
            reconnectDelay: 5000,
            onConnect: () => {
                client.subscribe(`/topic/game/${gameId}`, (message: IMessage) => {
                    try {
                        const payload: GameStatusMessage = JSON.parse(message.body);
                        if (payload.status === "GAME_READY") {
                            onGameReady(payload.gameId);
                        }
                    } catch (e) {
                        console.error("Error processing STOMP message:", e);
                    }
                });
            },
            onStompError: (frame) => {
                console.error('[STOMP] Broker reported error: ' + frame.headers['message']);
                console.error('[STOMP] Additional details: ' + frame.body);
            },
            onDisconnect: () => {
                console.log('[STOMP] Disconnected');
            }
        });

        client.activate();

        return () => {
            if (client.active) {
                void client.deactivate();
            }
        };

    }, [gameId, onGameReady, router]);
};

export default function GamePage() {
    const router = useRouter();

    const [mode, setMode] = useState<"idle" | "creating" | "waiting" | "joiningInput" | "joiningConnecting">("idle");
    const [username] = useState<string | null>(getInitialUsername());
    const [gameIdInput, setGameIdInput] = useState("");
    const [activeGameId, setActiveGameId] = useState<string | null>(null);
    const [errorMessage, setErrorMessage] = useState("");

    useEffect(() => {
        if (!username) {
            router.push("/");
        }
    }, [username, router]);

    const navigateToPlaceShip = useCallback((id: string) => {
        router.replace(`/placeship?gameId=${id}`);
    }, [router]);

    useGameSocket(activeGameId, navigateToPlaceShip);

    const createGame = async () => {
        setErrorMessage("");
        setMode("creating");

        try {
            const res = await fetch(`${API_BASE_URL}/api/games/create`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ username }),
            });

            const data: { gameId?: string, message?: string } = await res.json();

            if (!res.ok || !data.gameId) {
                setErrorMessage(data.message || "Player not found. Please try again.");
                setMode("idle");
            }
            else {
                setActiveGameId(data.gameId);
                setMode("waiting");
            }
        } catch (error) {
            console.error("Network error during game creation:", error);
            setErrorMessage("Network error or server unreachable.");
            setMode("idle");
        }
    };

    const joinGame = async () => {
        setErrorMessage("");

        const idToJoin = gameIdInput.trim();
        if (!idToJoin) {
            return setErrorMessage("Please enter a Game ID.");
        }

        setMode("joiningConnecting");

        try {
            const res = await fetch(`${API_BASE_URL}/api/games/${idToJoin}/join`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ username }),
            });

            const data = await res.json();
            if (!res.ok) {
                if (res.status === 404) {
                    setErrorMessage("Game not found. Please check the Game ID.");
                }
                else if (res.status === 400 && data?.message) {
                    setErrorMessage(data.message);
                }
                else {
                    setErrorMessage("Unable to join the game. Please try again.");
                }
                setMode("joiningInput");
            }
            else {
                setActiveGameId(idToJoin);
                navigateToPlaceShip(idToJoin);
            }
        } catch (error) {
            console.error("Network error during game join:", error);
            setErrorMessage("Network error or server unreachable.");
            setMode("joiningInput");
        }
    };

    const logout = () => {
        localStorage.removeItem("currentUser");
        setActiveGameId(null);
        setGameIdInput("");
        setMode("idle");
        router.replace("/");
    };

    return (
        <main className="min-h-screen flex flex-col items-center justify-center bg-gray-900 text-white p-6 gap-6">
            {username && (
                <h1 className="text-4xl font-bold text-center">
                    Welcome {username}!
                </h1>
            )}
            <Card className="bg-gray-800 text-white max-w-3xl w-full rounded-2xl shadow-lg p-8">
                <CardContent className="flex gap-8">
                    <div className="flex justify-end mb-4"></div>
                    <div className="flex gap-8">
                        <Image
                            src="https://cdn.dribbble.com/userupload/22555646/file/original-42564b58ad494881ba20ceedf3a6656b.gif"
                            width={1000}
                            height={1000}
                            alt="Battleships GIF"
                            className="w-1/2 h-auto rounded-xl shadow-lg object-cover"
                        />
                        <div className="flex flex-col w-1/2 justify-center items-center text-center gap-6">
                            {mode === "idle" && (
                                <>
                                    <h1 className="text-3xl font-bold">Battleships Online</h1>
                                    <Button
                                        onClick={createGame}
                                        className="bg-blue-600 hover:bg-blue-700 text-white px-5 py-3 rounded-xl shadow w-full"
                                    >
                                        Create Game
                                    </Button>
                                    <Button
                                        onClick={() => {
                                            setErrorMessage("");
                                            setMode("joiningInput");
                                        }}
                                        className="bg-green-600 hover:bg-green-700 text-white px-5 py-3 rounded-xl shadow w-full"
                                    >
                                        Join Game
                                    </Button>
                                </>
                            )}
                            {mode === "joiningInput" && (
                                <div className="mt-10 flex flex-col items-center text-center gap-2">
                                    <h2 className="text-2xl font-semibold mb-4">Join a Game</h2>
                                    <input
                                        placeholder="Enter Game ID"
                                        className="w-full px-3 py-2 rounded bg-gray-700 mb-4"
                                        value={gameIdInput}
                                        onChange={(e) => setGameIdInput(e.target.value)}
                                    />
                                    {errorMessage && (
                                        <p className="text-red-400 text-sm font-medium mb-1">{errorMessage}</p>
                                    )}
                                    <Button
                                        onClick={joinGame}
                                        className="bg-green-600 hover:bg-green-700 text-white px-5 py-3 rounded-xl shadow w-full"
                                    >
                                        Connect
                                    </Button>
                                    <Button
                                        variant="link"
                                        onClick={() => {
                                            setGameIdInput("");
                                            setMode("idle");
                                        }}
                                        className="text-shadow-md text-gray-400 underline hover:text-gray-300"
                                    >
                                        Back
                                    </Button>
                                </div>
                            )}
                            {mode === "waiting" && activeGameId && (
                                <div className="mt-10 flex flex-col items-center text-center gap-4">
                                    <h2 className="text-2xl font-semibold whitespace-nowrap overflow-hidden text-ellipsis">
                                        Waiting for player to join...
                                    </h2>
                                    <p className="text-lg opacity-80">
                                        Share this Game ID:
                                    </p>
                                    <div className="bg-gray-700 p-4 rounded-xl text-xl font-mono tracking-wider select-text px-10">
                                        {activeGameId}
                                    </div>
                                    <Button
                                        variant="link"
                                        onClick={() => {
                                            setGameIdInput("");
                                            setErrorMessage("");
                                            setMode("idle");
                                        }}
                                        className="text-shadow-md text-gray-400 underline hover:text-gray-300"
                                    >
                                        Back
                                    </Button>
                                </div>
                            )}
                        </div>
                    </div>
                </CardContent>
            </Card>
            <Button
                variant="link"
                onClick={logout}
                className="text-shadow-md text-gray-300 underline hover:text-white"
            >
                Logout
            </Button>
        </main>
    );
}