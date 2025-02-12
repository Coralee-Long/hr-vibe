// DashMetrics.tsx
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
import { generateLast7DaysCategories } from "@/utils/dateUtils";

/**
 * Props for the DashMetrics component.
 */
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
   * Expected format is "YYYY-MM-DD" (e.g., "2025-01-24").
   */
  referenceDate: string;
};

/**
 * DashMetrics Component
 *
 * This component renders a header and a grid of metric cards.
 * It computes the common last 7 days’ categories using the referenceDate
 * and passes that shared data to each child card (e.g., HeartRate, BodyBattery).
 */
export const DashMetrics: React.FC<DashMetricsProps> = ({
                                                          loading = false,
                                                          referenceDate,
                                                        }) => {
  // Compute the shared x-axis labels (last 7 days) using our utility function.
  const categories = generateLast7DaysCategories(referenceDate);

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
          <HeartRate loading={loading} referenceDate={referenceDate} categories={categories} />
        </Card>

        {/* BodyBattery Card */}
        <Card title="BodyBattery" icon={<RiBattery2ChargeFill size={32} color="#FFB54C" />}>
          <BodyBattery loading={loading} referenceDate={referenceDate} categories={categories} />
        </Card>

        {/* Other cards (Sleep, Stress, Intensity Minutes) remain unchanged */}
        <Card title="Sleep" icon={<PiMoonStarsFill size={32} color="#88CFF1" />}>
          <Sleep />
        </Card>
        <Card title="Stress" icon={<BiHealth size={32} color="#8CD47E" />}>
          <Stress />
        </Card>
        <Card title="Intensity Minutes" icon={<PiClockCountdownFill size={32} color="#3C50E0" />}>
          <IntensityMinutes />
        </Card>
      </div>
    </div>
  );
};

export default DashMetrics;
