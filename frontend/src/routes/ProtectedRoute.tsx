import { useState, useEffect } from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '@/context/AuthContext';

export const ProtectedRoute = () => {
  const { user, isAuthLoading, checkAuth } = useAuth();
  const [delayPassed, setDelayPassed] = useState(false);

  // If authentication finished and no user is detected, wait a bit before redirecting.
  useEffect(() => {
    if (!isAuthLoading && !user) {
      const timer = setTimeout(() => {
        setDelayPassed(true);
        // Optionally re-run checkAuth() here.
        checkAuth();
      }, 2000); // 2-second delay; adjust as needed
      return () => clearTimeout(timer);
    }
  }, [isAuthLoading, user, checkAuth]);

  if (isAuthLoading || (!user && !delayPassed)) {
    return <div>Loading...</div>; // You can replace this with your Loader component
  }
  if (!user) {
    return <Navigate to="/login" replace />;
  }
  return <Outlet />;
};
