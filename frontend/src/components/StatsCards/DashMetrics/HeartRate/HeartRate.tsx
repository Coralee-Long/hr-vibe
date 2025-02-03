import { HeartRateDonutChart } from "@/components/Charts/HeartRateDonutChart.tsx";
import { TrendsGraph } from "@/components/Charts/TrendsGraph.tsx";
import { useRecentDailySummaries } from "@/context/RecentDailySummariesContext.tsx";
import { useEffect } from "react";

export const HeartRate = () => {
  const { summaries } = useRecentDailySummaries();

// ✅ Directly use rhrAvg as the data for TrendsGraph
  const restingHrData = summaries?.rhrAvg || [];

  useEffect(() => {
    console.log(summaries)
  },[])
  return (
    <div
      className="flex flex-col items-center justify-start w-full h-full rounded-lg border border-stroke bg-white p-4 shadow-md dark:border-strokedark dark:bg-boxdark md:p-6 xl:p-7.5">
      {/* TODO: Create Icons Later */}
      <div className="mb-5 flex w-full flex-row items-center gap-3">
        <svg width="34" height="34" viewBox="0 0 34 34" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path
            d="M31.8752 24.65H31.5564V9.19062C31.5564 7.96875 30.5471 6.90625 29.2721 6.90625H26.4033C25.1814 6.90625 24.1189 7.91562 24.1189 9.19062V24.65H20.7189V12.1656C20.7189 10.9437 19.7096 9.88125 18.4346 9.88125H15.5658C14.3439 9.88125 13.2814 10.8906 13.2814 12.1656V24.65H9.82832V15.6187C9.82832 14.3969 8.81895 13.3344 7.54395 13.3344H4.6752C3.45332 13.3344 2.39082 14.3437 2.39082 15.6187V24.65H2.1252C1.4877 24.65 0.90332 25.1813 0.90332 25.8719C0.90332 26.5625 1.43457 27.0938 2.1252 27.0938H31.8752C32.5127 27.0938 33.0971 26.5625 33.0971 25.8719C33.0971 25.1813 32.5127 24.65 31.8752 24.65ZM4.83457 24.65V15.7781H7.4377V24.65H4.83457ZM15.6721 24.65V12.325H18.2752V24.65H15.6721ZM26.5627 24.65V9.35H29.1658V24.65H26.5627V24.65Z"
            fill="#3C50E0"
          />
        </svg>
        <h2 className="text-title-md2 font-bold text-black dark:text-white">
          Heart Rate
        </h2>
      </div>
      <div>
        <HeartRateDonutChart />
        <TrendsGraph title="Resting Heart Rate Trends" label="Avg RHR" data={restingHrData} color="#FF6961" />
      </div>
    </div>
  );
};
