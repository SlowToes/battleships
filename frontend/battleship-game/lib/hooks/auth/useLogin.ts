import { useAuth } from "./useAuth";

export function useLogin() {
    const auth = useAuth();

    const handleLogin = () =>
        auth.authAction({
            endpoint: "http://localhost:8080/api/players/login",
            onSuccessRedirect: "/home",
            errorText: "Failed to login. Incorrect username or password.",
        });

    return { ...auth, handleLogin };
}
