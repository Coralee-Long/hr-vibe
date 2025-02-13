// Dashboard.jsx
import { useEffect, useState } from "react";
import { DefaultLayout } from "@/layout/DefaultLayout";
import { DashMetrics } from "@/components/metrics/DashMetrics/DashMetrics";
import { useRecentDailySummaries } from "@/context/RecentDailySummariesContext";
import GarminDataService from "@/api/services/garminDataService";

/**
 * Dashboard Component
 *
 * Responsibilities:
 * - Manages the selected reference date used for data display.
 * - Fetches the latest available date (dateLimit) when the component mounts.
 * - Fetches recent daily summaries and current day data whenever the reference date changes.
 * - Passes loading state, summaries, the reference date, and date navigation callbacks to the DashMetrics component.
 */
export const Dashboard = () => {
  // Holds the currently selected reference date.
  const [referenceDate, setReferenceDate] = useState("2025-01-24");

  // Indicates whether data is currently being fetched.
  const [isLoading, setIsLoading] = useState(true);

  // Stores the latest available date fetched from the API.
  const [dateLimit, setDateLimit] = useState("2025-01-25");

  // Extract fetch functions and summary data from the RecentDailySummaries context.
  const { fetchRecentDailySummaries, fetchCurrentDayData, summaries } =
    useRecentDailySummaries();

  /**
   * On mount, fetch the latest available date.
   * This date will be used to limit the maximum selectable date in the DateNavigator.
   */
  useEffect(() => {
    GarminDataService.getAllDaySummaries(1)
      .then((data) => {
        if (data && data.length > 0) {
          setDateLimit(data[0].day);
          console.log("Fetched dateLimit:", data[0].day);
        }
      })
      .catch((error) => {
        console.error("Error fetching day limit:", error);
      });
  }, []);

  /**
   * Whenever the reference date changes, fetch the corresponding data.
   * This includes both recent daily summaries and current day data.
   */
  useEffect(() => {
    // console.log("Reference date changed:", referenceDate);
    setIsLoading(true); // Set loading state while data is being fetched.

    // Fetch both data sources concurrently.
    Promise.all([
      fetchRecentDailySummaries(referenceDate).then((data) => {
        // console.log("Fetched summaries for", referenceDate, data);
        return data;
      }),
      fetchCurrentDayData(referenceDate)
    ])
      .then(() => {
        setIsLoading(false); // Data fetching complete.
        // console.log("Updated summaries:", summaries);
      })
      .catch((error) => {
        console.error("Error fetching data for", referenceDate, error);
        setIsLoading(false);
      });
  }, [referenceDate, fetchRecentDailySummaries, fetchCurrentDayData]);

  return (
    <DefaultLayout>
      {/* Pass the state and callback to the DashMetrics component */}
      <DashMetrics
        loading={isLoading}
        summaries={summaries}
        referenceDate={referenceDate}
        latestDate={dateLimit}         // Passes the latest available date for the DateNavigator.
        onDateChange={setReferenceDate}  // Callback to update the reference date.
      />
    </DefaultLayout>
  );
};
