import React, { useEffect, useState } from "react";

/**
 * Props for the CountDownTimer component.
 */
export type CountdownTimerProps = {
  /**
   * The final date string in "YYYY-MM-DD" format.
   * For example: "2025-12-12".
   */
  finalDate: string;
};

/**
 * CountDownTimer Component
 *
 * This component renders a countdown timer that updates every second until the target date.
 * The finalDate prop is in "YYYY-MM-DD" format, and we append "T00:00:00"
 * to create a valid ISO string for the Date constructor.
 *
 * Each time unit (days, hours, minutes, seconds) is split into individual digits, displayed
 * in boxes with a partial overlay to visualize remaining time as a percentage.
 * The layout uses flex-wrap and responsive classes to adapt to various container widths.
 */
export const CountDownTimer: React.FC<CountdownTimerProps> = ({ finalDate }) => {
  // State variables to hold the remaining time components.
  const [days, setDays] = useState(0);
  const [hours, setHours] = useState(0);
  const [minutes, setMinutes] = useState(0);
  const [seconds, setSeconds] = useState(0);

  // Create a Date object for midnight of the specified final date.
  const targetDate = new Date(finalDate + "T00:00:00");

  /**
   * Calculates the remaining time as a percentage of the target date's epoch time.
   * This is used for the partial overlay height in each digit box.
   */
  const calculateRemainingPercentage = (): string => {
    const now = new Date();
    const difference = targetDate.getTime() - now.getTime();
    // If the target date is in the future, difference will be positive; otherwise 0 or negative.
    const elapsedPercentage = (difference / targetDate.getTime()) * 100;
    const remainingPercentage = (100 - elapsedPercentage).toFixed(2);
    // Clamp the percentage between 0% and 100% to avoid negative/overshoot.
    const clamped = Math.max(0, Math.min(100, parseFloat(remainingPercentage)));
    return `${clamped}%`;
  };

  /**
   * Formats a number into an array of individual digits (at least two).
   *
   * @param num - The number to format.
   * @returns An array of digits.
   */
  const formatNumber = (num: number): number[] => {
    const formattedNumber = num.toString().padStart(2, "0");
    return formattedNumber.split("").map(Number);
  };

  useEffect(() => {
    // Set up an interval to update the countdown every second.
    const interval = setInterval(() => {
      const now = new Date();
      const difference = targetDate.getTime() - now.getTime();

      // Calculate the remaining time components.
      const d = Math.floor(difference / (1000 * 60 * 60 * 24));
      const h = Math.floor((difference % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
      const m = Math.floor((difference % (1000 * 60 * 60)) / (1000 * 60));
      const s = Math.floor((difference % (1000 * 60)) / 1000);

      setDays(d);
      setHours(h);
      setMinutes(m);
      setSeconds(s);
    }, 1000);

    // Clean up the interval on component unmount.
    return () => clearInterval(interval);
  }, [targetDate]);

  /**
   * A helper component that renders one or two digits (like "0" and "1" for "01")
   * with a partial overlay representing the remaining time percentage.
   */
  const DigitBox: React.FC<{ digit: number }> = ({ digit }) => {
    return (
      <div className="timer-box relative overflow-hidden rounded-lg">
        <span className="
          flex items-center justify-center
          h-8 w-[24px]        /* Base size */
          sm:h-10 sm:w-[28px]  /* Scale up on small screens */
          md:h-12 md:w-[32px]  /* Scale further on medium+ screens */
          rounded-lg
          bg-black dark:bg-boxdark
          px-0
          text-lg sm:text-xl md:text-2xl
          font-black leading-[1.1]
          text-white
        ">
          {digit}
        </span>
        {/* Partial overlay to indicate remaining time */}
        <span
          className="absolute bottom-0 left-0 block w-full bg-[#000]/20"
          style={{ height: calculateRemainingPercentage() }}
        />
      </div>
    );
  };

  /**
   * Renders a group of digits (for days, hours, minutes, or seconds) plus a label.
   */
  const TimeGroup: React.FC<{ value: number; label: string }> = ({ value, label }) => {
    const digits = formatNumber(value);
    return (
      <div className="flex flex-col items-center mb-3 sm:mb-0">
        <div className="mb-2 flex flex-nowrap gap-0.5">
          {digits.map((digit, index) => (
            <DigitBox key={index} digit={digit} />
          ))}
        </div>
        <span className="block text-center text-sm sm:text-base font-medium">{label}</span>
      </div>
    );
  };

  return (
    <div className="
      flex
      items-center justify-center
      gap-1 sm:gap-2 md:gap-3
    ">
      <TimeGroup value={days} label="Days" />
      <TimeGroup value={hours} label="Hours" />
      <TimeGroup value={minutes} label="Minutes" />
      <TimeGroup value={seconds} label="Seconds" />
    </div>
  );
};
