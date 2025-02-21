import { ApexOptions } from "apexcharts";

/**
 * StressChartConfig
 *
 * Generates ApexCharts options for the Stress chart,
 * a radar chart displaying stress data.
 *
 * @param title - The chart title.
 * @param categories - The circular/perimeter categories (e.g., the last 7 days).
 * @param colors - A single colour to represent the stress data.
 * @returns An ApexOptions object configured for the Stress chart.
 */
export const StressChartConfig = (
  title: string,
  categories: string[],
  colors: string[]
): ApexOptions => {
  return {
    chart: {
      type: "radar",
      toolbar: { show: false },
      zoom: { enabled: false },
      fontFamily: "Satoshi, sans-serif",
    },
    plotOptions: {
      radar: {
        polygons: {
          strokeColors: "#3d4d60",
          strokeWidth: "1",
          connectorColors: "#3d4d60",
          fill: {
            colors: ["rgba(61, 77, 96, 0.2)", "#24303F"]
          },
        },
      },
    },
      legend: { position: "bottom", offsetY: 10 },
      fill: {
        opacity: 0.4,
        colors,
      },
      markers: {
        size: 3,
        hover: {
          size: 5
        }
      },
      title: {
        text: title,
        align: "left",
        style: { color: "#AEB7C0" },
      },
      xaxis: {
        categories: categories,
        labels: {
          show: true,
          style: {
            colors: ["#a8a8a8"],
            fontSize: "11px",

          }
        }
      },
    yaxis: {
      min: 0,
      max: 100,
      show: false,
    },
    dataLabels: {
      enabled: false,
      background: {
        enabled: true,
        borderRadius: 2,
      }
    },
    // Custom tooltip to override the default marker size
    tooltip: {
      marker: { show: false },
      shared: true,
      intersect: false,
      custom: function({
                         series,
                         dataPointIndex,
                         w,
                       }: {
        series: number[][];
        seriesIndex: number;
        dataPointIndex: number;
        w: any;
      }): string {
        let tooltipHtml = `<div style="padding:8px; font-size:12px;">`;
        w.config.series.forEach((s: any, i: number) => {
          // If the series is hidden, skip it.
          if (w.globals.seriesHidden && w.globals.seriesHidden[i]) return;
          const val = series[i][dataPointIndex];
          // Build a small marker inline with the text.
          const markerHtml = `<span style="display:inline-block;width:6px;height:6px;border-radius:50%;background-color:${w.globals.colors[i]};margin-right:4px;"></span>`;
          tooltipHtml += `<div style="margin-bottom:2px;">
                            ${markerHtml}<strong>${s.name}:</strong> ${val}
                          </div>`;
        });
        tooltipHtml += `</div>`;
        return tooltipHtml;
      },
    },
    }
};
