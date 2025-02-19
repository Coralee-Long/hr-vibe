import React from "react";
import { LoaderNoBg } from "@/common/LoaderNoBg";
import { useRecentDailySummaries } from "@/context/RecentDailySummariesContext";
import { SleepChartConfig } from "@/config/SleepChartConfig";
import { GenericChart } from "@/components/charts/GenericChart";
import {
  convertAndRoundTimeToHours,
  computeNonRemSleepHours,
} from "@/utils/timeUtils";

/**
 * Props for the Sleep component.
 */
type SleepChartProps = {
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
 * Sleep Component
 *
 * This component displays sleep metrics as a stacked column chart.
 * It retrieves sleep data (total sleep and REM sleep) from context,
 * converts sleep time strings to hours, computes Non-REM sleep hours,
 * generates chart options using SleepChartConfig,
 * and renders the chart using the GenericChart component.
 */
export const Sleep: React.FC<SleepChartProps> = ({
                                                   loading,
                                                   referenceDate: _referenceDate,
                                                   categories,
                                                 }) => {
  const { summaries } = useRecentDailySummaries();
  const sleepAvg = summaries?.sleepAvg || [];
  const remSleepAvg = summaries?.remSleepAvg || [];

  // Convert sleep time strings (formatted as "HH:MM:SS") to hours (rounded to 2 decimals).
  const sleepAvgHours = sleepAvg.map(convertAndRoundTimeToHours);
  const remSleepAvgHours = remSleepAvg.map(convertAndRoundTimeToHours);

  // Compute Non-REM sleep hours for each day (total sleep hours minus REM sleep hours).
  const nonRemSleepHours = computeNonRemSleepHours(sleepAvgHours, remSleepAvgHours);

  if (loading) {
    return (
      <div className="flex h-full items-center justify-center">
        <LoaderNoBg />
      </div>
    );
  }

  // Prepare series data for the stacked column chart.
  const series = [
    { name: "REM Sleep", data: remSleepAvgHours },
    { name: "Non-REM Sleep", data: nonRemSleepHours },
  ];

  // Define colors for each chart segment.
  const colors = ["#FFB54C", "#3C50E0"];

  // Generate chart options using the centralized SleepChartConfig.
  const options = SleepChartConfig("Sleep Duration", categories, colors);

  return (
    <div className="chart-wrapper w-full p-0 m-0 min-w-[320]">
      <GenericChart
        options={options}
        series={series}
        type="bar"
        height={500}
        width="100%"
        loading={loading}
      />
    </div>
  );
};
