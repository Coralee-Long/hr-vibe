import React from "react";
import { LoaderNoBg } from "@/common/LoaderNoBg";
import { useRecentDailySummaries } from "@/context/RecentDailySummariesContext";
import { GenericChart } from "@/components/charts/GenericChart";
import { ActivitiesPieChartConfig } from "@/config/ActivitiesPieChartConfig";
import { DataField} from "@/common/DataField.tsx";
import {
  convertTimeToMinutes,
  roundTo2Decimals,
} from "@/utils/timeUtils";

// Import icons for data fields
import { IoFootsteps } from "react-icons/io5";
import { PiStepsFill } from "react-icons/pi";
import { FaRoad, FaFire, FaRegClock } from "react-icons/fa";
import { GiWaterDrop } from "react-icons/gi";

type ActivitiesProps = {
  /**
   * Indicates if data is still being fetched.
   */
  loading: boolean;
  /**
   * The reference date (format: "YYYY-MM-DD") for which the metrics are displayed.
   */
  referenceDate: string;
};

/**
 * Activities Component
 *
 * Displays a single-day overview of activity metrics including:
 * - A grid of data fields: Steps, Floors, Distance, Calories, Duration, and Sweat Loss.
 * - A three-slice pie chart showing vigorous, moderate, and leftover (low-intensity) minutes.
 *
 * Missing or zero values are displayed as "--". If the total intensity time
 * is below a set threshold, a "No Activity Data" message is shown instead of the chart.
 *
 * @param props - The component properties.
 * @returns A React element representing the activities summary.
 */
export const Activities: React.FC<ActivitiesProps> = ({ loading }) => {
  const { currentDayData } = useRecentDailySummaries();

  // Show loader if data is still loading or unavailable
  if (loading || !currentDayData) {
    return (
      <div className="flex h-full items-center justify-center">
        <LoaderNoBg />
      </div>
    );
  }

  // Extract single-day activity data
  const {
    steps,
    floors,
    activitiesCalories,
    intensityTime,         // e.g., "00:02:00"
    sweatLoss,
    activitiesDistance,
    moderateActivityTime,   // e.g., "00:00:00"
    vigorousActivityTime,   // e.g., "00:01:00"
  } = currentDayData.summary;

  // ------------------ Helper Functions ------------------ //
  /**
   * Safely displays a numeric value or returns "--" if missing or zero.
   * @param val - The numeric value.
   * @param suffix - Optional suffix to append (e.g., " kCal", " ml").
   */
  const safeNumberDisplay = (val?: number | null, suffix?: string): string => {
    if (!val || val === 0) return "--";
    return suffix ? `${val}${suffix}` : val.toString();
  };

  /**
   * Safely displays a distance value rounded to 2 decimals, or returns "--" if missing.
   * @param val - The distance value.
   */
  const safeDistanceDisplay = (val?: number | null): string => {
    if (!val || val === 0) return "--";
    return `${roundTo2Decimals(val)} km`;
  };

  /**
   * Safely displays a time string (HH:MM:SS) or returns "--" if missing or zero.
   * @param time - The time string.
   */
  const safeTimeDisplay = (time?: string | null): string => {
    if (!time || time === "00:00:00") return "--";
    return time;
  };

  // ------------------ Prepare Display Values ------------------ //
  const stepsDisplay = safeNumberDisplay(steps);
  const floorsDisplay = safeNumberDisplay(floors);
  const caloriesDisplay = safeNumberDisplay(activitiesCalories, " kCal");
  const distanceDisplay = safeDistanceDisplay(activitiesDistance);
  const intensityTimeDisplay = safeTimeDisplay(intensityTime);
  const sweatLossDisplay = safeNumberDisplay(sweatLoss, " ml");

  // ------------------ Compute Pie Chart Slices ------------------ //
  // Convert the time strings to minutes
  const totalMinutes = convertTimeToMinutes(intensityTime || "00:00:00");
  const moderateMinutes = convertTimeToMinutes(moderateActivityTime || "00:00:00");
  const vigorousMinutes = convertTimeToMinutes(vigorousActivityTime || "00:00:00");

  // The leftover "Low" slice represents the unclassified minutes
  let otherMinutes = totalMinutes - (moderateMinutes + vigorousMinutes);
  if (otherMinutes < 0) otherMinutes = 0;

  // Set a minimum threshold for total activity; if not met, the chart is not shown.
  const MIN_ACTIVITY_THRESHOLD = 3; // in minutes
  const hasEnoughActivity = totalMinutes >= MIN_ACTIVITY_THRESHOLD;

  // Define pie chart data and options
  const pieLabels = ["Vigorous", "Moderate", "Low"];
  const pieSeries = [vigorousMinutes, moderateMinutes, otherMinutes];
  const pieChartOptions = ActivitiesPieChartConfig("Activity Intensity", pieLabels);

  // ------------------ Data Fields Grid ------------------ //
  // Create an array of objects representing each data field
  const dataFields = [
    {
      icon: <IoFootsteps size={36} />,
      label: "Steps",
      value: stepsDisplay,
    },
    {
      icon: <PiStepsFill size={36} />,
      label: "Floors",
      value: floorsDisplay,
    },
    {
      icon: <FaRoad size={36} />,
      label: "Distance",
      value: distanceDisplay,
    },
    {
      icon: <FaFire size={36} />,
      label: "Calories",
      value: caloriesDisplay,
    },
    {
      icon: <FaRegClock size={36} />,
      label: "Duration",
      value: intensityTimeDisplay,
    },
    {
      icon: <GiWaterDrop size={36} />,
      label: "Sweat Loss",
      value: sweatLossDisplay,
    },
  ];

  return (
    <div className="activities-container flex flex-col justify-center items-between">
    <div className="datafields-wrapper">
      {/* ------------------ Data Fields Grid ------------------ */}
      <div className="grid grid-cols-2 gap-4">
        {dataFields.map((field, index) => (
          <DataField
            key={index}
            icon={field.icon}
            label={field.label}
            value={field.value}
          />
        ))}
      </div>

      {/* ------------------ Pie Chart ------------------ */}
      <div className="chart-wrapper mt-6 pt-4 w-full min-w-[320px] min-h-[200]">
        {hasEnoughActivity ? (
          <GenericChart
            options={pieChartOptions}
            series={pieSeries}
            type="pie"
            height={200}
            width="100%"
            loading={false}
          />
        ) : (
          <div className="flex h-[200px] items-center justify-center text-gray-500 dark:text-gray-400">
            No Activity Data
          </div>
        )}
      </div>
    </div>
    </div>
  );
};
