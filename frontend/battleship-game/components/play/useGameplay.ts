"use client";

import { useEffect, useState } from "react";
import { useGameplaySocket } from "@/components/play/useGameplaySocket";
import { Coordinate, FireResult, FireResultType, OwnCellState, TargetCellState } from "@/lib/types";
import { formatShipName } from "@/lib/utils";
import { CONFIG } from "@/lib/config";

export function useGameplay(gameId: string) {
    const [statusMessage, setStatusMessage] = useState("Game Started!");
    const [errorMessage, setErrorMessage] = useState("");
    const [myUsername, setMyUsername] = useState<string | null>(null);
    const [winnerUsername, setWinnerUsername] = useState<string | null>(null);
    const [isMyTurn, setIsMyTurn] = useState(false);
    const [isGameOver, setIsGameOver] = useState(false);
    const [ownBoard, setOwnBoard] = useState<OwnCellState[][]>(
        Array(11).fill(null).map(() => Array(11).fill("EMPTY"))
    );
    const [targetBoard, setTargetBoard] = useState<TargetCellState[][]>(
        Array(11).fill(null).map(() => Array(11).fill("EMPTY"))
    );

    useEffect(() => {
        fetch(`${CONFIG.API_URL}/api/play/me`, {
            credentials: "include",
        })
            .then(async (res) => {
                if (!res.ok) {
                    setErrorMessage("Error getting username from server");
                    return;
                }
                const text = await res.text();
                setMyUsername(text);
            })
    }, []);

    useEffect(() => {
        if (!myUsername) return;

        fetch(`${CONFIG.API_URL}/api/play/${gameId}/coordinates`, {
            credentials: "include",
        })
            .then(async (res) => {
                if (!res.ok) {
                    setErrorMessage("Failed to fetch coordinates");
                    return;
                }
                const coordinates: Coordinate[] = await res.json();

                setOwnBoard(prev => {
                    const newBoard = prev.map(row => [...row]);
                    coordinates.forEach(coordinate => {
                        if (newBoard[coordinate.row] && newBoard[coordinate.row][coordinate.col]) {
                            newBoard[coordinate.row][coordinate.col] = "SHIP";
                        }
                    });
                    return newBoard;
                });
            })
    }, [gameId, myUsername]);

    useEffect(() => {
        if (!myUsername) return;

        fetch(`${CONFIG.API_URL}/api/play/${gameId}/initial`, {
            credentials: "include",
        })
            .then(res => res.text())
            .then(startingUsername => {
                const isItMyTurn = startingUsername === myUsername;
                setIsMyTurn(isItMyTurn);
                setStatusMessage(isItMyTurn ? "Your turn!" : "Waiting for opponent...");
            })
    }, [gameId, myUsername]);

    useGameplaySocket(gameId, {
        onFireResult: (result) => {
            const { coordinate, fireResultType, currentPlayerUsername } = result;
            if (!coordinate) return;

            const r = coordinate.row;
            const c = coordinate.col;

            const isMyTurn = (
                ((fireResultType === FireResultType.HIT || fireResultType === FireResultType.SANK_SHIP || fireResultType === FireResultType.GAME_OVER) && currentPlayerUsername === myUsername) ||
                (fireResultType === FireResultType.MISS && currentPlayerUsername !== myUsername)
            );

            if (isMyTurn) {
                setTargetBoard(prev => {
                    const copy = prev.map(row => [...row]);
                    copy[r][c] = fireResultType === FireResultType.MISS ? "MISS" : "HIT";
                    return copy;
                });
            } else {
                setOwnBoard(prev => {
                    const copy = prev.map(row => [...row]);
                    copy[r][c] = fireResultType === FireResultType.MISS ? "MISS" : "HIT";
                    return copy;
                });
            }

            setIsMyTurn(currentPlayerUsername === myUsername);
            handleStatusMessage(result, isMyTurn);
        }
    });

    const handleStatusMessage = (result: FireResult, isMyTurn: boolean) => {
        if (result.winnerUsername) {
            setWinnerUsername(result.winnerUsername);
            setStatusMessage(result.winnerUsername === myUsername ? `YOU SANK THEIR ${formatShipName(result.sunkShipName)}!` : `THEY SANK YOUR ${formatShipName(result.sunkShipName)}!`);
            setIsGameOver(true);
            return;
        }

        switch (result.fireResultType) {
            case FireResultType.HIT: {
                setStatusMessage(isMyTurn ? "YOU HIT!" : "OPPONENT HIT YOUR SHIP!");
                break;
            }
            case FireResultType.MISS: {
                setStatusMessage(isMyTurn ? "YOU MISSED!" : "OPPONENT MISSED!");
                break;
            }
            case FireResultType.SANK_SHIP: {
                setStatusMessage(isMyTurn ? `YOU SANK THEIR ${formatShipName(result.sunkShipName)}!` : `THEY SANK YOUR ${formatShipName(result.sunkShipName)}!`);
                break;
            }
        }
    };

    const fireAt = async (row: number, col: number) => {
        setErrorMessage("");
        if (!isMyTurn || targetBoard[row][col] !== "EMPTY") return;

        const res = await fetch(`${CONFIG.API_URL}/api/play/${gameId}/fire`, {
            method: "POST",
            credentials: "include",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ row, col }),
        });

        if (!res.ok) {
            setErrorMessage("Error when firing");
        }
    };

    return {
        statusMessage,
        errorMessage,
        myUsername,
        winnerUsername,
        isMyTurn,
        isGameOver,
        ownBoard,
        targetBoard,
        fireAt
    };
}
