import React from "react";
import { LoaderNoBg } from "@/common/LoaderNoBg";
import { useRecentDailySummaries } from "@/context/RecentDailySummariesContext";
import { BodyBatteryChartConfig } from "@/config/BodyBatteryChartConfig";
import { GenericChart } from "@/components/charts/GenericChart";

/**
 * Props for the BodyBattery component.
 */
type BodyBatteryProps = {
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
 * BodyBattery Component
 *
 * This component displays body battery metrics as a line chart.
 * It retrieves low and high body battery data (bbMin and bbMax) from context,
 * generates chart options using BodyBatteryChartConfig,
 * and renders the line chart.
 */
export const BodyBattery: React.FC<BodyBatteryProps> = ({
                                                          loading,
                                                          referenceDate: _referenceDate,
                                                          categories,
                                                        }) => {
  const { summaries } = useRecentDailySummaries();
  const bbMinData = summaries?.bbMin || [];
  const bbMaxData = summaries?.bbMax || [];

  if (loading) {
    return (
      <div className="flex h-full items-center justify-center">
        <LoaderNoBg />
      </div>
    );
  }

  // Prepare series data for the line chart.
  const series = [
    { name: "Low Body Battery", data: bbMinData },
    { name: "High Body Battery", data: bbMaxData },
  ];

  // Define colors for each series.
  const colors = ["#FF6961", "#10B981"];

  // Generate chart options using the external BodyBatteryChartConfig.
  const options = BodyBatteryChartConfig("Body Battery", categories, colors, bbMinData, bbMaxData);

  return (
    <div className="chart-wrapper w-full p-0 m-0 min-w-[320]">
      <GenericChart
        options={options}
        series={series}
        type="area"
        height={500}
        width="100%"
        loading={loading}
      />
    </div>
  );
};
