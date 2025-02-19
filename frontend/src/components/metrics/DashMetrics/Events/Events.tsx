import React from "react";
import { EventItem, ActivityItemProps } from "@/components/metrics/DashMetrics/Events/EventItem.tsx";
import { formatDate } from "@/utils/dateUtils";
import MarathonLogo from "@/images/logo/marathonlogo.png";
import wflwrLogo from "@/images/logo/wflwrLogo.png";

/**
 * Events Component
 *
 * This component renders a list of upcoming events using the EventItem component.
 * The event data is defined locally and passed as props to EventItem.
 *
 * @returns A JSX element representing the events list.
 */
export const Events: React.FC = () => {
  // Array of event items with their details.
  const eventItems: ActivityItemProps[] = [
    {
      image: MarathonLogo,
      name: "Rotterdam Marathon",
      location: "The Netherlands",
      distance: "42km",
      eventDate: "2025-04-13",
      formattedEventDate: formatDate("2025-04-13"),
    },
    {
      image: wflwrLogo,
      name: "Wings For Life World Run",
      location: "Slovenia",
      distance: "TBD",
      eventDate: "2025-05-04",
      formattedEventDate: formatDate("2025-05-04"),
    },
  ];

  return (
    <div className="col-span-12 bg-white px-2 shadow-default dark:bg-boxdark xl:col-span-5">
      <div className="flex flex-col gap-5">
        {eventItems.map((item, index) => (
          <EventItem key={index} item={item} />
        ))}
      </div>
    </div>
  );
};
