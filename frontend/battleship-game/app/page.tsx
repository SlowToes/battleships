"use client";

import { useMain } from "@/lib/hooks/auth/useMain"
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import Image from "next/image";

export default function MainPage() {
    const {
        errorMessage,
        handleGuestLogin,
        goToSignup,
        goToLogin,
    } = useMain();

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
                    <Button
                        className="rounded-2xl py-4 text-lg w-full bg-gray-600 hover:bg-gray-700 text-white"
                        variant="default"
                        onClick={handleGuestLogin}
                    >
                        Login as Guest
                    </Button>
                    {errorMessage && <p role="alert" className="text-red-400 text-center">{errorMessage}</p>}
                    <Button
                        className="rounded-2xl py-4 text-lg w-full"
                        variant="secondary"
                        onClick={goToSignup}
                    >
                        Sign Up
                    </Button>
                    <Button
                        className="rounded-2xl py-4 text-lg w-full"
                        variant="secondary"
                        onClick={goToLogin}
                    >
                        Login
                    </Button>
                </CardContent>
            </Card>
        </main>
    );
}
