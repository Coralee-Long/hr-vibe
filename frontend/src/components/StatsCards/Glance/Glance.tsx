import { HeartRate } from "@/components/StatsCards/Glance/HeartRate.tsx";
import { VO2Max } from "@/components/StatsCards/Glance/VO2Max.tsx";
import { HRVStatus } from "@/components/StatsCards/Glance/HRV.tsx";
import { Stress } from "@/components/StatsCards/Glance/Stress.tsx";
import { Sleep } from "@/components/StatsCards/Glance/Sleep.tsx";
import { IntensityMinutes } from "@/components/StatsCards/Glance/IntensityMinutes.tsx";

export const Glance = () => {
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
