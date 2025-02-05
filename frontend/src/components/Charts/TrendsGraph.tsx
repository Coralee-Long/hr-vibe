import React from "react";
import ReactApexChart from "react-apexcharts";
import { trendsGraphConfig } from "@/config/trendsGraphConfig.ts";

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
  const series = [{ name: label, data }];
  const options = trendsGraphConfig(color); // Use the config function

  return (
    <div className="col-span-12 rounded-lg pt-7 pb-5 sm:px-3 xl:col-span-8">
      <div className="mb-6">
        <h4 className="text-title-sm2 font-bold text-black dark:text-white">{title}</h4>
      </div>
      <div id="chartTrends" className="-ml-5">
        <ReactApexChart options={options} series={series} type="area" height={200} />
      </div>
    </div>
  );
};
