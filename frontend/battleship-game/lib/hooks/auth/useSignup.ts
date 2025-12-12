import { useAuth } from "./useAuth";

export function useSignup() {
    const auth = useAuth();

    const handleSignUp = () =>
        auth.authAction({
            endpoint: "http://localhost:8080/api/players/register/player",
            onSuccessRedirect: "/login",
            errorText: "Failed to sign up. Username already exists.",
        });

    return { ...auth, handleSignUp };
}
