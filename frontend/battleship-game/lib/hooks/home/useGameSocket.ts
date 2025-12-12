"use client";

import { useEffect } from "react";
import { Client, IMessage } from "@stomp/stompjs";
import SockJS from "sockjs-client";

interface GameStatusMessage {
    gameId: string;
    status: string;
    message: string;
}

export function useGameSocket(
    gameId: string | null,
    onGameReady: (id: string) => void
) {
    useEffect(() => {
        if (!gameId) return;

        const client = new Client({
            webSocketFactory: () => new SockJS("http://localhost:8080/gameplay"),
            reconnectDelay: 5000,
            onConnect: () => {
                client.subscribe(`/topic/game/${gameId}`, (message: IMessage) => {
                    try {
                        const payload: GameStatusMessage = JSON.parse(message.body);
                        if (payload.status === "GAME_READY") {
                            onGameReady(payload.gameId);
                        }
                    } catch (e) {
                        console.error("Error processing STOMP message:", e);
                    }
                });
            },
            onStompError: (frame) => {
                console.error('[STOMP] Broker reported error: ' + frame.headers['message']);
                console.error('[STOMP] Additional details: ' + frame.body);
            },
            onDisconnect: () => {
                console.log('[STOMP] Disconnected');
            }
        });

        client.activate();

        return () => {
            if (client.active) {
                void client.deactivate();
            }
        };
    }, [gameId, onGameReady]);
}
