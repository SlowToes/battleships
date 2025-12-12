"use client";

import { useRouter } from "next/navigation";

export function useGameNavigation() {
    const router = useRouter();

    const goPlaceShips = (id: string) => router.replace(`/place-ship?gameId=${id}`);
    const logout = () => {
        localStorage.removeItem("currentUser");
        router.replace("/");
    };

    return {
        goPlaceShips,
        logout
    };
}
