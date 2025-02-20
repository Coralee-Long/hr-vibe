import { ApexOptions } from "apexcharts";

/**
 * HeartRateLineChartConfig
 *
 * Generates ApexCharts options for a HeartRate line chart.
 * This chart displays the resting heart rate over the last 7 days.
 *
 * @param title - The chart title.
 * @param categories - The x-axis categories (e.g., last 7 days).
 * @param colors - An array of colors for the data series.
 * @param lineType - The style of the line ("straight", "smooth", or "stepline"). Default is "straight".
 * @param strokeWidth - The stroke width of the line. Default is 3.
 * @param markersSize - The size of the markers. Default is 0.
 * @returns An ApexOptions object configured for the HeartRate line chart.
 */
export const HeartRateLineChartConfig = (
  title: string,
  categories: string[],
  colors: string[],
  lineType: "smooth" | "straight" | "stepline" = "straight",
  strokeWidth: number = 3,
  markersSize: number = 0
): ApexOptions => {
  return {
    chart: {
      type: "line",
      zoom: { enabled: false },
      toolbar: { show: false },
    },
    stroke: {
      curve: lineType,
      width: strokeWidth,
    },
    markers: { size: markersSize },
    colors: colors,
    title: {
      text: title,
      align: "left",
      style: { color: "#AEB7C0" },
    },
    xaxis: {
      type: "category",
      categories: categories,
      axisBorder: { show: false },
      axisTicks: { show: false },
    },
  };
};
