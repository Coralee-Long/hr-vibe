import { useCurrentDaySummary } from "@/context/CurrentDaySummaryContext";
import React, { useEffect, useState } from "react";
import ReactApexChart from "react-apexcharts";
import { heartRateDonutConfig } from "@/config/heartRateDonutConfig.ts"; // Import config

interface DonutChartState {
  series: number[];
}

export const HeartRateDonutChart: React.FC = () => {
  const { currentDayData } = useCurrentDaySummary();

  // Hardcoded Values just to create rainbox arc style
  const [state] = useState<DonutChartState>({
    series: [20, 10, 10, 20, 20, 20],
  });

  useEffect(() => {
    console.log(currentDayData);
  });

  return (
    <div className="relative w-full flex justify-center items-center">
      {/* Donut Chart */}
      <ReactApexChart options={heartRateDonutConfig} series={state.series} type="donut" />

      {/* Centered Text */}
      <div className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 text-center">
        <h5 className="text-2xl font-semibold text-black dark:text-white">
          {currentDayData?.summary?.rhrAvg} bpm
        </h5>
        <p className="text-sm text-gray-500">Avg Resting HR</p>
      </div>
    </div>
  );
};
