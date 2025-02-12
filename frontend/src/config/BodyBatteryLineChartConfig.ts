import { ApexOptions } from "apexcharts";

/**
 * Generates default options for a Body Battery line chart.
 *
 * This configuration is tailored for charts displaying two series:
 * one for low Body Battery values and one for high Body Battery values.
 * It accepts arrays of data for the low (bbMin) and high (bbMax) readings,
 * and uses them to create annotations on the chart.
 *
 * @param title - The chart title.
 * @param categories - The x-axis categories (e.g., last 7 dates).
 * @param colors - An array of colors for the series.
 *                 Typically, the first color is used for low values and the second for high values.
 * @param bbMin - The array of low Body Battery readings.
 * @param bbMax - The array of high Body Battery readings.
 * @param lineType - The style of the line ("straight", "smooth", or "stepline").
 * @param strokeWidth - The stroke width of the lines.
 * @param markersSize - The size of the markers.
 * @returns An ApexOptions object configured for a Body Battery line chart.
 */
export const BodyBatteryLineChartConfig = (
  title: string,
  categories: string[],
  colors: string[],
  bbMin: number[],
  bbMax: number[],
  lineType: "smooth" | "straight" | "stepline" = "straight",
  strokeWidth: number = 2,
  markersSize: number = 0
): ApexOptions => {
  // For example purposes, we compute the minimum and maximum from the passed arrays
  const minAnnotation = Math.min(...bbMin);
  const maxAnnotation = Math.max(...bbMax);

  return {
    chart: {
      type: "line",
      zoom: { enabled: false },
      toolbar: { show: false },
      width: "100%",
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
    // Annotations can be used to highlight key thresholds on the y-axis.
    annotations: {
      yaxis: [
        {
          y: minAnnotation,
          borderColor: colors[0] || "#FFB54C",
          label: {
            borderColor: colors[0] || "#FFB54C",
            style: { color: "#fff", background: colors[0] || "#FFB54C" },
            text: "Low Body Battery",
          },
        },
        {
          y: maxAnnotation,
          borderColor: colors[1] || "#FFA500",
          label: {
            borderColor: colors[1] || "#FFA500",
            style: { color: "#fff", background: colors[1] || "#FFA500" },
            text: "High Body Battery",
          },
        },
      ],
    },
  };
};
