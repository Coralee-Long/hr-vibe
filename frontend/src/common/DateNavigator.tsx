import React from "react";
import { Tooltip } from "@/common/ToolTip.tsx";

interface DateNavigatorProps {
  /**
   * The currently displayed date in "YYYY-MM-DD" format.
   */
  currentDate: string;
  /**
   * The latest available date (i.e. the date limit) in "YYYY-MM-DD" format.
   */
  latestDate: string;
  /**
   * Callback to update the current date.
   */
  onDateChange: (newDate: string) => void;
}

// Helper to format a Date object as "YYYY-MM-DD"
const formatDate = (date: Date): string => {
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  return `${year}-${month}-${day}`;
};

export const DateNavigator: React.FC<DateNavigatorProps> = ({
                                                              currentDate,
                                                              latestDate,
                                                              onDateChange,
                                                            }) => {
  const current = new Date(currentDate);

  const handlePrev = () => {
    const newDate = new Date(current);
    newDate.setDate(current.getDate() - 1);
    onDateChange(formatDate(newDate));
  };

  const handleNext = () => {
    const newDate = new Date(current);
    newDate.setDate(current.getDate() + 1);
    onDateChange(formatDate(newDate));
  };

  // Disable next if currentDate is at or past latestDate.
  const disableNext = currentDate >= latestDate;

  return (
    <div className="flex items-center space-x-2">
      <button onClick={handlePrev} className="p-2" aria-label="Previous Day">
        <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor">
          <polyline points="15 18 9 12 15 6" />
        </svg>
      </button>
      <div className="text-lg font-medium">{currentDate}</div>
      <Tooltip
        active={disableNext}
        position="right"
        tooltipText="No more data available past this date"
      >
        {/* Wrap in a span so the container is clickable */}
        <span className="inline-block">
          <button
            // Only attach handleNext if not disabled.
            onClick={!disableNext ? handleNext : undefined}
            // Apply simulated disabled styling if disableNext is true.
            className={`p-2 ${disableNext ? "cursor-not-allowed opacity-50" : ""}`}
            aria-label="Next Day"
          >
            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor">
              <polyline points="9 18 15 12 9 6" />
            </svg>
          </button>
        </span>
      </Tooltip>
    </div>
  );
};

export default DateNavigator;
