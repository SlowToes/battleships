"use client";

import { useState } from "react";
import { calculateCoordinates, isOutOfBounds, overlapsExisting } from "@/components/place-ship/usePlacementUtil";
import { SHIPS, Ship, Orientation, Coordinate } from "@/lib/shipTypes";

const API_URL = "http://localhost:8080";

export function useShipPlacement(gameId: string) {
    const [ships, setShips] = useState<Ship[]>(SHIPS);
    const [selectedShip, setSelectedShip] = useState<Ship | null>(null);
    const [orientation, setOrientation] = useState<Orientation>("HORIZONTAL");
    const [hoverCoords, setHoverCoords] = useState<Coordinate[]>([]);
    const [occupied, setOccupied] = useState<Set<string>>(new Set());
    const [errorMessage, setErrorMessage] = useState("");
    const [isReady, setIsReady] = useState(false)

    function handleHover(row: number, col: number) {
        if (!selectedShip) return;

        setHoverCoords(
            calculateCoordinates(
                { row, col },
                selectedShip.size,
                orientation
            )
        );
    }

    function clearHover() {
        setHoverCoords([]);
    }

    async function handlePlace(row: number, col: number) {
        if (!selectedShip || isReady) return;
        setErrorMessage("");

        const coords = calculateCoordinates(
            { row, col },
            selectedShip.size,
            orientation
        );

        if (isOutOfBounds(coords)) {
            setErrorMessage("Ship is out of bounds");
            return;
        }

        if (overlapsExisting(coords, occupied)) {
            setErrorMessage("Ship is overlapping another ship")
            return;
        }

        const res = await fetch(`${API_URL}/api/games/${gameId}/ships/place`, {
            method: "POST",
            credentials: "include",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                shipName: selectedShip.name,
                shipOrientation: orientation,
                row,
                col,
            }),
        });

        if (!res.ok) {
            setErrorMessage("Invalid ship placement")
            return;
        }

        const nextOccupied = new Set(occupied);
        coords.forEach(c => nextOccupied.add(`${c.row},${c.col}`));

        setOccupied(nextOccupied);
        setShips(prev =>
            prev.map(s =>
                s.name === selectedShip.name ? { ...s, placed: true } : s
            )
        );

        setSelectedShip(null);
        setHoverCoords([]);
    }

    async function handleReset() {
        if (isReady) return;

        const res = await fetch(`${API_URL}/api/games/${gameId}/ships/reset`, {
            method: "POST",
            credentials: "include",
        });

        if (!res.ok) {
            setErrorMessage("Reset failed")
            return;
        }

        setOccupied(new Set());
        setShips(SHIPS.map(s => ({ ...s, placed: false })));
        setSelectedShip(null);
        setHoverCoords([]);
    }

    async function handleReady() {
        if (isReady) return;

        const res = await fetch(`${API_URL}/api/games/${gameId}/ships/ready`, {
            method: "POST",
            credentials: "include",
        });

        if (!res.ok) {
            setErrorMessage("You must place all ships")
            return;
        }

        setIsReady(true);
        setErrorMessage("");
        setSelectedShip(null);
        setHoverCoords([]);
    }

    function cellHighlight(row: number, col: number) {
        if (!hoverCoords.some(c => c.row === row && c.col === col)) return "NONE";
        if (isOutOfBounds(hoverCoords) || overlapsExisting(hoverCoords, occupied)) return "INVALID";
        return "VALID";
    }

    function rotate() {
        if (isReady) return;

        setOrientation(o =>
            o === "HORIZONTAL" ? "VERTICAL" : "HORIZONTAL"
        );
    }

    return {
        ships,
        selectedShip,
        orientation,
        occupied,
        errorMessage,
        isReady,
        setSelectedShip,
        rotate,
        handleHover,
        clearHover,
        handlePlace,
        handleReset,
        handleReady,
        cellHighlight
    };
}
