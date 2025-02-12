import React from "react";
import ReactApexChart from "react-apexcharts";
import { ApexOptions } from "apexcharts";
import { LoaderNoBg } from "@/common/LoaderNoBg";

export interface LineChartProps {
  title: string;
  label: string;
  data: number[];
  color?: string;
  lineType?: "smooth" | "straight" | "stepline";
  strokeWidth?: number;
  markersSize?: number;
  height?: number;
  loading?: boolean;
  // New prop for the latest day from DateNavigator (e.g. "2025-01-24")
  latestDay: string;
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
                                                      latestDay,
                                                    }) => {
  // Create a Date object from the latestDay prop.
  const latestDate = new Date(latestDay);

  // Generate an array of 7 dates ending on latestDay.
  // For example, if latestDay is "2025-01-24", this produces:
  // ["18.01", "19,01", "20.01"...]
  const last7Dates = Array.from({ length: 7 }, (_, i) => {
    const date = new Date(latestDate);
    date.setDate(date.getDate() - (6 - i));
    const day = date.getDate().toString().padStart(2, "0");
    const month = (date.getMonth() + 1).toString().padStart(2, "0");
    return `${day}.${month}`;
  });

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
    title: {
      text: title,
      align: "left",
      style: { color: "#F7F9FC" },
    },
    xaxis: {
      type: "category",
      categories: last7Dates, // Set the x-axis labels here.
      axisBorder: {
        show: false, // Hides the axis border (the line at the bottom)
      },
      axisTicks: {
        show: false, // Hides the tick marks along the axis
      },
    },
  };

  return (
    <div className="line-chart-container">
      {loading ? <LoaderNoBg /> : <ReactApexChart options={options} series={series} type="line" height={height} />}
    </div>
  );
};

