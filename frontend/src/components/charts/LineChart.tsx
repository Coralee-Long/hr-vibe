// LineChart.tsx
import React from "react";
import ReactApexChart from "react-apexcharts";
import { ApexOptions } from "apexcharts";
import { LoaderNoBg } from "@/common/LoaderNoBg"; // Import your LoaderNoBg component

export interface LineChartProps {
  /**
   * The title displayed above the chart.
   */
  title: string;
  /**
   * The label for the data series (used in the legend and tooltips).
   */
  label: string;
  /**
   * The array of data points to be plotted.
   */
  data: number[];
  /**
   * The color of the line (any valid CSS color string).
   * Defaults to "#13C296".
   */
  color?: string;
  /**
   * The style of line connection.
   * Allowed values:
   * - "smooth": smooth curved lines,
   * - "straight": direct linear connections,
   * - "stepline": step-like transitions.
   * Defaults to "straight".
   */
  lineType?: "smooth" | "straight" | "stepline";
  /**
   * The stroke (line) width in pixels.
   * Defaults to 2.
   */
  strokeWidth?: number;
  /**
   * The size of the markers on the line.
   * Setting this to 0 hides the markers.
   * Defaults to 0.
   */
  markersSize?: number;
  /**
   * The height of the chart in pixels.
   * Defaults to 200.
   */
  height?: number;
  /**
   * Whether to display a loading spinner instead of the chart.
   * Defaults to false.
   */
  loading?: boolean;
}

export const LineChart: React.FC<LineChartProps> = ({
                                                      title,
                                                      label,
                                                      data,
                                                      color = "#13C296",
                                                      lineType = "straight",
                                                      strokeWidth = 2,
                                                      markersSize = 0,
                                                      height = 200,
                                                      loading = false,
                                                    }) => {
  const series = [{ name: label, data }];
  const options: ApexOptions = {
    chart: {
      type: "line",
      zoom: { enabled: false },
      toolbar: { show: false },
    },
    stroke: {
      curve: lineType,
      width: strokeWidth,
    },
    markers: { size: markersSize },
    colors: [color],
    title: { text: title, align: "left" },
    xaxis: { type: "category" },
  };

  return (
    <div className="line-chart-container">
      {loading ? <LoaderNoBg /> : <ReactApexChart options={options} series={series} type="line" height={height} />}
    </div>
  );
};

export default LineChart;
