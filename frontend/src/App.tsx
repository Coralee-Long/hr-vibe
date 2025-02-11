import { useEffect, useState } from "react";
import { Navigate, Route, Routes, useLocation } from "react-router-dom";

import { Loader } from "@/common/Loader.tsx";
import { Dashboard } from "@/pages/Dashboard/Dashboard.tsx";
import { Insights } from "@/pages/Insights/Insights.tsx";
import { Login } from "@/pages/Login/Login.tsx";
import { Sleep } from "@/pages/Sleep/Sleep.tsx";
import { HRV } from "@/pages/HRV/HRV.tsx";
import { Training } from "@/pages/Training/Training.tsx";
import { Activites } from "@/pages/Activities/Activities.tsx";
import { Home } from "@/pages/Home/Home.tsx";

import { CurrentDaySummaryProvider } from "@/context/CurrentDaySummaryContext";
import { RecentDailySummariesProvider } from "@/context/RecentDailySummariesContext.tsx";
import { AuthProvider } from "@/context/AuthContext.tsx";
import { ProtectedRoute } from "./Routes/ProtectedRoute.tsx";
import { PublicLayout } from "@/layout/PublicLayout.tsx";
import { PageWrapper } from "@/Routes/PageWrapper.tsx";

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
      <RecentDailySummariesProvider>
        <CurrentDaySummaryProvider>
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

            {/* Catch-all: redirect unknown routes to the homepage */}
            <Route path="*" element={<Navigate to="/" replace />} />
          </Routes>
        </CurrentDaySummaryProvider>
      </RecentDailySummariesProvider>
    </AuthProvider>
  );
};
