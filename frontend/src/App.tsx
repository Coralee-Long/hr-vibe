import { useEffect, useState } from "react";
import { Route, Routes, useLocation } from "react-router-dom";
import "./App.css";

import { Loader } from "@/common/Loader.tsx";
import { Dashboard } from "@/pages/Dashboard/Dashboard.tsx";
import { Insights } from "@/pages/Insights/Insights.tsx";
import { Login } from "@/pages/Login/Login.tsx";
import { PageTitle } from "@/common/PageTitle.tsx";

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
        <PageTitle title="Login | HRVibe" />
        <Route path="/login" element={ <Login /> } />

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
          path="/insights"
          element={
            <>
              <PageTitle title="Insights | HRVibe" />
              <Insights />
            </>
          }
        />
      </Routes>
    </>
  );
};
