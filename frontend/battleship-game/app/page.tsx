"use client";

import { useRouter } from "next/navigation";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { useAuth } from "@/components/auth/useAuth";
import Image from "next/image";
import { useState } from "react";

export default function MainPage() {
    const router = useRouter();
    const { loginAsGuest, errorMessage, loading } = useAuth();
    const [showHelp, setShowHelp] = useState(false);

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
                    <Button className="rounded-2xl py-4 text-lg w-full bg-gray-600 hover:bg-gray-700 hover:cursor-pointer text-white" variant="default" onClick={loginAsGuest} disabled={loading}>
                        Login as Guest
                    </Button>
                    {errorMessage && <p role="alert" className="text-red-400 text-center">{errorMessage}</p>}
                    <Button className="rounded-2xl py-4 text-lg w-full hover:cursor-pointer" variant="secondary" onClick={() => router.push("/signup")}>
                        Sign Up
                    </Button>
                    <Button className="rounded-2xl py-4 text-lg w-full hover:cursor-pointer" variant="secondary" onClick={() => router.push("/login")}>
                        Login
                    </Button>
                    <div className="relative flex items-center gap-2 text-sm text-gray-400">
                        <span>Login not working?</span>
                        <div
                            className="relative"
                            onMouseEnter={() => setShowHelp(true)}
                            onMouseLeave={() => setShowHelp(false)}
                        >
                            <span className="flex items-center justify-center w-5 h-5 rounded-full bg-gray-600 text-white text-xs font-bold cursor-help select-none">
                                ?
                            </span>
                            {showHelp && (
                                <div className="absolute bottom-7 left-1/2 -translate-x-1/2 w-72 bg-gray-700 text-gray-100 text-sm rounded-xl shadow-xl p-4 z-10 leading-relaxed">
                                    <p className="font-semibold mb-2">The backend may be sleeping 💤</p>
                                    <p className="mb-3 text-gray-300">
                                        This app is hosted on Render&apos;s free tier, which spins down after inactivity.
                                        To wake it up:
                                    </p>
                                    <ol className="list-decimal list-inside space-y-1 text-gray-300 mb-3">
                                        <li>
                                            Open the{" "}
                                            <a
                                                href="https://github.com/SlowToes/battleships?tab=readme-ov-file#deployment"
                                                target="_blank"
                                                rel="noopener noreferrer"
                                                className="text-blue-400 underline hover:text-blue-300"
                                            >
                                                deployment section
                                            </a>
                                        </li>
                                        <li>
                                            Click the{" "}
                                            <a
                                                href="https://battleships-v2y0.onrender.com"
                                                target="_blank"
                                                rel="noopener noreferrer"
                                                className="text-blue-400 underline hover:text-blue-300"
                                            >
                                                backend link
                                            </a>
                                        </li>
                                        <li>Wait ~30 seconds for it to wake up, then try again</li>
                                    </ol>
                                    <div className="absolute -bottom-2 left-1/2 -translate-x-1/2 w-3 h-3 bg-gray-700 rotate-45" />
                                </div>
                            )}
                        </div>
                    </div>
                </CardContent>
            </Card>
        </main>
    );
}