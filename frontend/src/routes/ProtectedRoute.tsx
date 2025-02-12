
import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "@/context/AuthContext";

export const ProtectedRoute = () => {
  const { user } = useAuth(); // Ensure that your AuthContext exposes a "user" property.

  // If user is not authenticated, redirect to login.
  return user ? <Outlet /> : <Navigate to="/login" replace />;
};
