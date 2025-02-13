// HeartRate.tsx
import { HeartRateDonutChart } from "@/components/charts/HeartRateDonutChart";
import { LineChart } from "@/components/charts/LineChart";
import { LoaderNoBg } from "@/common/LoaderNoBg";
import { useRecentDailySummaries } from "@/context/RecentDailySummariesContext";
import { HeartRateLineChartConfig } from "@/config/HeartRateLineChartConfig";

/**
 * Props for the HeartRate component.
 */
export type HeartRateProps = {
  /**
   * Indicates if data is still being fetched.
   */
  loading: boolean;
  /**
   * The reference date (format: "YYYY-MM-DD") for which the metrics are displayed.
   */
  referenceDate: string;
  /**
   * The pre-calculated x-axis categories (last 7 days) passed from the parent.
   */
  categories: string[];
};

/**
 * HeartRate Component
 *
 * This component displays heart rate metrics.
 * It retrieves the resting heart rate data (rhrAvg) from context,
 * generates chart options using the HeartRateLineChartConfig,
 * and renders a donut chart along with a line chart.
 */
export const HeartRate: React.FC<HeartRateProps> = ({ loading, referenceDate: _referenceDate, categories }) => {
  const { summaries } = useRecentDailySummaries();
  const restingHrData = summaries?.rhrAvg || [];

  if (loading) {
    return (
      <div className=" flex h-full items-center justify-center">
        <LoaderNoBg />
      </div>
    );
  }

  // Build the series data for the line chart.
  const series = [{ name: "Avg RHR", data: restingHrData }];

  // Generate chart options using the external HeartRateLineChartConfig.
  const options = HeartRateLineChartConfig("Resting Heart Rate", categories, ["#FF6961"]);

  return (
    <div className="charts-container">
      <HeartRateDonutChart height={250} />
      <LineChart options={options} series={series} height={250} loading={false} />
    </div>
  );
};

export default HeartRate;
