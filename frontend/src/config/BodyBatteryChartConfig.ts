import { ApexOptions } from "apexcharts";

/**
 * BodyBatteryChartConfig
 *
 * Generates ApexCharts options for the BodyBattery chart,
 * a line area chart displaying body battery metrics.
 * It displays two data series ("Low Body Battery" and "High Body Battery")
 * with a gradient fill and y-axis annotations.
 *
 * @param title - The chart title.
 * @param categories - The x-axis categories (e.g., the last 7 days).
 * @param colors - An array of colors for the data series.
 * @param bbMin - Data series for low body battery values.
 * @param bbMax - Data series for high body battery values.
 * @param lineType - The line style (default "smooth").
 * @param strokeWidth - The stroke width (default 5).
 * @param markersSize - The marker size (default 0).
 * @returns An ApexOptions object configured for the BodyBattery chart.
 */
export const BodyBatteryChartConfig = (
  title: string,
  categories: string[],
  colors: string[],
  bbMin: number[],
  bbMax: number[],
  lineType: "smooth" | "straight" | "stepline" = "smooth",
  strokeWidth: number = 5,
  markersSize: number = 0
): ApexOptions => {
  const minAnnotation = Math.min(...bbMin);
  const maxAnnotation = Math.max(...bbMax);

  return {
    chart: {
      type: "area",
      zoom: { enabled: false },
      toolbar: { show: false },
      fontFamily: "Satoshi, sans-serif",
    },
    stroke: {
      curve: lineType,
      width: strokeWidth,
    },
    fill: {
      type: "gradient",
      gradient: {
        shade: "light",
        type: "vertical",
        shadeIntensity: 0.2,
        opacityFrom: 0.5,
        opacityTo: 0.1,
        stops: [0, 100],
      },
    },
    legend: { position: "bottom", offsetY: 10 },
    markers: { size: markersSize },
    colors: colors,
    title: { text: title, align: "left", style: { color: "#AEB7C0" } },
    xaxis: {
      type: "category",
      categories: categories,
      axisBorder: { show: false },
      axisTicks: { show: false },
    },
    annotations: {
      yaxis: [
        {
          y: minAnnotation,
          borderColor: colors[0],
          label: {
            text: "Lowest",
            style: { background: colors[0], color: "white" },
          },
        },
        {
          y: maxAnnotation,
          borderColor: colors[1],
          label: {
            text: "Highest",
            style: { background: colors[1], color: "white" },
          },
        },
      ],
    },
  };
};
