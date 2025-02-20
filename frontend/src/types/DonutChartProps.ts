import { ApexOptions } from "apexcharts";

export type DonutChartProps = {
  /**
   * The ApexChart options object.
   * If provided, it will override the other configuration props.
   */
  options?: ApexOptions;
  /**
   * The chart title.
   */
  title?: string;
  /**
   * The labels for the donut chart segments.
   */
  labels?: string[];
  /**
   * The default colors for the chart.
   */
  colors?: string[];
  /**
   * The data series for the donut chart.
   */
  series: number[];
  /**
   * The height of the chart.
   */
  height?: number | string;
  /**
   * The width of the chart.
   */
  width?: number | string;
  /**
   * Whether to display a loading spinner instead of the chart.
   */
  loading?: boolean;
  /**
   * Optional: Additional ApexChart options to merge with the default options.
   */
  extraOptions?: Partial<ApexOptions>;
};
