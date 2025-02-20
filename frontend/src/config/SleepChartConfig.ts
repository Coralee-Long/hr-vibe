import { ApexOptions } from "apexcharts";

/**
 * SleepChartConfig
 *
 * Generates ApexCharts options for the Sleep chart,
 * a stacked column chart displaying sleep data.
 * The chart splits daily sleep duration into two segments:
 * - "REM Sleep": The average REM sleep hours.
 * - "Non-REM Sleep": Total sleep hours minus REM sleep hours.
 *
 * @param title - The chart title.
 * @param categories - The x-axis categories (e.g., the last 7 days).
 * @param colors - An array of two colors: first for REM Sleep, second for Non-REM Sleep.
 * @returns An ApexOptions object configured for the Sleep chart.
 */
export const SleepChartConfig = (
  title: string,
  categories: string[],
  colors: string[]
): ApexOptions => {
  return {
    chart: {
      type: "bar",
      stacked: true,
      toolbar: { show: false },
      zoom: { enabled: false },
      fontFamily: "Satoshi, sans-serif",
    },
    plotOptions: {
      bar: {
        horizontal: false,
        borderRadius: 5,
        columnWidth: "75%",
        borderRadiusApplication: "end",
        borderRadiusWhenStacked: "last",
        dataLabels: {
          total: {
            enabled: false,
            style: {
              fontSize: "13px",
              fontWeight: "900",
            },
          },
        },
      },
    },
    responsive: [
      {
        breakpoint: 480,
        options: {
          legend: {
            position: "bottom",
            offsetX: 0,
            offsetY: 0,
          },
        },
      },
    ],
    xaxis: {
      type: "category",
      categories: categories,
      axisBorder: { show: false },
      axisTicks: { show: false },
    },
    yaxis: {
      title: { text: "Hours" },
      min: 0,
      max: 12,
      labels: {
        formatter: (value: number) => Number(value).toFixed(0),
      },
    },
    legend: { position: "bottom", offsetY: 10 },
    fill: { opacity: 1 },
    colors: colors,
    title: {
      text: title,
      align: "left",
      style: { color: "#AEB7C0" },
    },
  };
};
