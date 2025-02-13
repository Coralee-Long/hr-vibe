import { ApexOptions } from "apexcharts"; // ✅ Required for TypeScript

export const BodyBatteryLineChartConfig = (
  title: string,
  categories: string[],
  colors: string[],
  bbMin: number[],
  bbMax: number[],
  lineType: "smooth" | "straight" | "stepline" = "smooth",
  strokeWidth: number = 5,
  markersSize: number = 0,
): ApexOptions => {
  const minAnnotation = Math.min(...bbMin);
  const maxAnnotation = Math.max(...bbMax);

  return {
    chart: {
      type: "area", // ✅ Must be "area" to allow fill under the lines
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
    markers: { size: markersSize },
    colors, // ✅ Ensures the correct colors for the lines
    title: { text: title, align: "left", style: { color: "#AEB7C0" } },
    xaxis: { type: "category", categories, axisBorder: { show: false }, axisTicks: { show: false } },
    annotations: {
      yaxis: [
        {
          y: minAnnotation,
          borderColor: colors[0],
          label: { text: "Lowest", style: { background: colors[0], color: "white" } },
        },
        {
          y: maxAnnotation,
          borderColor: colors[1],
          label: { text: "Highest", style: { background: colors[1], color: "white" } },
        },
      ],
    },
  };
};
