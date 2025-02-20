import { ApexOptions } from "apexcharts";

/**
 * Generates default options for a line chart.
 *
 * This function provides a basic configuration for a line chart
 * and can be used by various metrics that rely on a line chart.
 *
 * @param title - The chart title.
 * @param categories - The x-axis categories.
 * @param colors - An array of colors for the series.
 * @param lineType - The style of the line ("straight", "smooth", or "stepline").
 * @param strokeWidth - The stroke width of the lines.
 * @param markersSize - The size of the markers.
 * @returns A generic ApexOptions object for a line chart.
 */
export const getDefaultLineChartOptions = (
  title: string,
  categories: string[],
  colors: string[],
  lineType: "smooth" | "straight" | "stepline" = "straight",
  strokeWidth: number = 2,
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

/**
 * Generates default options for a donut chart.
 *
 * @param title - The chart title.
 * @param labels - The labels for each segment.
 * @param colors - The color scheme.
 * @returns A generic ApexOptions object for a donut chart.
 */
export const getDefaultDonutChartOptions = (
  title: string,
  labels: string[],
  colors: string[]
): ApexOptions => ({
  chart: {
    fontFamily: "Satoshi, sans-serif",
    type: "donut",
    toolbar: { show: false },
  },
  colors,
  labels,
  legend: { show: false, position: "bottom" },
  plotOptions: {
    pie: {
      donut: {
        size: "70%",
        background: "transparent",
        labels: {
          show: true,
          name: {
            show: true,
            fontSize: "22px",
            fontFamily: "Satoshi, sans-serif",
            fontWeight: 600,
          },
        },
      },
    },
  },
  dataLabels: { enabled: false },
  title: {
    text: title,
    align: "left",
    style: { color: "#AEB7C0" },
  },
});
