/**
 * SyncedCharts Component
 *
 * This component renders two synchronized line charts:
 *  - REM Sleep (in minutes)
 *  - Stress Average (using resting heart rate as a placeholder)
 *
 * The charts share a common x-axis (datetime) for dates from 2025-01-01 to 2025-02-17.
 * Synchronization is achieved by assigning the same group name to both charts.
 *
 * @param {SyncedChartsProps} props - The component props.
 * @param {CombinedData[]} props.data - Array of health metrics with lifestyle booleans.
 */

import React from "react";
import ReactApexChart from "react-apexcharts";
import { ApexOptions } from "apexcharts";
import { convertTimeToMinutes } from "@/utils/timeUtils";
import { CombinedData } from "@/types/HealthData";

interface SyncedChartsProps {
  data: CombinedData[];
}

// Explicitly cast the x-axis configuration to satisfy the ApexXAxis union type.
const commonXaxis: ApexOptions["xaxis"] = {
  type: "datetime" as const,
  min: new Date("2025-01-01").getTime(),
  max: new Date("2025-02-17").getTime(),
};

export const SyncedCharts: React.FC<SyncedChartsProps> = ({ data }) => {
  // Prepare REM sleep series for the first chart.
  const remSeries = {
    name: "REM Sleep (min)",
    data: data.map((d) => ({
      x: new Date(d.date).getTime(),
      y: convertTimeToMinutes(d.remSleepAvg),
    })),
  };

  // Prepare stress series for the second chart (using rhrAvg as a placeholder for stress data).
  const stressSeries = {
    name: "Stress Avg",
    data: data.map((d) => ({
      x: new Date(d.date).getTime(),
      y: d.rhrAvg,
    })),
  };

  // Define options for the REM Sleep chart.
  const remOptions: ApexOptions = {
    chart: {
      id: "rem-chart",
      group: "synced",
      type: "line" as const,
      height: 160,
    },
    xaxis: commonXaxis,
    yaxis: { title: { text: "REM Sleep (min)" }, labels: { minWidth: 40 } },
  };

  // Define options for the Stress Avg chart.
  const stressOptions: ApexOptions = {
    chart: {
      id: "stress-chart",
      group: "synced",
      type: "line" as const,
      height: 160,
    },
    xaxis: commonXaxis,
    yaxis: { title: { text: "Stress Avg" }, labels: { minWidth: 40 } },
  };

  return (
    <div id="synced-charts">
      <div id="rem-chart-container">
        <ReactApexChart options={remOptions} series={[remSeries]} type="line" height={160} />
      </div>
      <div id="stress-chart-container">
        <ReactApexChart options={stressOptions} series={[stressSeries]} type="line" height={160} />
      </div>
    </div>
  );
};
