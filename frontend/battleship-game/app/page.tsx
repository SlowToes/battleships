"use client";

import { useState } from "react";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { useRouter } from "next/navigation";
import Image from "next/image";

export default function Home() {
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
        }
        else {
            const username = data.username;
            localStorage.setItem("currentUser", username);
            console.log("Guest login successful as: " + username);
            router.push("/home");
        }
    };

    return (
        <main className="min-h-screen flex items-center justify-center bg-gray-900 p-4 text-white">
            <Card className="w-full max-w-sm shadow-xl rounded-2xl bg-gray-800 text-white">
                <CardContent className="flex flex-col gap-6 p-6 items-center">
                    <Image
                        src="https://www.cbc.ca/kids/images/battleship_thumbnail.jpg"
                        width={1000}
                        height={1000}
                        alt="Battleships"
                        className="w-64 h-40 rounded-xl shadow-lg object-cover"
                    />
                    {errorMessage && (
                        <p
                            role="alert"
                            className="text-red-400 text-center"
                        >
                            {errorMessage}
                        </p>
                    )}
                    <Button
                        className="rounded-2xl py-4 text-lg w-full"
                        variant="default"
                        onClick={handleGuestLogin}
                    >
                        Login as Guest
                    </Button>
                    <Button
                        className="rounded-2xl py-4 text-lg w-full"
                        variant="secondary"
                        onClick={() => router.push("/signup")}
                    >
                        Sign Up
                    </Button>
                    <Button
                        className="rounded-2xl py-4 text-lg w-full"
                        variant="secondary"
                        onClick={() => router.push("/login")}
                    >
                        Login
                    </Button>
                </CardContent>
            </Card>
        </main>
    );
}
