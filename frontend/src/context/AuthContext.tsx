import {
  createContext,
  useContext,
  useEffect,
  useState,
  useCallback,
} from "react";
import { UserType } from "@/types/UserType.ts";
import axios from "axios";
import { useNavigate } from "react-router-dom";

// Define the authentication state type
type AuthContextType = {
  user: UserType | null;
  isAuthLoading: boolean;
  loginAsGuest: () => Promise<void>;
  logout: () => Promise<void>;
};

// Create the AuthContext
const AuthContext = createContext<AuthContextType | undefined>(undefined);

// Custom hook for accessing AuthContext
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};

// Helper function to format user data with fallbacks
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

// Compute backend URL from the current location (update this as needed)
const getBackendUrl = () => {
  return window.location.host === "localhost:5173"
    ? "http://localhost:8080"
    : window.location.origin;
};

// AuthProvider component to wrap around the app
export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [user, setUser] = useState<UserType | null>(null);
  const [isAuthLoading, setIsAuthLoading] = useState<boolean>(true);
  const navigate = useNavigate();
  const backendUrl = getBackendUrl();

  /**
   * Fetches authenticated user details from the backend.
   * If the admin endpoint fails (guest user), try the guest endpoint.
   */
  const checkAuth = useCallback(async () => {
    try {
      // Try the admin (OAuth2) endpoint first
      const response = await axios.get(`${backendUrl}/auth/admin`, {
        withCredentials: true,
      });
      console.log("Admin authentication successful:", response.data);
      setUser(formatUserData(response.data, "ADMIN"));
    } catch (adminError) {
      console.warn("Admin check failed, trying guest endpoint...", adminError);
      try {
        const guestResponse = await axios.get(`${backendUrl}/auth/guest`, {
          withCredentials: true,
        });
        console.log("Guest authentication successful:", guestResponse.data);
        setUser(formatUserData(guestResponse.data, "GUEST"));
      } catch (guestError) {
        console.warn("Guest check failed:", guestError);
        setUser(null);
      }
    } finally {
      setIsAuthLoading(false);
    }
  }, [backendUrl]);

  /**
   * Logs in as a guest user and sets the guest profile.
   */
  const loginAsGuest = async () => {
    try {
      const response = await axios.get(`${backendUrl}/auth/guest`, {
        withCredentials: true,
      });
      console.log("Guest login response:", response.data);
      setUser(formatUserData(response.data, "GUEST"));
    } catch (error) {
      console.error("Failed to log in as guest:", error);
    }
  };

  /**
   * Logs out the user, clears session data, and redirects to homepage.
   */
  const logout = async () => {
    try {
      await axios.post(
        `${backendUrl}/auth/logout`,
        {},
        { withCredentials: true }
      );
      console.log("Logout successful");
      setUser(null);
      navigate("/");
    } catch (error) {
      console.error("Logout failed:", error);
    }
  };

  // Check authentication on component mount
  useEffect(() => {
    checkAuth();
  }, [checkAuth]);

  return (
    <AuthContext.Provider value={{ user, isAuthLoading, loginAsGuest, logout }}>
      {children}
    </AuthContext.Provider>
  );
};
