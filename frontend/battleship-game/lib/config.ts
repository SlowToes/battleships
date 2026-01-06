const API_BASE = process.env.NEXT_PUBLIC_API_URL!;

export const CONFIG = {
    API_URL: API_BASE,
    WS_URL: API_BASE.replace(/^http/, "ws") + "/game",
};
