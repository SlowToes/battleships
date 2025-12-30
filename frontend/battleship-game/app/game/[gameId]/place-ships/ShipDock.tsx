"use client";

import { Button } from "@/components/ui/button";
import { Ship, Orientation } from "@/lib/shipTypes";

interface ShipDockProps {
    ships: Ship[];
    selectedShip: Ship | null;
    orientation: Orientation;
    onSelectShip: (ship: Ship) => void;
    onRotate: () => void;
    onReset: () => void;
}

export default function ShipDock({ships, selectedShip, orientation, onSelectShip, onRotate, onReset}: ShipDockProps) {
    return (
        <div className="space-y-4 w-56">
            <h2 className="font-bold text-lg text-center">Ships</h2>

            {ships.map(ship => (
                <Button
                    key={ship.name}
                    disabled={ship.placed}
                    onClick={() => onSelectShip(ship)}
                    className={`
                    w-full h-12
                    ${selectedShip?.name === ship.name ? "bg-blue-600" : "bg-gray-700"} hover:bg-blue-500 hover: cursor-pointer
                    ${ship.placed ? "opacity-50" : ""}
                    `}
                >
                    {ship.name} ({ship.size})
                </Button>
            ))}

            <Button onClick={onRotate} className="w-full h-12 bg-yellow-500 text-black font-semibold hover:bg-yellow-600 hover: cursor-pointer">
                Rotate ({orientation})
            </Button>

            <Button onClick={onReset} className="w-full h-12 bg-red-600 font-semibold hover:bg-red-700 hover: cursor-pointer">
                Reset
            </Button>
        </div>
    );
}
