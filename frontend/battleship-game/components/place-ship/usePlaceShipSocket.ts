"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";
import { Client } from "@stomp/stompjs";

export function usePlaceShipSocket(gameId: string) {
    const router = useRouter();

    useEffect(() => {
        const client = new Client({
            brokerURL: "ws://localhost:8080/game",
            reconnectDelay: 5000,
        });

        client.onConnect = () => {
            client.subscribe(
                `/topic/game/${gameId}/place-ships`,
                message => {
                    if (message.body === "BOTH_READY") {
                        router.push(`/game/${gameId}/play`);
                    }
                }
            );
        };

        client.activate();

        return () => {
            void client.deactivate();
        };
    }, [gameId, router]);
}
