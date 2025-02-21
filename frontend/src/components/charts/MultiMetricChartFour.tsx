import React from 'react';
import ReactApexChart from 'react-apexcharts';
import { MultiMetricChartProps } from "@/types/MultiMetricsChartTypes.ts";

/**
 * MultiMetricChartFour renders a mixed chart for activity metrics.
 * It displays:
 *  - Intensity Time as a filled area series (with gradient fill).
 *  - Moderate Activity as a solid line series.
 *  - Vigorous Activity as a solid line series.
 *
 * This configuration mirrors the "gold standard" of Chart One so that
 * both the area and line series display correctly.
 */
export const MultiMetricChartFour: React.FC<MultiMetricChartProps> = (props) => {
  // Check if series data is provided in xy format.
  const hasXYData = props.seriesData.some(series =>
    series.data.length > 0 &&
    typeof series.data[0] === 'object' &&
    'x' in series.data[0]
  );

  const options = {
    chart: {
      animations: { speed: 500 },
      // Using "line" as the overall type allows mixing area and line series.
      type: "line" as const,
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
      // Configure stroke widths:
      // - First series (Intensity Time area) gets no stroke (so only its fill shows).
      // - Subsequent series (Moderate and Vigorous Activity) get a stroke width of 2.
      width: [0, 2, 2],
    },
    colors: [
      "#10B981", // Base color for Intensity Time area
      "#ff9466", // Color for Moderate Activity line
      "#D34053", // Color for Vigorous Activity line
    ],
    fill: {
      // Only the first series gets a gradient fill; the others get no fill.
      type: ['gradient', 'solid', 'solid'],
      opacity: [1, 1, 1],
      gradient: {
        shade: 'light',
        type: 'vertical',
        shadeIntensity: 0.5,
        // Configure the gradient so that the top is gold and the bottom is the base color.
        gradientToColors: ['#FFD700'], // Gold at the top
        inverseColors: true,
        opacityFrom: 0.8,
        opacityTo: 0.2,
        stops: [0, 100],
      }
    },
    tooltip: {
      shared: true,
      intersect: false,
      custom: function({ dataPointIndex, w }: { dataPointIndex: number; w: any }): string {
        let tooltipHtml = `<div style="padding: 8px;">`;
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
    <div id="chart-four">
      {props.loading ? (
        <div>Loading chart...</div>
      ) : (
        // Casting seriesData to any bypasses strict type issues.
        <ReactApexChart
          options={options}
          series={props.seriesData as any}
          type="line"
          height={500}
        />
      )}
    </div>
  );
};
