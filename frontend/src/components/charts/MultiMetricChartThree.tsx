import React from 'react';
import ReactApexChart from 'react-apexcharts';
import { MultiMetricChartProps } from "@/types/MultiMetricsChartTypes.ts";

/**
 * MultiMetricChartThree renders the Sleep & Activity chart.
 * It displays:
 *  - Bar series for REM Sleep and Non-REM Sleep (in minutes)
 *  - A line series for Intensity Time (in minutes)
 *  - An area series for Moderate Activity (in minutes)
 *  - An area series for Vigorous Activity (in minutes)
 *
 * Because this chart mixes different series types, you may want to adjust
 * the chart type or options as necessary.
 */
export const MultiMetricChartThree: React.FC<MultiMetricChartProps> = (props) => {
  const hasXYData = props.seriesData.some(series =>
    series.data.length > 0 &&
    typeof series.data[0] === 'object' &&
    'x' in series.data[0]
  );

  const options = {
    chart: {
      type: "bar" as const,
      stacked: true,
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
    dataLabels: {
      enabled: false,
    },
    stroke: {
      curve: "smooth" as const,
      // Define stroke widths per series.
      width: [0, 2, 0, 0, 0],
    },
    colors: [
      "#3C50E0", // Color for REM Sleep bars
      "#FFB54C", // Color for Non-REM Sleep bars
    ],
    fill: {
      // For bar series, fill is controlled by the chart.
      type: ['solid', 'solid'],
      opacity: [1, 1],
    },
    tooltip: {
      shared: true,
      intersect: false,
      custom: function({ dataPointIndex, w }: { dataPointIndex: number; w: any }): string {
        let tooltipHtml = `<div style="padding: 8px;">`;
        // Loop through each series
        w.config.series.forEach((s: any, i: number) => {
          if (w.globals.seriesHidden && w.globals.seriesHidden[i]) return;
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
    <div id="chart-three">
      {props.loading ? (
        <div>Loading chart...</div>
      ) : (
        <ReactApexChart
          options={options}
          series={props.seriesData as any}
          // Note: Ensure the chart type passed here matches your options if needed.
          type="line"
          height={500}
        />
      )}
    </div>
  );
};
