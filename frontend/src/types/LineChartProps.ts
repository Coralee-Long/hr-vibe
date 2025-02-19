import { ApexOptions } from "apexcharts";

export type LineChartProps = {
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
   * An array of formatted x-axis labels (e.g., last 7 dates).
   */
  categories?: string[];
  /**
   * The default colors for the series.
   */
  colors?: string[];
  /**
   * The style of the line connection.
   */
  lineType?: "smooth" | "straight" | "stepline";
  /**
   * The stroke (line) width in pixels.
   */
  strokeWidth?: number;
  /**
   * The size of the markers on the line.
   */
  markersSize?: number;
  /**
   * An array of series objects.
   * Each series object should have a `name` and a `data` array.
   */
  series: { name: string; data: number[] }[];
  /**
   * The height of the chart. Can be a number (in pixels) or a string (e.g., "100%").
   */
  height?: number | string;
  /**
   * The width of the chart. Can be a number (in pixels) or a string (e.g., "100%").
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
  /**
   * The starting opacity for the area fill under the line.
   * Higher values make the gradient more visible.
   * Default: `0.55`
   */
  fillOpacityFrom?: number;
  /**
   * The ending opacity for the area fill under the line.
   * Lower values make the fill fade out.
   * Default: `0`
   */
  fillOpacityTo?: number;
};
