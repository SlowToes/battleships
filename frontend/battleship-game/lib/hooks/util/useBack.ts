"use client";

import { useRouter } from "next/navigation";

export function useBack(path: string = "/") {
    const router = useRouter();

    const goBack = () => {
        router.replace(path);
    };

    return { goBack };
}