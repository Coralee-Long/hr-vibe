import ReactApexChart from "react-apexcharts";
import { ApexOptions } from "apexcharts";

interface Metric {
  name: string;
  data: number[];
  visible: boolean;
}

interface ChartComponentProps {
  metrics: Metric[];
}

export const ChartComponent: React.FC<ChartComponentProps> = ({ metrics }) => {
  const series = metrics
    .filter((metric) => metric.visible)
    .map(({ name, data }) => ({ name, data }));

  const options: ApexOptions = {
    chart: {
      type: "area",
      height: 310,
      fontFamily: "Satoshi, sans-serif",
      background: "transparent",
      toolbar: { show: false },
    },
    stroke: { curve: "smooth" },
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
    grid: {
      show: true,
      borderColor: "rgba(255, 255, 255, 0.2)",
    },
    xaxis: {
      categories: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"],
      axisBorder: { show: false },
      axisTicks: { show: false },
      labels: { style: { colors: "inherit" } },
    },
    yaxis: {
      labels: { style: { colors: "inherit" } },
    },
    legend: {
      show: true, // Keeps the legend visible
      position: "top",
      horizontalAlign: "left",
      labels: { colors: "inherit" },
    },
    markers: {
      size: 4, // Keeps small dots, but removes number labels
      strokeWidth: 2,
      hover: { sizeOffset: 3 },
    },
    dataLabels: {
      enabled: false, // This removes the number labels inside the chart
    },
    colors: ["#3C50E0", "#22C55E", "#EAB308", "#EF4444"],
  };

  return (
    <ReactApexChart
      options={options}
      series={series}
      type="area"
      height={310}
    />
  );
};
