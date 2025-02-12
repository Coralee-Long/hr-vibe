import { useEffect, useState } from "react";
import { DefaultLayout } from "@/layout/DefaultLayout";
import { DashMetrics } from "@/components/metrics/DashMetrics/DashMetrics";
import { useRecentDailySummaries } from "@/context/RecentDailySummariesContext";
import DateNavigator from "@/common/DateNavigator";
import GarminDataService from "@/api/services/garminDataService";

/**
 * Dashboard Component
 *
 * Responsibilities:
 * 1. Manage the selected reference date (for which data is displayed).
 * 2. Fetch the latest available date (dateLimit) on mount.
 * 3. When the reference date changes, fetch both recent daily summaries and current day data.
 * 4. Pass the overall loading state and fetched summaries to DashMetrics.
 *
 * Note: Although summaries are passed to DashMetrics, individual metric components (like HeartRate)
 * can retrieve the specific data they need directly from the context via the useRecentDailySummaries hook.
 */
export const Dashboard = () => {
  // The currently selected date for which data is displayed.
  const [referenceDate, setReferenceDate] = useState("2025-01-24");

  // Indicates whether data is currently being fetched.
  const [isLoading, setIsLoading] = useState(true);

  // The latest available date, fetched on mount, used by the DateNavigator.
  const [dateLimit, setDateLimit] = useState<string>("2025-01-25");

  // Extract fetch functions and the summaries data from our context.
  // Note: summaries may be of type RecentDailySummariesDTO or null.
  const { fetchRecentDailySummaries, fetchCurrentDayData, summaries } =
    useRecentDailySummaries();

  // On mount, fetch day summaries (limit to 1) to determine the latest available date.
  useEffect(() => {
    GarminDataService.getAllDaySummaries(1)
      .then((data) => {
        if (data && data.length > 0) {
          // Assume the DTO has a "day" field (e.g., "2025-01-25").
          setDateLimit(data[0].day);
          console.log("Fetched dateLimit from /garmin/days:", data[0].day);
        }
      })
      .catch((error) => console.error("Error fetching day limit:", error));
  }, []);

  // Whenever the reference date changes, fetch the corresponding data.
  useEffect(() => {
    console.log("Reference date changed:", referenceDate);
    // Set loading state to true while new data is fetched.
    setIsLoading(true);

    // Use Promise.all to fetch both recent daily summaries and current day data concurrently.
    Promise.all([
      // Fetch recent daily summaries for the selected reference date.
      fetchRecentDailySummaries(referenceDate).then((data) => {
        console.log("Fetched recent summaries for date", referenceDate, data);
        return data;
      }),
      // Fetch current day data for the selected reference date.
      fetchCurrentDayData(referenceDate)
    ])
      .then(() => {
        // Once both fetches complete, set loading to false.
        setIsLoading(false);
        console.log("Updated summaries from context:", summaries);
      })
      .catch((error) => {
        console.error("Error fetching data for date", referenceDate, error);
        setIsLoading(false);
      });
  }, [referenceDate, fetchRecentDailySummaries, fetchCurrentDayData]);

  return (
    <DefaultLayout>
      {/* DateNavigator allows users to change the reference date.
          When changed, setReferenceDate updates the state, triggering a data fetch. */}
      <div className="mb-4">
        <DateNavigator
          currentDate={referenceDate}
          latestDate={dateLimit} // Uses the fetched dateLimit from /garmin/days.
          onDateChange={setReferenceDate}
        />
      </div>

      {/* DashMetrics renders the metric cards.
          It is passed the loading state and the fetched summaries.
          Even though summaries are passed as a prop, individual metric components,
          such as HeartRate, use context to extract their specific data.
      */}
      <div className="grid grid-cols-1 gap-4 md:gap-6 2xl:gap-7.5">
        <DashMetrics loading={isLoading} summaries={summaries} />
      </div>
    </DefaultLayout>
  );
};

export default Dashboard;
