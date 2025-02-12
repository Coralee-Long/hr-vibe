import { HeartRateDonutChart } from "@/components/charts/HeartRateDonutChart.tsx";
import { LineChart } from "@/components/charts/LineChart.tsx";
import { LoaderNoBg } from "@/common/LoaderNoBg";
import { useRecentDailySummaries } from "@/context/RecentDailySummariesContext.tsx";

export const HeartRate = ({ loading }: { loading: boolean }) => {
  // Get heart rate data (rhrAvg) from the context.
  const { summaries } = useRecentDailySummaries();
  const restingHrData = summaries?.rhrAvg || [];

  // If the data is still loading, show the loader.
  if (loading) {
    return (
      <div className="min-h-[650px] flex items-center justify-center">
        <LoaderNoBg />
      </div>
    );
  }

  // Once loaded, render the charts.
  return (
    <div className="h-[650px]">
      <HeartRateDonutChart />
      <LineChart
        title="Resting Heart Rate"
        label="Avg RHR"
        data={restingHrData}
        color="#FF6961"
        // Since we're handling the loader here, we can pass loading={false}
        loading={false}
      />
    </div>
  );
};
