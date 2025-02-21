import React from "react";
import ReactApexChart from "react-apexcharts";
import { MultiMetricChartProps } from "@/types/MultiMetricsChartTypes.ts";

/**
 * MultiMetricChartOne renders a combined chart for Body Battery metrics.
 * It displays:
 *  - A range area for the Body Battery Range (using a gradient fill).
 *  - An overlaid line for the Body Battery Average.
 *  - Additional line series (Stress Avg and Resting HR) can be added as needed.
 *
 * The component uses ReactApexChart with custom options such as:
 *  - A smooth stroke (with the first series having no stroke).
 *  - Custom colors for each series.
 *  - A gradient fill for the first series that is inverted so that red appears at the bottom and yellow at the top.
 */
export const MultiMetricChartOne: React.FC<MultiMetricChartProps> = (props) => {
  // Determine if the series data is in xy format (each point includes its own x value).
  const hasXYData = props.seriesData.some(series =>
    series.data.length > 0 &&
    typeof series.data[0] === "object" &&
    "x" in series.data[0]
  );

  // Define the chart options.
  const options = {
    chart: {
      animations: { speed: 500 },
      fontFamily: "Satoshi, sans-serif",
    },
    title: {
      text: props.title,
      style: { color: "#AEB7C0" },
    },
    xaxis: {
      ...(hasXYData ? {} : { categories: props.categories }),
      labels: {
        show: false,
      },
      axisBorder: { show: false },
      axisTicks: { show: false },
    },
    yaxis: {
      min: 0,
      max: 100,
      labels: {
        formatter: (val: number) => Math.round(val).toString(),
      },
    },
    dataLabels: {
      enabled: false,
    },
    stroke: {
      curve: "smooth" as const,
      width: [0, 2, 2, 2],
    },
    colors: [
      "#ff9466",
      "#FFBA00",
      "#10B981",
      "#D34053",
    ],
    fill: {
      type: ["gradient", "solid", "solid", "solid"],
      opacity: [1, 1, 1, 1],
      gradient: {
        shade: "light",
        type: "vertical",
        shadeIntensity: 0.5,
        gradientToColors: ["#F8D66D"],
        inverseColors: true,
        opacityFrom: 0.8,
        opacityTo: 0.2,
        stops: [0, 100],
      },
    },
    tooltip: {
      shared: true,
      intersect: false,
      // Removed marker.radius from options because it's not part of the ApexOptions type.
      custom: function({ dataPointIndex, w }: { dataPointIndex: number; w: any }): string {
        let tooltipHtml = `<div style="padding: 8px;">`;
        w.config.series.forEach((s: any, i: number) => {
          const point = s.data[dataPointIndex];
          let value = "";
          if (point && Array.isArray(point.y)) {
            value = `${point.y[0]} - ${point.y[1]}`;
          } else if (point && point.y !== undefined) {
            value = point.y;
          }
          if (value !== "") {
            tooltipHtml += `<div style="font-size: 12px; margin-bottom: 2px;">
                          <span style="display:inline-block;width:8px;height:8px;border-radius:50%;background-color:${w.globals.colors[i]};margin-right:4px;"></span>
                          <strong>${s.name}:</strong> ${value}
                        </div>`;
          }
        });
        tooltipHtml += `</div>`;
        return tooltipHtml;
      }
    },
    legend: {
      offsetY: 10
    }
  };

  return (
    <div id="chart">
      {props.loading ? (
        <div>Loading chart...</div>
      ) : (
        <ReactApexChart
          options={options}
          series={props.seriesData as any}
          type="rangeArea"
          height={500}
        />
      )}
    </div>
  );
};
