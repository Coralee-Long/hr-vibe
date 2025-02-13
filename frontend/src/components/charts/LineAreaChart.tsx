import React from "react";
import ReactApexChart from "react-apexcharts";
import { ApexOptions } from "apexcharts";
import { LoaderNoBg } from "@/common/LoaderNoBg";
import { LineAreaChartProps } from "@/types/LineAreaChartProps";
import { getDefaultLineAreaChartOptions } from "@/config/ChartOptions.ts";

export const LineAreaChart: React.FC<LineAreaChartProps> = (props) => {
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
  const defaultOptions: ApexOptions = getDefaultLineAreaChartOptions(
    title || "",
    categories || [],
    colors || [],
    lineType,
    strokeWidth,
    markersSize,
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
          type="area"
          height={height}
          width={width}
        />
      )}
    </div>
  );
};

