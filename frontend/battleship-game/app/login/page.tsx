"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Input } from "@/components/ui/input";

export default function LoginPage() {
    const router = useRouter();

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [errorMessage, setErrorMessage] = useState("");

    const handleLogin = async () => {
        setErrorMessage("");

        const res = await fetch("http://localhost:8080/api/players/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ username, password }),
        });

        const data = await res.json();
        if (!res.ok) {
            setErrorMessage("Failed to login. Incorrect username or password. Please try again.");
            console.error('Login failed:', data);
        }
        else {
            const username = data.username;
            localStorage.setItem("currentUser", username);
            console.log('Player login successful');
            router.push("/home");
        }
    };

    return (
        <main className="min-h-screen flex items-center justify-center bg-gray-900 p-4 text-white">
            <Card className="w-full max-w-sm rounded-2xl shadow-xl bg-gray-800 text-white">
                <CardContent className="flex flex-col gap-4 p-6">
                    <h1 className="text-2xl font-bold text-center mb-2">Login</h1>
                    <Input
                        placeholder="Username"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        className="bg-gray-700 text-white placeholder-gray-400 border-none"
                    />
                    <Input
                        placeholder="Password"
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        className="bg-gray-700 text-white placeholder-gray-400 border-none"
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
                        className="rounded-2xl py-5 text-lg w-full"
                        onClick={handleLogin}
                        disabled={!username || !password}
                    >
                        Login
                    </Button>
                    <Button
                        variant="link"
                        className="text-gray-400 underline hover:text-gray-300 text-shadow-md"
                        onClick={() => router.replace("/")}
                    >
                        Back
                    </Button>
                </CardContent>
            </Card>
        </main>
    );
}
