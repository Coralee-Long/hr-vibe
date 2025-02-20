import React from "react";
import { GenericChart } from "@/components/charts/GenericChart";
import { MultiMetricChartConfig } from "@/config/MultiMetricChartConfig";

type MultiMetricChartProps = {
  /**
   * Categories for the x-axis (e.g., dates)
   */
  categories: string[];
  /**
   * Series data for each metric. Each object should have a `name` and an array of numbers (`data`).
   */
  seriesData: { name: string; data: number[] }[];
  /**
   * Optional chart title.
   */
  title?: string;
  /**
   * Optional array of colors for the series.
   */
  colors?: string[];
  /**
   * Indicates if data is still being fetched.
   */
  loading?: boolean;
}

export const MultiMetricChart: React.FC<MultiMetricChartProps> = ({
                                                                    categories,
                                                                    seriesData,
                                                                    title = "Multi-Metric Insights",
                                                                    colors = ["#3C50E0", "#22C55E", "#EAB308", "#EF4444", "#FF6961", "#10B981"],
                                                                    loading = false,
                                                                  }) => {
  // Generate chart options using the config file.
  const options = MultiMetricChartConfig(title, categories, colors, seriesData);

  return (
    <div className="chart-wrapper w-full p-0 m-0 min-w-[320px]">
      <GenericChart
        options={options}
        series={seriesData}
        type="line"
        height={500}
        width="100%"
        loading={loading}
      />
    </div>
  );
};
