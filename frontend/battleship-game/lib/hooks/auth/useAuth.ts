"use client";

import { useRouter } from "next/navigation";
import { useState } from "react";

export function useAuth() {
    const router = useRouter();

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [errorMessage, setErrorMessage] = useState("");

    const authAction = async ({
                                  endpoint,
                                  onSuccessRedirect,
                                  errorText,
                              }: {
        endpoint: string;
        onSuccessRedirect: string;
        errorText: string;
    }) => {
        setErrorMessage("");

        const res = await fetch(endpoint, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, password }),
        });

        const data = await res.json();

        if (!res.ok) {
            setErrorMessage(errorText);
            return;
        }

        localStorage.setItem("currentUser", data.username);
        router.push(onSuccessRedirect);
    };

    return {
        username,
        password,
        errorMessage,
        setUsername,
        setPassword,
        authAction,
    };
}
