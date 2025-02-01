import { ApexOptions } from "apexcharts";

// ---- Trends Graph Config ---- //
export const trendsGraphConfig = (color: string): ApexOptions => ({
  legend: { show: false },
  colors: [color],
  chart: {
    fontFamily: "Satoshi, sans-serif",
    height: 200,
    type: "area",
    toolbar: { show: false },
  },
  fill: {
    gradient: {
      opacityFrom: 0.55,
      opacityTo: 0,
    },
  },
  stroke: { width: 2, curve: "smooth" },
  markers: { size: 0 },
  grid: {
    strokeDashArray: 7,
    xaxis: { lines: { show: true } },
    yaxis: { lines: { show: true } },
  },
  dataLabels: { enabled: false },
  xaxis: {
    type: "category",
    categories: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
    axisBorder: { show: false },
    axisTicks: { show: false },
  },
  yaxis: { labels: { style: { colors: "#fff" } } },
});
