interface Metric {
  name: string;
  visible: boolean;
}

interface MetricTogglesProps {
  metrics: Metric[];
  toggleMetric: (name: string) => void;
}

export const MetricToggles: React.FC<MetricTogglesProps> = ({
  metrics,
  toggleMetric,
}) => {
  return (
    <>
      <div className="mb-8">
        <h2 className="mb-1.5 text-title-md2 font-bold text-black dark:text-white">
          Health Metrics
        </h2>
        {/*<p className="font-medium">SubHeading</p>*/}
      </div>

      <div className="flex flex-col gap-4 mb-4">
        {metrics.map((metric) => (
          <label key={metric.name} className="flex items-center space-x-2">
            <input
              type="checkbox"
              checked={metric.visible}
              onChange={() => toggleMetric(metric.name)}
              className="cursor-pointer"
            />
            <span>{metric.name}</span>
          </label>
        ))}
      </div>
    </>
  );
};
