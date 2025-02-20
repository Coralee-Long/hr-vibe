import { ApexOptions } from "apexcharts";

/**
 * StressChartConfig
 *
 * Generates ApexCharts options for the Stress chart,
 * a radar chart displaying stress data.
 *
 * @param title - The chart title.
 * @param categories - The circular/perimeter categories (e.g., the last 7 days).
 * @param colors - A single colour to represent the stress data.
 * @returns An ApexOptions object configured for the Stress chart.
 */
export const StressChartConfig = (
  title: string,
  categories: string[],
  colors: string[]
): ApexOptions => {
  return {
    chart: {
      type: "radar",
      toolbar: { show: false },
      zoom: { enabled: false },
      fontFamily: "Satoshi, sans-serif",
    },
    plotOptions: {
      radar: {
        polygons: {
          strokeColors: "#3d4d60",
          strokeWidth: "1",
          connectorColors: "#3d4d60",
          fill: {
            colors: ["rgba(61, 77, 96, 0.2)", "#24303F"]
          },
        },
      },
    },
      legend: { position: "bottom", offsetY: 10 },
      fill: {
        opacity: 0.4,
        colors,
      },
      markers: {
        size: 3,
        hover: {
          size: 5
        }
      },
      title: {
        text: title,
        align: "left",
        style: { color: "#AEB7C0" },
      },
      xaxis: {
        categories: categories,
        labels: {
          show: true,
          style: {
            colors: ["#a8a8a8"],
            fontSize: "11px",

          }
        }
      },
    yaxis: {
      min: 0,
      max: 100,
      show: false,
    },
    dataLabels: {
      enabled: false,
      background: {
        enabled: true,
        borderRadius: 2,
      }
    },
    }
};
