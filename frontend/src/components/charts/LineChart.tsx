import React from "react";
import ReactApexChart from "react-apexcharts";
import { ApexOptions } from "apexcharts";
import { LoaderNoBg } from "@/common/LoaderNoBg";
import { LineChartProps } from "@/types/LineChartProps";
import { getDefaultLineChartOptions } from "@/config/ChartOptions";

export const LineChart: React.FC<LineChartProps> = (props) => {
  const {
    options,
    title,
    series,
    categories,
    colors,
    lineType = "straight",
    strokeWidth = 2,
    markersSize = 0,
    height,
    width = 380,
    loading = false,
    extraOptions = {},
  } = props;

  // Build default options from individual props.
  const defaultOptions: ApexOptions = getDefaultLineChartOptions(
    title || "",
    categories || [],
    colors || [],
    lineType,
    strokeWidth,
    markersSize
  );

  // If the caller provided an options object, use that.
  // Otherwise, use our default options merged with any extra options.
  const computedOptions: ApexOptions = options
    ? options
    : { ...defaultOptions, ...extraOptions };

  return (
    <div className="line-chart-container">
      {loading ? (
        <LoaderNoBg />
      ) : (
        <ReactApexChart
          options={computedOptions}
          series={series}
          type="line"
          height={height}
          width={width}
        />
      )}
    </div>
  );
};

