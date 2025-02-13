import { ApexOptions } from "apexcharts";

/**
 * Generates a custom radial bar chart configuration for the Heart Rate chart.
 *
 * This configuration displays the average resting heart rate using a radial bar (gauge-style)
 * chart with a gradient that transitions through five heart rate zones.
 *
 * @param value - The resting heart rate value to display.
 * @param label - The label for the radial bar chart.
 * @returns An ApexOptions object configured for the Heart Rate radial bar chart.
 */
export const HeartRateRadialChartConfig = (value: number, label: string): ApexOptions => ({
  chart: {
    type: "radialBar",
    toolbar: { show: false },
    fontFamily: "Satoshi, sans-serif",
    height: "100%",
  },
  series: [value],
  // Start with zone-1 color; the gradient in fill will transition through the zones.
  colors: ["#88CFF1"],
  plotOptions: {
    radialBar: {
      hollow: {
        size: "60%", // Lower percentage means a thicker ring.
      },
      startAngle: -135,
      endAngle: 135,
      track: {
        background: "#333",
        startAngle: -135,
        endAngle: 135,
      },
      dataLabels: {
        name: {
          show: false,
        },
        value: {
          fontSize: "30px",
          show: true,
          offsetY: 5,
          formatter: () => `${value} bpm`,
        },
      },
    },
  },
  fill: {
    type: "gradient",
    gradient: {
      shade: "dark",
      type: "horizontal",
      stops: [0, 100],
      // Create a gradient that goes through the five heart rate zones.
      colorStops: [
        { offset: 0, color: "#88CFF1" },   // zone-1
        { offset: 25, color: "#8CD47E" },  // zone-2
        { offset: 50, color: "#F8D66D" },  // zone-3
        { offset: 75, color: "#FFB54C" },  // zone-4
        { offset: 100, color: "#FF6961" }, // zone-5
      ],
    },
  },
  stroke: {
    lineCap: "butt",
  },
  labels: [label],
});

export default HeartRateRadialChartConfig;
