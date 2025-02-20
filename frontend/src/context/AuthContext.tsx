// AuthContext.tsx
import {
  createContext,
  useContext,
  useEffect,
  useState,
  useCallback,
} from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import useLocalStorage from "@/hooks/useLocalStorage";
import { UserType } from "@/types/UserType.ts";

// Define the authentication context type.
type AuthContextType = {
  user: UserType | null;
  isAuthLoading: boolean;
  loginAsAdmin: () => void;
  loginAsGuest: () => Promise<void>;
  logout: () => Promise<void>;
  checkAuth: () => Promise<void>;
};

// Create the AuthContext.
const AuthContext = createContext<AuthContextType | undefined>(undefined);

// Custom hook to access the AuthContext.
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};

// Helper function to format raw user data from the backend.
const formatUserData = (
  data: any,
  defaultRole: "ADMIN" | "GUEST"
): UserType => ({
  username: data.username ?? "UserName",
  firstName: data.firstName ?? "First Name",
  lastName: data.lastName ?? "Last Name",
  city: data.city ?? "City",
  country: data.country ?? "Country",
  role: data.role ?? defaultRole,
});

// Compute backend URL based on the current environment.
const getBackendUrl = () => {
  return window.location.host === "localhost:5173"
    ? "http://localhost:8080"
    : window.location.origin;
};

// AuthProvider component that wraps your application.
export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  // Persist the user state using the custom useLocalStorage hook.
  const [user, setUser] = useLocalStorage<UserType | null>("user", null);
  const [isAuthLoading, setIsAuthLoading] = useState<boolean>(true);
  const navigate = useNavigate();
  const backendUrl = getBackendUrl();

  /**
   * checkAuth
   * -----------
   * Attempts to authenticate the user via the admin endpoint.
   * If a user is already stored in localStorage and has a "GUEST" role,
   * we assume the guest session is valid and skip the admin check.
   * Otherwise, we attempt to call /auth/admin.
   * If the admin endpoint returns a 401, we log a warning and clear the user state.
   */
  const checkAuth = useCallback(async () => {
    // If we already have a guest user stored, skip admin reauthentication.
    if (user && user.role === "GUEST") {
      setIsAuthLoading(false);
      return;
    }
    try {
      const response = await axios.get(`${backendUrl}/auth/admin`);
      setUser(formatUserData(response.data, "ADMIN"));
    } catch (error) {
      if (axios.isAxiosError(error) && error.response?.status === 401) {
        console.warn("No active admin session.");
      } else {
        console.error("Admin authentication failed:", error);
      }
      setUser(null);
    } finally {
      setIsAuthLoading(false);
    }
  }, [backendUrl, setUser, user]);

  /**
   * loginAsAdmin
   * -------------
   * Initiates the admin login via OAuth2 by redirecting the browser to the GitHub OAuth endpoint.
   * The backend will handle the OAuth flow and, upon successful login, create a session.
   */
  const loginAsAdmin = () => {
    // Redirect to the backend OAuth2 endpoint.
    window.location.href = `${backendUrl}/oauth2/authorization/github`;
  };

  /**
   * loginAsGuest
   * -------------
   * Explicitly logs in the user as a guest by calling the guest endpoint.
   */
  const loginAsGuest = async () => {
    try {
      const response = await axios.get(`${backendUrl}/auth/guest`);
      setUser(formatUserData(response.data, "GUEST"));
    } catch (error) {
      console.error("Failed to log in as guest:", error);
    }
  };

  /**
   * logout
   * -------
   * Logs out the user by calling the backend logout endpoint.
   * Clears the user state and navigates back to the home page.
   */
  const logout = async () => {
    try {
      await axios.post(`${backendUrl}/auth/logout`, {});
      setUser(null);
      navigate("/");
    } catch (error) {
      console.error("Logout failed:", error);
    }
  };

  // On component mount, attempt to authenticate (only for admin).
  // If a guest is already logged in (stored in localStorage), checkAuth skips the admin call.
  useEffect(() => {
    const verifyAuth = async () => {
      try {
        await checkAuth();
      } catch (error) {
        console.error("Error during authentication check:", error);
      }
    };
    void verifyAuth();
  }, [checkAuth]);

  return (
    <AuthContext.Provider
      value={{ user, isAuthLoading, loginAsAdmin, loginAsGuest, logout, checkAuth }}
    >
      {children}
    </AuthContext.Provider>
  );
};
