import React from "react";
import { Tooltip } from "@/common/ToolTip.tsx";
import { FaRegCalendar } from "react-icons/fa";
// import { TbPlayerTrackPrevFilled, TbPlayerTrackNextFilled } from "react-icons/tb";
import { GrLinkPrevious, GrLinkNext } from "react-icons/gr";

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
    <div className="flex items-center justify-end space-x-2 rounded border border-stroke bg-white py-2 px-4 text-sm font-medium shadow-card-2 focus-visible:outline-none dark:border-strokedark dark:bg-boxdark">
      <button onClick={handlePrev} className="p-2" aria-label="Previous Day">
        <GrLinkPrevious size={16} />
      </button>
      <FaRegCalendar size={20} />
      <div className="text-lg font-medium p-1">{currentDate}</div>
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
            <GrLinkNext size={16} />
          </button>
        </span>
      </Tooltip>
    </div>
  );
};

export default DateNavigator;
