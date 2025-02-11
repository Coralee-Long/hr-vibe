import { ApexOptions } from "apexcharts";

// ---- Donut Chart Config ---- //
export const heartRateDonutConfig: ApexOptions = {
  chart: {
    fontFamily: "Satoshi, sans-serif",
    type: "donut",
    width: "100%",
  },
  colors: [
    "#F8D66D",
    "#FFB54C",
    "#FF6961",
    "transparent",
    "#88CFF1",
    "#8CD47E",
  ],
  labels: ["Zone 3", "Zone 4", "Zone 5", "EMPTY", "Zone 1", "Zone 2"],
  legend: { show: false, position: "bottom" },
  plotOptions: {
    pie: {
      donut: {
        size: "70%",
        background: "transparent",
      },
    },
  },
  dataLabels: { enabled: false },
  responsive: [
    { breakpoint: 2600, options: { chart: { width: 380 } } },
    { breakpoint: 640, options: { chart: { width: "90%" } } },
  ],
};
