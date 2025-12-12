"use client";

import { useLogin } from "@/lib/hooks/auth/useLogin";
import { useBack } from "@/lib/hooks/util/useBack";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Input } from "@/components/ui/input";

export default function LoginPage() {
    const {
        username,
        password,
        errorMessage,
        setUsername,
        setPassword,
        handleLogin,
    } = useLogin();

    const { goBack } = useBack();

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
                    {errorMessage && <p role="alert" className="text-red-400 text-center">{errorMessage}</p>}
                    <Button
                        className="rounded-2xl py-5 text-lg w-full"
                        onClick={handleLogin}
                        disabled={!username || !password}
                    >
                        Login
                    </Button>
                    <Button
                        className="text-gray-400 underline hover:text-gray-300 text-shadow-md"
                        variant="link"
                        onClick={goBack}
                    >
                        Back
                    </Button>
                </CardContent>
            </Card>
        </main>
    );
}
