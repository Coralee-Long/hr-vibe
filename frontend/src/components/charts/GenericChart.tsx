import React from "react";
import ReactApexChart from "react-apexcharts";
import { ApexOptions } from "apexcharts";

/**
 * AllowedChartTypes defines the specific set of chart types that ReactApexChart accepts.
 * This prevents the IDE error (TS2769) by ensuring that only these literal values are used.
 */
export type AllowedChartTypes =
  | "line"
  | "area"
  | "bar"
  | "pie"
  | "donut"
  | "radialBar"
  | "scatter"
  | "bubble"
  | "heatmap"
  | "candlestick"
  | "boxPlot"
  | "radar"
  | "polarArea"
  | "rangeBar"
  | "rangeArea"
  | "treemap";

/**
 * Props for the GenericChart component.
 * This component wraps ReactApexChart and is completely driven by its props.
 */
export interface GenericChartProps {
  /**
   * A complete ApexCharts options object that defines the chart settings.
   */
  options: ApexOptions;
  /**
   * Series data for the chart.
   */
  series: any;
  /**
   * The chart type. It must be one of the allowed literal values.
   */
  type: AllowedChartTypes;
  /**
   * The chart height (e.g., 500 or "100%").
   */
  height?: number | string;
  /**
   * The chart width (e.g., 380 or "100%").
   */
  width?: number | string;
  /**
   * If true, a loading indicator will be shown instead of the chart.
   */
  loading?: boolean;
}

/**
 * GenericChart Component
 *
 * A fully configurable and reusable chart component that leverages ReactApexChart.
 * All configuration is passed in via props, so no internal values are hardcoded.
 */
export const GenericChart: React.FC<GenericChartProps> = ({
                                                            options,
                                                            series,
                                                            type,
                                                            height,
                                                            width,
                                                            loading = false,
                                                          }) => {
  return (
    <div className="generic-chart-container">
      {loading ? (
        // Replace this with your LoaderNoBg component or another loading indicator as needed.
        <div className="loading-container">Loading...</div>
      ) : (
        <ReactApexChart
          options={options}
          series={series}
          type={type}
          height={height}
          width={width}
        />
      )}
    </div>
  );
};
