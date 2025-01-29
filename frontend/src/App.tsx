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
      <Routes>
        <Route
          path="/"
          element={
            <>
              <PageTitle title="Login | HRVibe" />
              <Login />
            </>
          }
        />

        <Route
          path="/dashboard"
          element={
            <>
              <PageTitle title="Dashboard | HRVibe" />
              <Dashboard />
            </>
          }
        />

        <Route
          path="/dashboard/insights"
          element={
            <>
              <PageTitle title="Insights | HRVibe" />
              <Insights />
            </>
          }
        />
        <Route
          path="/dashboard/sleep"
          element={
            <>
              <PageTitle title="Sleep & Stress | HRVibe" />
              <Sleep />
            </>
          }
        />
        <Route
          path="/dashboard/activities"
          element={
            <>
              <PageTitle title="Activites | HRVibe" />
              <Activites />
            </>
          }
        />
        <Route
          path="/dashboard/training"
          element={
            <>
              <PageTitle title="Training | HRVibe" />
              <Training />
            </>
          }
        />
        <Route
          path="/dashboard/hrv"
          element={
            <>
              <PageTitle title="HRV & Recovery | HRVibe" />
              <HRV />
            </>
          }
        />
      </Routes>
    </>
  );
};
