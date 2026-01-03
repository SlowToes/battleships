"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";
import { Client } from "@stomp/stompjs";

export function useHomeSocket(gameId: string | null) {
    const router = useRouter();

    useEffect(() => {
        const backendUrl = process.env.REACT_APP_API_URL!;
        const wsUrl = backendUrl.replace(/^http/, "ws") + "/game";

        const client = new Client({
            brokerURL: wsUrl,
            reconnectDelay: 5000,
        });

        client.onConnect = () => {
            client.subscribe(
                `/topic/game/${gameId}/ready`,
                message => {
                    if (message.body === "READY") {
                        router.push(`/game/${gameId}/place-ships`);
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
