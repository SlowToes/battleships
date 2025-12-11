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

            if (username) {
                localStorage.setItem("currentUser", username);
                console.log('Player login successful');
                router.push("/game");
            }
            else {
                setErrorMessage("Server returned success but no username. Please try again.");
                console.error("Login successful but missing username:", data);
            }
        }
    };

    return (
        <main className="min-h-screen flex items-center justify-center bg-gray-100 p-4">
            <Card className="w-full max-w-sm rounded-2xl shadow-xl">
                <CardContent className="flex flex-col gap-4 p-6">
                    <h1 className="text-xl font-bold text-center mb-2">Login</h1>

                    <Input placeholder="Username" value={username} onChange={(e) => setUsername(e.target.value)} />
                    <Input placeholder="Password" type="password" value={password} onChange={(e) => setPassword(e.target.value)} />

                    {errorMessage && (
                        <div className="error-message" role="alert" style={{ color: 'red', textAlign: 'center' }}>
                            {errorMessage}
                        </div>
                    )}

                    <Button className="rounded-2xl py-5 text-lg" onClick={handleLogin}>
                        Login
                    </Button>
                </CardContent>
            </Card>
        </main>
    );
}
