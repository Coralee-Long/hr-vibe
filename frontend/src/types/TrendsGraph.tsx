import { ApexOptions } from "apexcharts";
import React from "react";
import ReactApexChart from "react-apexcharts";

interface TrendsGraphProps {
  title: string;
  label: string;
  data: number[];
  color?: string; // Allow customization of the graph color
}

export const TrendsGraph: React.FC<TrendsGraphProps> = ({
                                                          title,
                                                          label,
                                                          data,
                                                          color = "#13C296", // Default green color
                                                        }) => {
  const series = [
    {
      name: label,
      data,
    },
  ];

  const options: ApexOptions = {
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
    yaxis: { labels: { style: { colors: "#fff" } } }, // Adjust for dark mode
  };

  return (
    <div className="col-span-12 rounded-lg  px-5 pt-7 pb-5 sm:px-7 xl:col-span-8">
      <div className="mb-6">
        <h4 className="text-title-sm2 font-bold text-black dark:text-white">{title}</h4>
      </div>
      <div id="chartTrends" className="-ml-5">
        <ReactApexChart options={options} series={series} type="area" height={200} />
      </div>
    </div>
  );
};
