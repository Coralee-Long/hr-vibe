import { useEffect, useState } from "react";
import { Route, Routes, useLocation } from "react-router-dom";

import { Loader } from "@/common/Loader.tsx";
import { Dashboard } from "@/pages/Dashboard/Dashboard.tsx";
import { Insights } from "@/pages/Insights/Insights.tsx";
import { Login } from "@/pages/Login/Login.tsx";
import { PageTitle } from "@/common/PageTitle.tsx";
import { Sleep } from "@/pages/Sleep/Sleep.tsx";
import { HRV } from "@/pages/HRV/HRV.tsx";
import { Training } from "@/pages/Training/Training.tsx";
import { Activites } from "@/pages/Activities/Activities.tsx";
import { Home } from "@/pages/Home/Home.tsx";

import { CurrentDaySummaryProvider } from "@/context/CurrentDaySummaryContext";

export const App = () => {
  const [loading, setLoading] = useState<boolean>(true);
  const { pathname } = useLocation();

  useEffect(() => {
    window.scrollTo(0, 0);
  }, [pathname]);

  useEffect(() => {
    setTimeout(() => setLoading(false), 1000);
  }, []);

  return loading ? (
    <Loader />
  ) : (
    <>
      <CurrentDaySummaryProvider>
      <Routes>
        <Route
          path="/"
          element={
            <>
              <PageTitle title="HRVibe | Home" />
              <Home />
            </>
          }
        />

        <Route
          path="/login"
          element={
            <>
              <PageTitle title="HRVibe | Login" />
              <Login />
            </>
          }
        />

        <Route
          path="/dashboard"
          element={
            <>
              <PageTitle title="HRVibe | Dashboard" />
              <Dashboard />
            </>
          }
        />

        <Route
          path="/dashboard/insights"
          element={
            <>
              <PageTitle title="HRVibe | Insights" />
              <Insights />
            </>
          }
        />
        <Route
          path="/dashboard/sleep"
          element={
            <>
              <PageTitle title="HRVibe | Sleep & Stress" />
              <Sleep />
            </>
          }
        />
        <Route
          path="/dashboard/activities"
          element={
            <>
              <PageTitle title="HRVibe | Activites" />
              <Activites />
            </>
          }
        />
        <Route
          path="/dashboard/training"
          element={
            <>
              <PageTitle title="HRVibe | Training" />
              <Training />
            </>
          }
        />
        <Route
          path="/dashboard/hrv"
          element={
            <>
              <PageTitle title="HRVibe | HRV & Recovery" />
              <HRV />
            </>
          }
        />
      </Routes>
        </CurrentDaySummaryProvider>
    </>
  );
};
