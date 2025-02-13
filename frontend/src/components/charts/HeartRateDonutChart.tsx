import React, { useEffect, useState } from "react";
import { DonutChart } from "@/components/charts/DonutChart";
import { useRecentDailySummaries } from "@/context/RecentDailySummariesContext";
import { HeartRateDonutChartConfig } from "@/config/HeartRateDonutChartConfig";

export const HeartRateDonutChart: React.FC<{ height?: number; loading?: boolean }> = ({
                                                                                        height = 200,
                                                                                        loading = false,
                                                                                      }) => {
  const { currentDayData } = useRecentDailySummaries();

  // Example series data
  const [series] = useState<number[]>([20, 10, 10, 20, 20, 20]);

  useEffect(() => {
    // Optionally log or process currentDayData.
  }, [currentDayData]);

  const labels = ["Zone 3", "Zone 4", "Zone 5", "EMPTY", "Zone 1", "Zone 2"];
  const colors = ["#F8D66D", "#FFB54C", "#FF6961", "transparent", "#88CFF1", "#8CD47E"];

  // Get the resting heart rate value (or a placeholder if not available)
  const rhrValue = currentDayData?.summary?.rhrAvg || "--";

  // Generate custom chart options using our new config file.
  const options = HeartRateDonutChartConfig(rhrValue, labels, colors);

  return (
    <div className="relative w-full flex justify-center items-center">
      <DonutChart options={options} series={series} height={height} loading={loading} />
    </div>
  );
};
