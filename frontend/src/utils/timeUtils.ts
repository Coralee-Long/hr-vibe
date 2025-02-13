/**
 * Converts a time string in "HH:MM:SS" format to hours.
 *
 * @param timeStr - A time string (e.g., "07:30:00").
 * @returns The total number of hours as a decimal.
 */
export const convertTimeToHours = (timeStr: string): number => {
  const [hours, minutes, seconds] = timeStr.split(":").map(Number);
  return hours + minutes / 60 + seconds / 3600;
};

/**
 * Rounds a given number to 2 decimal places.
 *
 * @param value - The number to round.
 * @returns The number rounded to 2 decimal places.
 */
export const roundTo2Decimals = (value: number): number => Math.round(value * 100) / 100;

/**
 * Converts a time string in "HH:MM:SS" format to hours,
 * and rounds the result to 2 decimal places.
 *
 * @param timeStr - A time string (e.g., "07:30:00").
 * @returns The total number of hours as a decimal rounded to 2 decimal places.
 */
export const convertAndRoundTimeToHours = (timeStr: string): number => {
  const hours = convertTimeToHours(timeStr);
  return roundTo2Decimals(hours);
};

/**
 * Computes Non-REM sleep hours for each day by subtracting REM sleep hours from total sleep hours.
 *
 * @param sleepHours - Array of total sleep hours (rounded to 2 decimals).
 * @param remSleepHours - Array of REM sleep hours (rounded to 2 decimals).
 * @returns An array of Non-REM sleep hours, each rounded to 2 decimal places.
 */
export const computeNonRemSleepHours = (sleepHours: number[], remSleepHours: number[]): number[] => {
  return sleepHours.map((total, index) => roundTo2Decimals(total - remSleepHours[index]));
};
