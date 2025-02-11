import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "@/context/AuthContext.tsx";
import { Loader } from "@/common/Loader.tsx";

export const ProtectedRoute = () => {
  const { user, isAuthLoading } = useAuth();

  // If still checking authentication, show a loader
  if (isAuthLoading) {
    return <Loader />;
  }

  // If no user is found after authentication check, redirect to the login page
  if (!user) {
    console.warn("User not authenticated; redirecting to /login");
    return <Navigate to="/login" replace />;
  }

  // If user is authenticated, render the nested routes
  return <Outlet />;
};
