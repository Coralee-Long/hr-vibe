import React from "react";
import ReactApexChart from "react-apexcharts";
import { ApexOptions } from "apexcharts";
import { LoaderNoBg } from "@/common/LoaderNoBg";
import { DonutChartProps } from "@/types/DonutChartProps";
import { getDefaultDonutChartOptions } from "@/config/ChartOptions";

export const DonutChart: React.FC<DonutChartProps> = (props) => {
  const {
    options,
    title,
    series,
    labels,
    colors,
    height,
    width = 380,
    loading = false,
    extraOptions = {},
  } = props;

  // Generate default options
  const defaultOptions: ApexOptions = getDefaultDonutChartOptions(
    title || "",
    labels || [],
    colors || []
  );

  // Merge provided options with defaults
  const computedOptions: ApexOptions = options
    ? options
    : { ...defaultOptions, ...extraOptions };

  return (
    <div className="relative w-full flex justify-center items-center">
      {loading ? (
        <LoaderNoBg />
      ) : (
        <ReactApexChart
          options={computedOptions}
          series={series}
          type="donut"
          height={height}
          width={width}
        />
      )}
    </div>
  );
};
