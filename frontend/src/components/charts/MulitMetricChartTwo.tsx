import React from 'react';
import ReactApexChart from 'react-apexcharts';
import { MultiMetricChartProps } from "@/types/MultiMetricsChartTypes.ts";

/**
 * MultiMetricChartTwo renders the Respiration & SpO₂ chart.
 * It displays:
 *  - A range area for Respiration (using rrMin and rrMax)
 *  - A line for Waking Respiration Average
 *  - A line for SpO₂ Average
 *
 * The series data is assumed to be in xy format.
 */
export const MultiMetricChartTwo: React.FC<MultiMetricChartProps> = (props) => {
  const hasXYData = props.seriesData.some(series =>
    series.data.length > 0 &&
    typeof series.data[0] === 'object' &&
    'x' in series.data[0]
  );

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
    dataLabels: {
      enabled: false,
    },
    stroke: {
      curve: "straight" as const,
      // First series (range area) gets no stroke; subsequent line series get a stroke width of 2.
      width: [0, 2, 2],
    },
    colors: [
      "#259AE6", // Color for Respiration Range (range area)
      "#ff9466", // Color for Waking Respiration Average line
      "#3C50E0", // Color for SpO₂ Average line
    ],
    fill: {
      type: ['gradient', 'solid', 'solid'],
      opacity: [1, 1, 1],
      gradient: {
        shade: 'light',
        type: 'vertical',
        shadeIntensity: 0.5,
        gradientToColors: ['#1E90FF'], // End color for the range area's gradient
        inverseColors: true,
        opacityFrom: 0.8,
        opacityTo: 0.2,
        stops: [0, 100],
      },
    },
    tooltip: {
      shared: true,
      intersect: false,
      custom: function({ dataPointIndex, w }: { dataPointIndex: number; w: any }): string {
        let tooltipHtml = `<div style="padding: 8px;">`;
        w.config.series.forEach((s: any, i: number) => {
          // Skip this series if it's hidden.
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
      },
    },
    legend: {
      offsetY: 10
    }
  };

  return (
    <div id="chart-two">
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
