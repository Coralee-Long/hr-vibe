import React from "react";
import { RecentDailySummariesDTO } from "@/types/RecentDailySummariesDTO";
import { Card } from "@/common/Card";
import { HeartRate } from "@/components/metrics/DashMetrics/HeartRate/HeartRate";
import { BodyBattery } from "@/components/metrics/DashMetrics/BodyBattery/BodyBattery";
import { Stress } from "@/components/metrics/DashMetrics/Stress/Stress";
import { Sleep } from "@/components/metrics/DashMetrics/Sleep/Sleep";
import { IntensityMinutes } from "@/components/metrics/DashMetrics/IntensityMiniutes/IntensityMinutes";
import { FaHeartPulse } from "react-icons/fa6";
import { RiBattery2ChargeFill } from "react-icons/ri";
import { PiMoonStarsFill } from "react-icons/pi";
import { BiHealth } from "react-icons/bi";
import { PiClockCountdownFill } from "react-icons/pi";

export type DashMetricsProps = {
  /**
   * Indicates if data is still being fetched.
   */
  loading?: boolean;
  /**
   * The complete summaries data fetched from the Dashboard.
   * This may be of type RecentDailySummariesDTO or null.
   */
  summaries?: RecentDailySummariesDTO | null;
  /**
   * The currently selected reference date for which data is displayed.
   * This date is used to filter and generate metrics for a specific day.
   * Expected format is "YYYY-MM-DD" (e.g., "2025-01-24").
   */
  referenceDate: string;
};

/**
 * DashMetrics Component
 *
 * This component renders a header and a grid of metric cards.
 * It receives:
 * - loading: a boolean indicating if data is still being fetched.
 * - summaries: the fetched data (or null if no data is available).
 *
 * Each Card displays a title, an icon, and a metric component.
 * For example, the HeartRate card renders the HeartRate component.
 *
 * Important: While DashMetrics receives the entire summaries data,
 * each metric component (e.g. HeartRate) uses the useRecentDailySummaries hook
 * to retrieve the specific data it needs from the context (e.g., the rhrAvg array).
 * This approach avoids passing down large objects through props when only parts are needed.
 */
export const DashMetrics: React.FC<DashMetricsProps> = ({ loading = false, summaries, referenceDate }) => {
  console.log("DashMetrics received summaries:", summaries);

  return (
    <div>
      {/* Header Section */}
      <div className="mb-5 flex items-center justify-between">
        <div>
          <h2 className="mb-1.5 text-title-md2 font-bold text-black dark:text-white">
            At a Glance
          </h2>
          <p className="font-medium">Latest Metrics</p>
        </div>
      </div>

      {/* Grid of Cards */}
      <div className="grid grid-cols-1 gap-4 md:grid-cols-2 md:gap-6 2xl:grid-cols-3 2xl:gap-7.5">
        {/* HeartRate Card */}
        <Card title="Heart Rate" icon={<FaHeartPulse size={32} color="#FF6961" />}>
          {/*
            The HeartRate component retrieves its specific data (like resting heart rate)
            directly from context using the useRecentDailySummaries hook.
            The loading state is passed as a prop.
          */}
          <HeartRate loading={loading} referenceDate={referenceDate} />
        </Card>

        {/* BodyBattery Card */}
        <Card title="BodyBattery" icon={<RiBattery2ChargeFill size={32} color="#FFB54C" />}>
          <BodyBattery />
        </Card>

        {/* Sleep Card */}
        <Card title="Sleep" icon={<PiMoonStarsFill size={32} color="#88CFF1" />}>
          <Sleep />
        </Card>

        {/* Stress Card */}
        <Card title="Stress" icon={<BiHealth size={32} color="#8CD47E" />}>
          <Stress />
        </Card>

        {/* Intensity Minutes Card */}
        <Card title="Intensity Minutes" icon={<PiClockCountdownFill size={32} color="#3C50E0" />}>
          <IntensityMinutes />
        </Card>
      </div>
    </div>
  );
};
