import { ApexOptions } from "apexcharts";

/**
 * Generates a configuration object for a pie chart that illustrates
 * the intensity distribution of activities.
 *
 * @param title - The title displayed on the chart.
 * @param labels - An array of labels for each segment of the pie chart.
 * @returns An ApexOptions object configured for a pie chart.
 */
export const ActivitiesPieChartConfig = (title: string, labels: string[]): ApexOptions => {
  return {
    chart: {
      type: "pie",
      toolbar: { show: false },
      fontFamily: "Satoshi, sans-serif",
    },
    title: {
      text: title,
      align: "left",
      style: { color: "#AEB7C0" },
    },
    colors: [ "#FF6961", "#FFB54C", "#8CD47E"],
    labels,
    legend: {
      position: "bottom",
    },
    dataLabels: {
      formatter: (_val: number, { seriesIndex, w }) => {
        // Retrieve the raw value (in minutes) from the series array
        const minutes = w.globals.series[seriesIndex];
        // Convert to integer (remove decimals) and display "XX min"
        return `${Math.floor(minutes)} min`;
      },
    },
    tooltip: {
      y: {
        formatter: (val) => `${Math.floor(val)} min`,
      },
    },
  };
};
