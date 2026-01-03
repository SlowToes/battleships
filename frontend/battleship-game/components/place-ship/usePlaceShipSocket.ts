"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";
import { Client } from "@stomp/stompjs";

export function usePlaceShipSocket(gameId: string) {
    const router = useRouter();

    useEffect(() => {
        const backendUrl = process.env.NEXT_PUBLIC_API_URL!;
        const wsUrl = backendUrl.replace(/^http/, "ws") + "/game";

        const client = new Client({
            brokerURL: wsUrl,
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
