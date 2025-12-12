"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";

export function useMain() {
    const router = useRouter();
    const [errorMessage, setErrorMessage] = useState("");

    const handleGuestLogin = async () => {
        setErrorMessage("");

        const res = await fetch("http://localhost:8080/api/players/register/guest", {
            method: "POST",
        });

        const data = await res.json();

        if (!res.ok) {
            setErrorMessage("Failed to login as guest. Please try again.");
            console.error("Login failed:", data);
            return;
        }

        const username = data.username;
        localStorage.setItem("currentUser", username);
        console.log("Guest login successful as: " + username);

        router.push("/home");
    };

    const goToSignup = () => {
        router.push("/signup");
    };

    const goToLogin = () => {
        router.push("/login");
    };

    return {
        errorMessage,
        setErrorMessage,
        handleGuestLogin,
        goToSignup,
        goToLogin
    };
}
