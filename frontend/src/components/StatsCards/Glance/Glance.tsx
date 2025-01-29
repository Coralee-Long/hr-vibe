import { HeartRate } from "@/components/StatsCards/Glance/HeartRate.tsx";

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

      <div className="grid grid-cols-1 gap-4 md:grid-cols-2 md:gap-6 2xl:gap-7.5">
        <HeartRate />

        <div
          className="rounded-sm border border-stroke bg-white p-4 shadow-default dark:border-strokedark dark:bg-boxdark md:p-6 xl:p-7.5">
          <h3 className="mb-2 text-title-md font-bold text-black dark:text-white">
            Another Stat
          </h3>
        </div>
        <div
          className="rounded-sm border border-stroke bg-white p-4 shadow-default dark:border-strokedark dark:bg-boxdark md:p-6 xl:p-7.5">
          <h3 className="mb-2 text-title-md font-bold text-black dark:text-white">
            Another Stat
          </h3>
        </div>
        <div
          className="rounded-sm border border-stroke bg-white p-4 shadow-default dark:border-strokedark dark:bg-boxdark md:p-6 xl:p-7.5">
          <h3 className="mb-2 text-title-md font-bold text-black dark:text-white">
            Another Stat
          </h3>
        </div>
      </div>
    </div>
  );
};

