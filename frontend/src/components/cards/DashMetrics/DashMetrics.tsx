// DashMetrics.tsx
import React from "react";
import { HeartRate } from "@/components/cards/DashMetrics/HeartRate/HeartRate";
import { VO2Max } from "@/components/cards/DashMetrics/VO2Max/VO2Max";
import { HRVStatus } from "@/components/cards/DashMetrics/HRV/HRV";
import { Stress } from "@/components/cards/DashMetrics/Stress/Stress";
import { Sleep } from "@/components/cards/DashMetrics/Sleep/Sleep";
import { IntensityMinutes } from "@/components/cards/DashMetrics/IntensityMiniutes/IntensityMinutes";
import { Loader } from "@/common/Loader";

type DashMetricsProps = {
  /**
   * When true, displays a loading spinner instead of the metrics.
   */
  loading?: boolean;
}

export const DashMetrics: React.FC<DashMetricsProps> = ({ loading = false }) => {
  if (loading) {
    return (
      <div className="flex items-center justify-center p-4">
        <Loader />
      </div>
    );
  }

  return (
    <div>
      <div className="mb-5 flex items-center justify-between">
        <div>
          <h2 className="mb-1.5 text-title-md2 font-bold text-black dark:text-white">
            At a Glance
          </h2>
          <p className="font-medium">Latest Metrics</p>
        </div>
      </div>
      <div className="grid grid-cols-1 gap-4 md:grid-cols-2 md:gap-6 xl:grid-cols-3 2xl:gap-7.5">
        <HeartRate loading={loading} />
        <VO2Max />
        <HRVStatus />
        <Sleep />
        <Stress />
        <IntensityMinutes />
      </div>
    </div>
  );
};

export default DashMetrics;
