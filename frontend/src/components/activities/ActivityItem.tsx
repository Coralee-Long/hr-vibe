import React from "react";
import { CountDownTimer } from "@/common/CountdownTimer";

/**
 * Props for the ActivityItem component.
 */
export type ActivityItemProps = {
  /**
   * URL or path to the event logo image.
   */
  image: string;
  /**
   * Name of the event.
   */
  name: string;
  /**
   * Location of the event.
   */
  location: string;
  /**
   * Distance for the event (e.g., "42km").
   */
  distance: string;
  /**
   * The event date in "YYYY-MM-DD" format.
   */
  eventDate: string;
  /**
   * The event date formatted as "DD Month YYYY".
   */
  formattedEventDate: string;
};

/**
 * ActivityItem Component
 *
 * This component renders an individual event item that displays the event logo,
 * name, location, distance, formatted event date, and a countdown timer.
 *
 * The countdown timer is styled to remain in a single row by using the "flex-nowrap" class.
 *
 * @param props - An object containing an event item.
 * @returns A JSX element representing the event item.
 */
export const ActivityItem: React.FC<{ item: ActivityItemProps }> = ({ item }) => {
  return (
    <div className="rounded-[5px] border border-stroke p-5 dark:border-strokedark">
      {/* Header: Event logo, name, location, distance, and formatted date */}
      <div className="mb-5.5 flex items-center justify-between">
        <div className="flex items-center gap-4.5">
          <div className="flex h-11.5 w-11.5 items-center justify-center rounded-full bg-[#EEF2F8]">
            <img src={item?.image} alt="event logo" />
          </div>
          <div>
            <h5 className="font-bold text-black dark:text-white">{item.name}</h5>
            <p className="title-xsm font-medium">{item.location}</p>
          </div>
        </div>
        <div className="text-right">
          <h5 className="font-bold text-black dark:text-white">{item.distance}</h5>
          <p className="title-xsm font-medium">{item.formattedEventDate}</p>
        </div>
      </div>
      {/* Countdown Timer */}
      <div className="countdown-wrapper flex flex-nowrap items-center w-full mt-12">
        <CountDownTimer finalDate={item.eventDate}  />
      </div>
    </div>
  );
};
