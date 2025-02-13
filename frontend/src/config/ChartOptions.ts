// chartOptions.ts
import { ApexOptions } from "apexcharts";

/**
 * Generates default options for a line chart.
 *
 * @param title - The chart title.
 * @param last7Dates - The x-axis categories (e.g., formatted dates).
 * @param colors - An array of colors for the series.
 * @param lineType - The style of the line (e.g., "straight", "smooth", "stepline").
 * @param strokeWidth - The stroke width of the lines.
 * @param markersSize - The size of the markers.
 * @returns An ApexOptions object with the default settings.
 */
export const getDefaultLineChartOptions = (
  title: string,
  last7Dates: string[],
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
    categories: last7Dates,
    axisBorder: { show: false },
    axisTicks: { show: false },
  },
});

export const getDefaultLineAreaChartOptions = (
  title: string,
  last7Dates: string[],
  colors: string[],
  lineType: "smooth" | "straight" | "stepline" = "smooth",
  strokeWidth: number = 3,
  markersSize: number = 0,
): ApexOptions => ({
  chart: {
    type: "area",
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
    categories: last7Dates,
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
 * @returns An ApexOptions object with the default settings.
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
            fontSize: '22px',
            fontFamily: 'Helvetica, Arial, sans-serif',
            fontWeight: 600,
          }
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

