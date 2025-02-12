// BodyBattery.tsx
import React from "react";
import { LineChart } from "@/components/charts/LineChart";
import { LoaderNoBg } from "@/common/LoaderNoBg";
import { useRecentDailySummaries } from "@/context/RecentDailySummariesContext";
import { BodyBatteryLineChartConfig } from "@/config/BodyBatteryLineChartConfig";

/**
 * Props for the BodyBattery component.
 */
export type BodyBatteryProps = {
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
 * generates chart options using BodyBatteryLineChartConfig,
 * and renders the line chart.
 */
export const BodyBattery: React.FC<BodyBatteryProps> = ({ loading, referenceDate: _referenceDate, categories }) => {
  const { summaries } = useRecentDailySummaries();
  const bbMinData = summaries?.bbMin || [];
  const bbMaxData = summaries?.bbMax || [];

  if (loading) {
    return (
      <div className="min-h-[350px] flex items-center justify-center">
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

  // Generate chart options using the external BodyBatteryLineChartConfig.
  const options = BodyBatteryLineChartConfig("Body Battery", categories, colors, bbMinData, bbMaxData);

  return (
    <div className="h-[350px]">
      <LineChart options={options} series={series} height={450} loading={false} />
    </div>
  );
};

export default BodyBattery;
