import { ApexOptions } from "apexcharts";

/**
 * MultiMetricChartConfig
 *
 * Generates ApexCharts options for a multi-metric line chart.
 * It accepts multiple series of data (each with a name and an array of numeric values),
 * an array of categories (e.g. dates), and an array of colors for the lines.
 *
 * @param title - The chart title.
 * @param categories - The x-axis categories (e.g., dates).
 * @param colors - An array of colors for the data series.
 * @param _seriesData - Array of series objects (each with a 'name' and 'data' array).
 * @param lineType - The line style (default "smooth").
 * @param strokeWidth - The stroke width (default 3).
 * @param markersSize - The marker size (default 3).
 * @returns An ApexOptions object configured for a multi-metric line chart.
 */
export const MultiMetricChartConfig = (
  title: string,
  categories: string[],
  colors: string[],
  _seriesData: { name: string; data: number[] }[],
  lineType: "smooth" | "straight" | "stepline" = "smooth",
  strokeWidth: number = 3,
  markersSize: number = 3
): ApexOptions => {
  return {
    chart: {
      type: "line",
      zoom: { enabled: false },
      toolbar: { show: false },
      fontFamily: "Satoshi, sans-serif",
    },
    stroke: {
      curve: lineType,
      width: strokeWidth,
    },
    markers: { size: markersSize },
    colors: colors,
    title: { text: title, align: "left", style: { color: "#AEB7C0" } },
    xaxis: {
      type: "category",
      categories: categories,
      axisBorder: { show: false },
      axisTicks: { show: false },
    },
    tooltip: {
      x: { format: "dd MMM yyyy" },
    },
    legend: { position: "bottom", offsetY: 10 },
  };
};
