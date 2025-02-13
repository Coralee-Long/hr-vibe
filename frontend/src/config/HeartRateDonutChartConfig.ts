import { ApexOptions } from "apexcharts";

/**
 * Generates a custom donut chart configuration for the Heart Rate chart.
 *
 * @param rhrValue - The resting heart rate value to display (e.g., "72" or "--").
 * @param labels - The labels for each donut segment.
 * @param colors - An array of colors for the donut segments.
 * @param centerLabel - The text to display as the center label.
 * @returns An ApexOptions object configured for the Heart Rate donut chart.
 */
export const HeartRateDonutChartConfig = (
  rhrValue: string | number,
  labels: string[],
  colors: string[],
  centerLabel: string = "Avg Resting HR"
): ApexOptions => ({
  chart: {
    type: "donut",
    toolbar: { show: false },
    fontFamily: "Satoshi, sans-serif",
  },
  colors,
  labels,
  legend: { show: false },
  plotOptions: {
    pie: {
      donut: {
        size: "70%",
        background: "transparent",
        labels: {
          show: true,
          total: {
            show: true,
            showAlways: true, // Ensure the center label is always visible
            label: centerLabel,
            fontSize: "12px",
            fontWeight: 400,
            formatter: () => `${rhrValue} bpm`,
          },
        },
      },
    },
  },
  // Disable global data labels so that only the donut's total label is shown
  dataLabels: { enabled: false },
  title: {
    text: "Heart Rate Zones",
    align: "left",
    style: { color: "#AEB7C0" },
  },
});
