import React from "react";
import { LoaderNoBg } from "@/common/LoaderNoBg";
import { useRecentDailySummaries } from "@/context/RecentDailySummariesContext";
import { StressChartConfig } from "@/config/StressChartConfig";
import { GenericChart } from "@/components/charts/GenericChart";

/**
 * Props for the Stress component.
 */
type StressChartProps = {
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
 * Stress Component
 *
 * This component displays stress metrics as a stacked column chart.
 * It retrieves stress data (total stress and REM stress) from context,
 * converts stress time strings to hours, computes Non-REM stress hours,
 * generates chart options using StressChartConfig,
 * and renders the chart using the GenericChart component.
 */
export const Stress: React.FC<StressChartProps> = ({
                                                   loading,
                                                   referenceDate: _referenceDate,
                                                   categories,
                                                 }) => {
  const { summaries } = useRecentDailySummaries();
  const stressAverages = summaries?.stressAvg || [];

  if (loading) {
    return (
      <div className="flex h-full items-center justify-center">
        <LoaderNoBg />
      </div>
    );
  }

  // Prepare series data for the stacked column chart.
  const series = [ { name: "Stress Levels", data: stressAverages }];

  // Define colors for each chart segment.
  const colors = ["#8CD47E"];

  // Generate chart options using the centralized StressChartConfig.
  const options = StressChartConfig("Stress Averages", categories, colors);

  return (
    <div className="chart-wrapper w-full p-0 m-0 min-w-[320]">
      <GenericChart
        options={options}
        series={series}
        type="radar"
        height={500}
        width="100%"
        loading={loading}
      />
    </div>
  );
};
