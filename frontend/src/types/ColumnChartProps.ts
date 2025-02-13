import { ApexOptions } from "apexcharts";

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