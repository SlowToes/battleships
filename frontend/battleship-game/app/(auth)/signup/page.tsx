"use client";

import { FormEvent } from "react";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { useAuth } from "@/components/auth/useAuth";
import { useRouter } from "next/navigation";

export default function SignupPage() {
    const router = useRouter();
    const {
        username,
        password,
        setUsername,
        setPassword,
        errorMessage,
        signup,
        loading,
    } = useAuth();

    const handleSubmit = (e: FormEvent) => {
        e.preventDefault();
        if (username && password && !loading) {
            void signup();
        }
    };

    return (
        <main className="min-h-screen flex items-center justify-center bg-gray-900 p-4 text-white">
            <Card className="w-full max-w-sm rounded-2xl shadow-xl bg-gray-800 text-white">
                <CardContent className="flex flex-col gap-4 p-6">
                    <h1 className="text-2xl font-bold text-center mb-2">Sign Up</h1>
                    <form onSubmit={handleSubmit} className="flex flex-col gap-4">
                        <Input
                            placeholder="Username"
                            type="text"
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
                        {errorMessage && <p role="alert" className="text-red-400 text-center">{errorMessage}</p>}
                        <Button className="rounded-2xl py-5 text-lg w-full hover:cursor-pointer" onClick={signup} disabled={!username || !password || loading}>
                            Create Account
                        </Button>
                        <Button className="text-gray-400 underline hover:text-gray-300 text-shadow-md hover:cursor-pointer" variant="link" onClick={() => router.back()}>
                            Back
                        </Button>
                    </form>
                </CardContent>
            </Card>
        </main>
    );
}
