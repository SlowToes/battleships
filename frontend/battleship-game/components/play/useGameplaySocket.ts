"use client";

import { useEffect, useRef } from "react";
import { Client } from "@stomp/stompjs";
import { FireResult } from "@/lib/types";
import { CONFIG } from "@/lib/config";

export function useGameplaySocket(
    gameId: string,
    handlers: { onFireResult: (result: FireResult) => void; }
) {
    const handlerRef = useRef(handlers);

    useEffect(() => {
        handlerRef.current = handlers;
    }, [handlers]);

    useEffect(() => {
        const client = new Client({
            brokerURL: CONFIG.WS_URL,
            reconnectDelay: 5000,
        });

        client.onConnect = () => {
            client.subscribe(
                `/topic/game/${gameId}/update`,
                message => {
                    handlerRef.current.onFireResult(JSON.parse(message.body));
                }
            );
        };

        client.activate();
        
        return () => {
            void client.deactivate();
        };
    }, [gameId]);
}
