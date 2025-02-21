/**
 * CombinedInsightsChart Component
 *
 * This component renders a combined chart that displays REM sleep (in minutes)
 * as a primary line series (left y-axis) and overlays a scatter series for alcohol consumption,
 * represented as the number of drinks, on a secondary y-axis.
 *
 * The chart is configured to display data for dates from 2025-01-01 to 2025-02-17.
 *
 * Using a numeric measure for alcohol (number of drinks) provides more granularity for correlation analysis.
 *
 * @param {CombinedInsightsChartProps} props - The component props.
 * @param {CombinedData[]} props.data - Array of health metrics with a numeric alcohol field.
 */

import React from "react";
import ReactApexChart from "react-apexcharts";
import { ApexOptions } from "apexcharts";
import { convertTimeToMinutes } from "@/utils/timeUtils";
import { CombinedData } from "@/types/HealthData";

interface CombinedInsightsChartProps {
  data: CombinedData[];
}

export const CombinedInsightsChart: React.FC<CombinedInsightsChartProps> = ({ data }) => {
  // Prepare the REM sleep series (line) with x-axis as timestamp and y-axis as minutes.
  const remSleepSeries = {
    name: "REM Sleep (min)",
    data: data.map((d) => ({
      x: new Date(d.date).getTime(),
      y: convertTimeToMinutes(d.remSleepAvg),
    })),
  };

  // Prepare a scatter series for alcohol consumption.
  // Here, d.alcohol is assumed to be a number (e.g., 0, 1, 2, 3 drinks).
  const alcoholSeries = {
    name: "Alcohol Drinks",
    data: data.map((d) => ({
      x: new Date(d.date).getTime(),
      y: d.alcohol, // the number of drinks
    })),
  };

  // Define chart options with dual y-axes.
  const options: ApexOptions = {
    chart: {
      type: "line" as const,
      height: 350,
      zoom: { enabled: true },
    },
    xaxis: {
      type: "datetime" as const,
      // Set fixed date range.
      min: new Date("2025-01-01").getTime(),
      max: new Date("2025-02-17").getTime(),
    },
    yaxis: [
      {
        title: { text: "REM Sleep (min)" },
      },
      {
        opposite: true,
        title: { text: "Alcohol Drinks" },
        min: 0,
        // Adjust max as needed based on your data; here, we assume a maximum of 10 drinks.
        max: 10,
        tickAmount: 5,
      },
    ],
    tooltip: {
      x: { format: "dd MMM yyyy" },
    },
  };

  // Combine the two series into an array. We'll render REM sleep as a line and alcohol as a scatter series.
  const series = [
    remSleepSeries,
    {
      ...alcoholSeries,
      type: "scatter" as const,
    },
  ];

  console.log("series", series);
  return (
    <div id="combined-insights-chart">
      <ReactApexChart options={options} series={series} type="line" height={350} />
    </div>
  );
};
