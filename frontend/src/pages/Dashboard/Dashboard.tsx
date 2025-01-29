import { DefaultLayout } from "@/layout/DefaultLayout.tsx";
import { DashMetrics } from "@/components/StatsCards/DashMetrics/DashMetrics.tsx";

export const Dashboard = () => {
  return (
    <>
      <DefaultLayout>
        <div className="grid grid-cols-1 gap-4 md:gap-6 2xl:gap-7.5">
          <DashMetrics />
        </div>

        {/*<div className="mt-7.5 grid grid-cols-12 gap-4 md:gap-6 2xl:gap-7.5">*/}
        {/*   Other Cards*/}
        {/*</div>*/}
      </DefaultLayout>
    </>
  );
};
