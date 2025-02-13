// SleepColumnChartConfig.ts
import { ApexOptions } from "apexcharts";

/**
 * SleepColumnChartConfig
 *
 * Generates ApexChart options for a stacked column chart displaying sleep data.
 * Two series are expected:
 * - Non-REM Sleep (calculated as Total Sleep Avg minus REM Sleep Avg)
 * - REM Sleep (REM Sleep Avg)
 *
 * The x-axis displays the provided date labels, and the y-axis (in hours)
 * is rounded to whole numbers.
 *
 * @param title - The chart title.
 * @param categories - The x-axis labels (e.g., formatted dates such as "18.01").
 * @param colors - Colors for the two series.
 * @returns An ApexOptions object configured for the chart.
 */
export const SleepColumnChartConfig = (
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
