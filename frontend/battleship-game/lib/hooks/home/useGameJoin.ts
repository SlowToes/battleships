"use client";

import type { HomeMode } from "@/lib/types/HomeMode";
import React, { useState } from "react";

export function useGameJoin(
    username: string | null,
    setMode: React.Dispatch<React.SetStateAction<HomeMode>>,
    navigateToPlaceShip: (id: string) => void)
{
    const [gameIdInput, setGameIdInput] = useState("");
    const [errorMessage, setErrorMessage] = useState("");

    const joinGame = async () => {
        setErrorMessage("");

        const idToJoin = gameIdInput.trim();
        if (!idToJoin) {
            return setErrorMessage("Please enter a Game ID.");
        }

        setMode("JOINING_CONNECTING");

        const res = await fetch(`http://localhost:8080/api/games/${idToJoin}/join`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username }),
        });

        const data = await res.json();

        if (!res.ok) {
            const err = data?.message || "Unable to join the game";
            setErrorMessage(err + ".");
            setMode("JOINING_INPUT");
            return;
        }

        navigateToPlaceShip(idToJoin);
    };

    return {
        gameIdInput,
        setGameIdInput,
        joinGame,
        errorMessage,
        setErrorMessage
    };
}
