// Dashboard.tsx
import { useEffect, useState } from "react";
import { DefaultLayout } from "@/layout/DefaultLayout";
import { DashMetrics } from "@/components/cards/DashMetrics/DashMetrics";
import { useRecentDailySummaries } from "@/context/RecentDailySummariesContext";
import DateNavigator from "@/common/DateNavigator";
import GarminDataService from "@/api/services/garminDataService"; // import your service

export const Dashboard = () => {
  // Start with a reference date (e.g. an early date)
  const [referenceDate, setReferenceDate] = useState("2025-01-24");
  // Manage loading state for data fetching.
  const [isLoading, setIsLoading] = useState(true);
  // We'll store the date limit (latest available date) here.
  const [dateLimit, setDateLimit] = useState<string>("2025-01-25"); // fallback/hardcoded default

  const { fetchRecentDailySummaries, fetchCurrentDayData } =
    useRecentDailySummaries();

  // On mount, fetch the day summaries (limit to 1) to get the most recent day.
  useEffect(() => {
    GarminDataService.getAllDaySummaries(1)
      .then((data) => {
        if (data && data.length > 0) {
          // Assume the DTO has a "day" field that is a string like "2025-01-25"
          setDateLimit(data[0].day);
          // console.log("Fetched dateLimit from /garmin/days:", data[0].day);
        }
      })
      .catch((error) => console.error("Error fetching day limit:", error));
  }, []);

  // Whenever the reference date changes, fetch the corresponding data.
  useEffect(() => {
    setIsLoading(true);
    Promise.all([
      fetchRecentDailySummaries(referenceDate),
      fetchCurrentDayData(referenceDate)
    ])
      .then(() => setIsLoading(false))
      .catch((error) => {
        console.error(error);
        setIsLoading(false);
      });
  }, [referenceDate, fetchRecentDailySummaries, fetchCurrentDayData]);

  return (
    <DefaultLayout>
      {/* Display the DateNavigator, passing our fetched dateLimit */}
      <div className="mb-4">
        <DateNavigator
          currentDate={referenceDate}
          latestDate={dateLimit} // now the navigator uses our date limit from /garmin/days
          onDateChange={setReferenceDate}
        />
      </div>
      {/* Pass the loading state to DashMetrics */}
      <div className="grid grid-cols-1 gap-4 md:gap-6 2xl:gap-7.5">
        <DashMetrics loading={isLoading} />
      </div>
    </DefaultLayout>
  );
};

export default Dashboard;
