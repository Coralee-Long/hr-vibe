// ColumnChart.tsx
import React from "react";
import ReactApexChart from "react-apexcharts";
import { ApexOptions } from "apexcharts";
import { LoaderNoBg } from "@/common/LoaderNoBg";

/**
 * Props for the ColumnChart component.
 */
export type ColumnChartProps = {
  /**
   * The ApexChart options object.
   * If provided, it overrides any default or extra options.
   */
  options?: ApexOptions;
  /**
   * An array of series objects.
   * Each series should have a `name` and a `data` array.
   */
  series: { name: string; data: number[] }[];
  /**
   * The height of the chart.
   */
  height?: number | string;
  /**
   * The width of the chart.
   */
  width?: number | string;
  /**
   * Whether to display a loading indicator instead of the chart.
   */
  loading?: boolean;
  /**
   * Optional additional ApexChart options to merge with the defaults.
   */
  extraOptions?: Partial<ApexOptions>;
};

/**
 * ColumnChart Component
 *
 * Renders a column (bar) chart using ReactApexChart. It handles the loading state
 * and passes all configuration options to the ApexChart instance.
 */
export const ColumnChart: React.FC<ColumnChartProps> = ({
                                                          options,
                                                          series,
                                                          height,
                                                          width = 380,
                                                          loading = false,
                                                          extraOptions = {},
                                                        }) => {
  // Compute options: if options is provided, use it; otherwise, fallback to extraOptions.
  const computedOptions: ApexOptions = options ? options : { ...extraOptions };

  return (
    <div className="column-chart-container">
      {loading ? (
        <LoaderNoBg />
      ) : (
        <ReactApexChart
          options={computedOptions}
          series={series}
          type="bar"
          height={height}
          width={width}
        />
      )}
    </div>
  );
};

export default ColumnChart;
