import React from "react";
import { useRecentDailySummaries } from "@/context/RecentDailySummariesContext";
import { LoaderNoBg } from "@/common/LoaderNoBg";
import ColumnChart from "@/components/charts/ColumnChart";
import { SleepColumnChartConfig } from "@/config/SleepColumnChartConfig";
import { ApexOptions } from "apexcharts";
import {
  convertAndRoundTimeToHours,
  computeNonRemSleepHours
} from "@/utils/timeUtils.ts";
import { SleepChartProps } from "@/types/SleepChartProps.ts";

export const Sleep: React.FC<SleepChartProps> = ({ loading, referenceDate: _referenceDate, categories }) => {
  /**
   * Sleep Component
   *
   * Renders a stacked column chart to display sleep data.
   * Each column represents the total average sleep duration for a day,
   * split into:
   * - Non-REM Sleep: Total Sleep Avg minus REM Sleep Avg.
   * - REM Sleep: REM Sleep Avg.
   *
   * For example, if a day has 8 hours total sleep and 2 hours REM sleep,
   * then the Non-REM segment is 6 hours and the REM segment is 2 hours.
   *
   * The y-axis displays hours (0–10) and the x-axis displays date labels.
   */
  const { summaries } = useRecentDailySummaries();
  const sleepAvg = summaries?.sleepAvg || [];
  const remSleepAvg = summaries?.remSleepAvg || [];

  // Convert "HH:MM:SS" strings to hours and round to 2 decimals using a utility function.
  const sleepAvgHours = sleepAvg.map(convertAndRoundTimeToHours);
  const remSleepAvgHours = remSleepAvg.map(convertAndRoundTimeToHours);

  // Compute Non-REM sleep hours (total sleep minus REM sleep) for each day using a helper function.
  const nonRemSleepHours = computeNonRemSleepHours(sleepAvgHours, remSleepAvgHours);

  // Prepare series data for the stacked column chart.
  const series = [
    { name: "REM Sleep", data: remSleepAvgHours },
    { name: "Non-REM Sleep", data: nonRemSleepHours }
  ];

  // Define colors for the two segments.
  const colors = ["#FFB54C", "#3C50E0"]; // Yellow for REM, Indigo for Non-REM.

  // Generate chart options using our custom configuration.
  const options: ApexOptions = SleepColumnChartConfig("Sleep Duration", categories, colors);

  if (loading) {
    return (
      <div className="flex h-full items-center justify-center">
        <LoaderNoBg />
      </div>
    );
  }

  return (
    <div className="charts-container">
      <ColumnChart options={options} series={series} height={500} loading={false} />
    </div>
  );
};
