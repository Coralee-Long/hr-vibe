import { ApexOptions } from "apexcharts";

/**
 * Generates a custom radial bar chart configuration for the Heart Rate chart.
 *
 * This configuration displays the average resting heart rate using a radial bar (gauge-style)
 * chart with a gradient that transitions through five heart rate zones.
 *
 * @param title - The chart title.
 * @param value - The resting heart rate value to display.
 * @param label - The label for the radial bar chart.
 * @returns An ApexOptions object configured for the Heart Rate radial bar chart.
 */
export const HeartRateRadialChartConfig = (
  title: string,
  value: number,
  label: string,
): ApexOptions => {
  return {
    chart: {
      type: "radialBar",
      toolbar: { show: false },
      fontFamily: "Satoshi, sans-serif",
      height: "100%",
      offsetY: 0,
    },
    title: {
      text: title,
      align: "left",
      style: { color: "#AEB7C0" },
      offsetY: 0,
    },
    series: [value],
    colors: ["#64748B"],
    plotOptions: {
      radialBar: {
        hollow: {
          size: "70%", // Lower percentage means a thicker ring.
        },
        startAngle: -125,
        endAngle: 125,
        track: {
          background: "#333",
          startAngle: -125,
          endAngle: 125,
        },
        dataLabels: {
          name: {
            show: true,
            offsetY: 20,
            fontSize: "14px",
            fontWeight: 300,
          },
          value: {
            show: true,
            offsetY: -20,
            fontSize: "26px",
            fontWeight: 400,
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
        // Cast the colorStops to any to bypass TS error about the "offset" property.
        colorStops: ([
          { offset: 0, color: "#64748B" },   // zone-0
          { offset: 50, color: "#88CFF1" },   // zone-1
          { offset: 70, color: "#8CD47E" },   // zone-2
          { offset: 80, color: "#F8D66D" },   // zone-3
          { offset: 90, color: "#FFB54C" },   // zone-4
          { offset: 100, color: "#FF6961" },  // zone-5
        ] as never),
      },
    },
    stroke: {
      lineCap: "round",
    },
    labels: [label],
  };
};

export default HeartRateRadialChartConfig;
