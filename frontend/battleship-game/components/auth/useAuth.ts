"use client"

import { useState } from "react";
import { useRouter } from "next/navigation";

const API_URL = process.env.REACT_APP_API_URL!;

export function useAuth() {
    const router = useRouter();

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [errorMessage, setErrorMessage] = useState<string | null>(null);
    const [loading, setLoading] = useState(false);

    const resetError = () => setErrorMessage(null);

    const login = async () => {
        setLoading(true);
        resetError();

        const res = await fetch(`${API_URL}/api/players/login`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            credentials: "include",
            body: JSON.stringify({ username, password }),
        });

        if (!res.ok) {
            setErrorMessage("Invalid username or password");
            setLoading(false);
            return;
        }

        router.push("/home");
        setLoading(false);
    };

    const signup = async () => {
        setLoading(true);
        resetError();

        const res = await fetch(`${API_URL}/api/players/signup`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            credentials: "include",
            body: JSON.stringify({ username, password }),
        });

        if (!res.ok) {
            setErrorMessage("Username already exists");
            setLoading(false);
            return;
        }

        router.push("/home");
        setLoading(false);
    };

    const loginAsGuest = async () => {
        setLoading(true);
        resetError();

        const res = await fetch(`${API_URL}/api/players/guest`, {
            method: "POST",
            credentials: "include",
        });

        if (!res.ok) {
            setErrorMessage("Guest login failed");
            setLoading(false);
            return;
        }

        router.push("/home");
        setLoading(false);
    };

    const logout = async () => {
        setLoading(true);
        resetError();

        await fetch(`${API_URL}/logout`, {
            method: "POST",
        })

        router.push("/");
    }

    return {
        username,
        password,
        setUsername,
        setPassword,
        errorMessage,
        loading,
        login,
        signup,
        loginAsGuest,
        logout,
    };
}
