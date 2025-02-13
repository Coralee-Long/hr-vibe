import React, { useEffect, useState } from "react";
import { DonutChart } from "@/components/charts/DonutChart";
import { useRecentDailySummaries } from "@/context/RecentDailySummariesContext";
import { getDefaultDonutChartOptions } from "@/config/ChartOptions";

export const HeartRateDonutChart: React.FC<{ height?: number }> = ({ height = 200 }) => {
  const { currentDayData } = useRecentDailySummaries();

  // Example series data
  const [series] = useState<number[]>([20, 10, 10, 20, 20, 20]);

  useEffect(() => {
    // console.log("Current Day Data:", currentDayData);
  }, []);

  const labels = ["Zone 3", "Zone 4", "Zone 5", "EMPTY", "Zone 1", "Zone 2"];
  const colors = ["#F8D66D", "#FFB54C", "#FF6961", "transparent", "#88CFF1", "#8CD47E"];

  // ✅ Use getDefaultDonutChartOptions to create chart options
  const options = getDefaultDonutChartOptions("Heart Rate Zones", labels, colors);

  return (
    <div className="relative w-full flex justify-center items-center">
      <DonutChart
        options={options}
        series={series}
        height={height}
      />
      <div className="absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 text-center">
        <h5 className="text-2xl font-semibold text-black dark:text-white">
          {currentDayData?.summary?.rhrAvg || "--"} bpm
        </h5>
        <p className="text-sm text-gray-500">Avg Resting HR</p>
      </div>
    </div>
  );
};
