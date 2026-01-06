"use client";

import React, {useEffect, useState} from "react";
import { HomeMode } from "@/lib/types";
import { CONFIG } from "@/lib/config";

export function useGameManagement(
    setActiveGameId: React.Dispatch<React.SetStateAction<string | null>>,
    setMode: React.Dispatch<React.SetStateAction<HomeMode>>
) {
    const [gameIdInput, setGameIdInput] = useState("");
    const [myUsername, setMyUsername] = useState<string | null>(null);
    const [errorMessage, setErrorMessage] = useState("");
    const [loading, setLoading] = useState(false);

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

    const createGame = async () => {
        setErrorMessage("");
        setLoading(true);
        setMode("CREATING");

        const res = await fetch(`${CONFIG.API_URL}/api/games/create`, {
            method: "POST",
            credentials: "include",
        });

        if (!res.ok) {
            setErrorMessage("Failed to create game");
            setMode("IDLE");
            setLoading(false);
            return;
        }

        const data = await res.json();
        setActiveGameId(data.gameId);
        setMode("WAITING");
        setLoading(false);
    };

    const joinGame = async () => {
        if (!gameIdInput) return;

        setActiveGameId(gameIdInput);
        setErrorMessage("");
        setLoading(true);
        setMode("JOINING_CONNECTING");

        const res = await fetch(
            `${CONFIG.API_URL}/api/games/${gameIdInput}/join`, {
                method: "POST",
                credentials: "include",
            }
        );

        if (!res.ok) {
            setErrorMessage("Failed to join game");
            setMode("JOINING_INPUT");
            setLoading(false);
            return;
        }

        setActiveGameId(gameIdInput);
        setMode("WAITING");
        setLoading(false);
    };

    return {
        gameIdInput,
        setGameIdInput,
        myUsername,
        errorMessage,
        setErrorMessage,
        loading,
        createGame,
        joinGame,
    };
}
