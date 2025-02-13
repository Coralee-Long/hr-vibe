// DashMetrics.tsx
import React from "react";
import { RecentDailySummariesDTO } from "@/types/RecentDailySummariesDTO";
import { Card } from "@/common/Card";
import { DateNavigator } from "@/common/DateNavigator";
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
   * Indicates whether data is currently being loaded.
   */
  loading?: boolean;
  /**
   * The fetched daily summaries data.
   */
  summaries?: RecentDailySummariesDTO | null;
  /**
   * The selected reference date in "YYYY-MM-DD" format.
   */
  referenceDate: string;
  /**
   * The latest available date fetched from the API.
   * Used to restrict the maximum date in the DateNavigator.
   */
  latestDate: string;
  /**
   * Callback function to update the selected reference date.
   */
  onDateChange: (newDate: string) => void;
};

/**
 * DashMetrics Component
 *
 * This component renders the dashboard metrics along with a header.
 * The header includes the title, description, and the DateNavigator component.
 * The DateNavigator lets users update the reference date, which is then passed up to the Dashboard.
 * The component also computes shared data (like last 7 days categories) for the metric cards.
 */
export const DashMetrics: React.FC<DashMetricsProps> = ({
                                                          loading = false,
                                                          referenceDate,
                                                          latestDate,
                                                          onDateChange,
                                                        }) => {
  // Generate the last 7 days categories based on the reference date.
  const categories = generateLast7DaysCategories(referenceDate);

  return (
    <div>
      {/* Header Section */}
      <div className="mb-5 flex flex-col sm:flex-row items-center justify-between">
        <div className="w-1/2">
          <h2 className="mb-1.5 text-title-md2 font-bold text-black dark:text-white">
            At a Glance
          </h2>
          <p className="font-medium">Latest Metrics</p>
        </div>
        <div className="date-navigator">
          {/* Render the DateNavigator for date selection */}
          <DateNavigator
            currentDate={referenceDate}
            latestDate={latestDate}
            onDateChange={onDateChange}
          />
        </div>
      </div>

      {/* Grid of Metric Cards */}
      <div className="grid grid-cols-1 gap-4 md:grid-cols-2 md:gap-6 2xl:grid-cols-3 2xl:gap-7.5">
        <Card title="Heart Rate" icon={<FaHeartPulse size={32} color="#FF6961" />}>
          <HeartRate loading={loading} referenceDate={referenceDate} categories={categories} />
        </Card>

        <Card title="BodyBattery" icon={<RiBattery2ChargeFill size={32} color="#FFB54C" />}>
          <BodyBattery loading={loading} referenceDate={referenceDate} categories={categories} />
        </Card>

        <Card title="Sleep" icon={<PiMoonStarsFill size={32} color="#88CFF1" />}>
          <Sleep loading={loading} referenceDate={referenceDate} categories={categories}/>
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

