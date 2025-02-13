import React from "react";
import { LoaderNoBg } from "@/common/LoaderNoBg";
import { useRecentDailySummaries } from "@/context/RecentDailySummariesContext";
import { HeartRateRadialChartConfig } from "@/config/HeartRateRadialChartConfig";
import { HeartRateLineChartConfig } from "@/config/HeartRateLineChartConfig";
import { GenericChart } from "@/components/charts/GenericChart";

/**
 * Props for the HeartRate component.
 */
type HeartRateProps = {
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
 * This component displays heart rate metrics using two charts:
 * - A radial bar chart that illustrates the average resting heart rate using a gradient
 *   from zone-1 to zone-5.
 * - A line chart that shows the resting heart rate over the last 7 days.
 */
export const HeartRate: React.FC<HeartRateProps> = ({
                                                      loading,
                                                      referenceDate: _referenceDate,
                                                      categories,
                                                    }) => {
  const { summaries, currentDayData } = useRecentDailySummaries();
  const restingHrData = summaries?.rhrAvg || [];

  if (loading) {
    return (
      <div className="flex h-full items-center justify-center">
        <LoaderNoBg />
      </div>
    );
  }

  // Build series data for the line chart.
  const lineSeries = [{ name: "Avg RHR", data: restingHrData }];

  // Define colors for the line chart.
  const lineColors = ["#FF6961"];

  // Generate chart options for the line chart using HeartRateLineChartConfig.
  const lineOptions = HeartRateLineChartConfig("Resting Heart Rate", categories, lineColors);

  // Retrieve the resting heart rate value (or use 0 if not available).
  const rhrValue = currentDayData?.summary?.rhrAvg;
  const numericRhrValue = typeof rhrValue === "number" ? rhrValue : 0;

  // Generate chart options for the radial bar chart using the new configuration.
  const radialOptions = HeartRateRadialChartConfig(numericRhrValue, "Avg Resting HR");
  const radialSeries = [numericRhrValue];

  return (
    <div className="charts-container">
      <div className="chart-wrapper w-full pb-10 m-0 min-w-[320]">
        <GenericChart
          options={radialOptions}
          series={radialSeries}
          type="radialBar"
          height={250}
          width="100%"
          loading={loading}
        />
      </div>
      <div className="chart-wrapper w-full p-0 m-0 min-w-[320]">
        <GenericChart
          options={lineOptions}
          series={lineSeries}
          type="line"
          height={250}
          width="100%"
          loading={loading}
        />
      </div>
    </div>
  );
};

export default HeartRate;
