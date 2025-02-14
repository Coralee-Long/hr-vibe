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
 * The finalDate prop is provided in the "YYYY-MM-DD" format and is processed by appending "T00:00:00"
 * to create a valid full ISO string for the Date constructor.
 *
 * The timer displays the remaining days, hours, minutes, and seconds. Each time unit is split into individual
 * digits which are rendered with a dynamic overlay indicating the remaining percentage.
 */
export const CountDownTimer: React.FC<CountdownTimerProps> = ({ finalDate }) => {
  // State variables to hold the remaining time components.
  const [days, setDays] = useState(0);
  const [hours, setHours] = useState(0);
  const [minutes, setMinutes] = useState(0);
  const [seconds, setSeconds] = useState(0);

  /**
   * Process the finalDate prop by appending "T00:00:00" to ensure a full ISO format string.
   * This creates a Date object representing midnight of the specified final date.
   */
  const targetDate = new Date(finalDate + "T00:00:00");

  /**
   * Calculates the remaining time as a percentage.
   *
   * Note: This function computes the percentage based on the ratio of the time remaining
   * to the total target time in milliseconds. Adjust this calculation if a more specific
   * percentage (e.g., relative to a starting date) is needed.
   *
   * @returns {string} The remaining percentage formatted as a string with a "%" suffix.
   */
  const calculateRemainingPercentage = (): string => {
    const now = new Date();
    const difference = targetDate.getTime() - now.getTime();
    const elapsedPercentage = (difference / targetDate.getTime()) * 100;
    const remainingPercentage = (100 - elapsedPercentage).toFixed(2);
    return `${remainingPercentage}%`;
  };

  /**
   * Formats a number into an array of individual digits.
   * Ensures that the number is represented with at least two digits.
   *
   * @param {number} num - The number to format.
   * @returns {number[]} An array of digits.
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

  return (
    <div className="flex flex-no-wrap gap-3">
      {/* Days Countdown */}
      <div>
        <div className="mb-3 flex items-center gap-1">
          {formatNumber(days).map((digit, index) => (
            <div key={index} className="timer-box relative overflow-hidden rounded-lg">
              <span className="flex h-14 w-[45px] items-center justify-center rounded-lg bg-black px-3 text-xl font-black leading-[1.35] text-white dark:bg-boxdark lg:text-3xl xl:text-[40px]">
                {digit}
              </span>
              <span
                className="absolute bottom-0 left-0 block w-full bg-[#000]/20"
                style={{ height: calculateRemainingPercentage() }}
              ></span>
            </div>
          ))}
        </div>
        <span className="block text-center font-medium">Days</span>
      </div>

      {/* Hours Countdown */}
      <div>
        <div className="mb-3 flex items-center gap-1">
          {formatNumber(hours).map((digit, index) => (
            <div key={index} className="timer-box relative overflow-hidden rounded-lg">
              <span className="flex h-14 w-[45px] items-center justify-center rounded-lg bg-black px-3 text-xl font-black leading-[1.35] text-white dark:bg-boxdark lg:text-3xl xl:text-[40px]">
                {digit}
              </span>
              <span
                className="absolute bottom-0 left-0 block w-full bg-[#000]/20"
                style={{ height: calculateRemainingPercentage() }}
              ></span>
            </div>
          ))}
        </div>
        <span className="block text-center font-medium">Hours</span>
      </div>

      {/* Minutes Countdown */}
      <div>
        <div className="mb-3 flex items-center gap-1">
          {formatNumber(minutes).map((digit, index) => (
            <div key={index} className="timer-box relative overflow-hidden rounded-lg">
              <span className="flex h-14 w-[45px] items-center justify-center rounded-lg bg-black px-3 text-xl font-black leading-[1.35] text-white dark:bg-boxdark lg:text-3xl xl:text-[40px]">
                {digit}
              </span>
              <span
                className="absolute bottom-0 left-0 block w-full bg-[#000]/20"
                style={{ height: calculateRemainingPercentage() }}
              ></span>
            </div>
          ))}
        </div>
        <span className="block text-center font-medium">Minutes</span>
      </div>

      {/* Seconds Countdown */}
      <div>
        <div className="mb-3 flex items-center gap-1">
          {formatNumber(seconds).map((digit, index) => (
            <div key={index} className="timer-box relative overflow-hidden rounded-lg">
              <span className="flex h-14 w-[45px] items-center justify-center rounded-lg bg-black px-3 text-xl font-black leading-[1.35] text-white dark:bg-boxdark lg:text-3xl xl:text-[40px]">
                {digit}
              </span>
              <span
                className="absolute bottom-0 left-0 block w-full bg-[#000]/20"
                style={{ height: calculateRemainingPercentage() }}
              ></span>
            </div>
          ))}
        </div>
        <span className="block text-center font-medium">Seconds</span>
      </div>
    </div>
  );
};
