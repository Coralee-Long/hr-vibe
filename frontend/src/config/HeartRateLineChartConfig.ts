import { ApexOptions } from "apexcharts";

/**
 * Generates default options for a Heart Rate line chart.
 *
 * @param title - The chart title.
 * @param categories - The x-axis categories (e.g., last 7 dates).
 * @param colors - An array of colors for the series.
 * @param lineType - The style of the line ("straight", "smooth", or "stepline").
 * @param strokeWidth - The stroke width of the line.
 * @param markersSize - The size of the markers.
 * @returns An ApexOptions object configured for a Heart Rate line chart.
 */
export const HeartRateLineChartConfig = (
  title: string,
  categories: string[],
  colors: string[],
  lineType: "smooth" | "straight" | "stepline" = "straight",
  strokeWidth: number = 3,
  markersSize: number = 0
): ApexOptions => ({
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
  colors,
  title: {
    text: title,
    align: "left",
    style: { color: "#AEB7C0" },
  },
  xaxis: {
    type: "category",
    categories,
    axisBorder: { show: false },
    axisTicks: { show: false },
  },
});
