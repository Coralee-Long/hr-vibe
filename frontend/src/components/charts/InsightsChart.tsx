import { useState } from "react";
import { MetricToggles } from "@/pages/Insights/MetricToggles.tsx";
import { ChartComponent } from "@/pages/Insights/ChartComponent.tsx";

interface Metric {
  name: string;
  data: number[];
  visible: boolean;
}

export const InsightsChart = () => {
  const [metrics, setMetrics] = useState<Metric[]>([
    { name: "Stress", data: [10, 20, 15, 30, 40, 25, 50], visible: true },
    { name: "Sleep", data: [50, 72, 73, 65, 90, 88, 79], visible: true },
    { name: "HRV Status", data: [70, 75, 65, 80, 85, 90, 78], visible: true },
    {
      name: "Intensity Minutes",
      data: [30, 45, 40, 60, 50, 55, 70],
      visible: true,
    },
  ]);

  const toggleMetric = (name: string) => {
    setMetrics((prevMetrics) =>
      prevMetrics.map((metric) =>
        metric.name === name ? { ...metric, visible: !metric.visible } : metric
      )
    );
  };

  return (
    <div className="rounded-sm border border-stroke bg-white p-4 shadow-default dark:border-strokedark dark:bg-boxdark md:p-6 xl:p-7.5">
      <div className="mb-8">
        <h2 className="mb-1.5 text-title-md2 font-bold text-black dark:text-white">
          Insights
        </h2>
        <p className="font-medium">Explore correlations</p>
      </div>
      {/* Chart */}
      <ChartComponent metrics={metrics} />
      {/* Toggles */}
      <MetricToggles metrics={metrics} toggleMetric={toggleMetric} />
    </div>
  );
};
