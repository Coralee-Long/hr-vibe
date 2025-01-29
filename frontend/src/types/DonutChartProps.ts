import { ApexOptions } from "apexcharts";

export type DonutChartProps = {
  subHeading?: string; // Optional heading for the chart
  series: number[]; // Data for the donut sections
  options: ApexOptions; // ApexCharts configuration
  showLegend?: boolean; // Toggle legend visibility
  width?: number | string; // Chart width (default responsive)
  height?: number | string; // Chart height (default responsive)
  className?: string; // Extra styles if needed
};