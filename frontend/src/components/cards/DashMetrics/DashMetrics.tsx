import { HeartRate } from "@/components/cards/DashMetrics/HeartRate/HeartRate.tsx";
import { VO2Max } from "@/components/cards/DashMetrics/VO2Max/VO2Max.tsx";
import { HRVStatus } from "@/components/cards/DashMetrics/HRV/HRV.tsx";
import { Stress } from "@/components/cards/DashMetrics/Stress/Stress.tsx";
import { Sleep } from "@/components/cards/DashMetrics/Sleep/Sleep.tsx";
import { IntensityMinutes } from "@/components/cards/DashMetrics/IntensityMiniutes/IntensityMinutes.tsx";

export const DashMetrics = () => {
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

      <div className="grid grid-cols-1 gap-4  md:grid-cols-2 md:gap-6 xl:grid-cols-3 2xl:gap-7.5">
        <HeartRate />
        <VO2Max />
        <HRVStatus />
        <Sleep />
        <Stress />
        <IntensityMinutes />
      </div>
    </div>
  );
};
