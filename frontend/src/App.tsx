import { useEffect, useState } from "react";
import { Navigate, Route, Routes, Outlet, useLocation } from "react-router-dom";

import { Loader } from "@/common/Loader";
import { Dashboard } from "@/pages/Dashboard/Dashboard";
import { Insights } from "@/pages/Insights/Insights";
import { Login } from "@/pages/Login/Login";
import { Sleep } from "@/pages/Sleep/Sleep";
import { HRV } from "@/pages/HRV/HRV";
import { Training } from "@/pages/Training/Training";
import { Activites } from "@/pages/Activities/Activities";
import { Home } from "@/pages/Home/Home";

import { GarminDataProvider } from "@/context/GarminDataContext";
import { RecentDailySummariesProvider } from "@/context/RecentDailySummariesContext";
import { AuthProvider } from "@/context/AuthContext";
import { ProtectedRoute } from "./routes/ProtectedRoute";
import { PublicLayout } from "@/layout/PublicLayout";
import { PageWrapper } from "@/routes/PageWrapper";

export const App = () => {
  const [loading, setLoading] = useState<boolean>(true);
  const { pathname } = useLocation();

  useEffect(() => {
    window.scrollTo(0, 0);
  }, [pathname]);

  useEffect(() => {
    setTimeout(() => setLoading(false), 1000);
  }, []);

  if (loading) return <Loader />;

  return (
    <AuthProvider>
      <Routes>
        {/* Public Routes */}
        <Route
          path="/"
          element={
            <PublicLayout>
              <Home />
            </PublicLayout>
          }
        />
        <Route
          path="/login"
          element={
            <PublicLayout>
              <Login />
            </PublicLayout>
          }
        />

        {/* Protected Routes Group */}
        <Route element={<ProtectedRoute />}>
          <Route
            element={
              <RecentDailySummariesProvider>
                <GarminDataProvider>
                  <Outlet />
                </GarminDataProvider>
              </RecentDailySummariesProvider>
            }
          >
            <Route
              path="/dashboard"
              element={
                <PageWrapper title="HRVibe | Dashboard">
                  <Dashboard />
                </PageWrapper>
              }
            />
            <Route
              path="/dashboard/insights"
              element={
                <PageWrapper title="HRVibe | Insights">
                  <Insights />
                </PageWrapper>
              }
            />
            <Route
              path="/dashboard/sleep"
              element={
                <PageWrapper title="HRVibe | Sleep">
                  <Sleep />
                </PageWrapper>
              }
            />
            <Route
              path="/dashboard/activities"
              element={
                <PageWrapper title="HRVibe | Activities">
                  <Activites />
                </PageWrapper>
              }
            />
            <Route
              path="/dashboard/training"
              element={
                <PageWrapper title="HRVibe | Training">
                  <Training />
                </PageWrapper>
              }
            />
            <Route
              path="/dashboard/hrv"
              element={
                <PageWrapper title="HRVibe | HRV">
                  <HRV />
                </PageWrapper>
              }
            />
          </Route>
        </Route>

        {/* Catch-all: redirect unknown routes to the homepage */}
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </AuthProvider>
  );
};
