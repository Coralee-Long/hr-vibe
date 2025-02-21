/**
 * Data point for multi‑metric charts.
 * It can be either a simple xy pair (for line/bar charts) or a range (for rangeArea charts).
 */
export type MultiMetricDataPoint =
  | { x: string; y: number }
  | { x: string; y: [number, number] };

/**
 * Series definition for multi‑metric charts.
 * Each series has a name, a type, and an array of data points.
 */
export type MultiMetricSeries = {
  name: string;
  type: "rangeArea" | "line" | "bar" | "area" | "scatter";
  data: MultiMetricDataPoint[];
}

/**
 * Props for the MultiMetricChartOne component.
 * - seriesData: an array of series to display.
 * - categories: optional x-axis categories (used when seriesData isn’t in xy format).
 * - title: chart title.
 * - loading: flag to show a loading state.
 */
export type MultiMetricChartProps = {
  seriesData: MultiMetricSeries[];
  categories?: string[];
  title: string;
  loading: boolean;
}
