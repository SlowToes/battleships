"use client";

import type { HomeMode } from "@/lib/types/HomeMode";
import React, { useState } from "react";

export function useGameCreate(
    username: string | null,
    setActiveGameId: React.Dispatch<React.SetStateAction<string | null>>,
    setMode: React.Dispatch<React.SetStateAction<HomeMode>>)
{
    const [errorMessage, setErrorMessage] = useState("");

    const createGame = async () => {
        setErrorMessage("");
        setMode("CREATING");

        const res = await fetch("http://localhost:8080/api/games/create", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username }),
        });

        const data = await res.json();

        if (!res.ok) {
            setErrorMessage("Player not found.");
            setMode("IDLE");
        }
        else {
            setActiveGameId(data.gameId);
            setMode("WAITING");
        }
    };

    return {
        createGame,
        errorMessage,
        setErrorMessage
    };
}
