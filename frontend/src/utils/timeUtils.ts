/**
 * Converts a time string in "HH:MM:SS" format to minutes.
 *
 * If the provided time string is undefined or empty, this function returns 0.
 *
 * @param timeStr - A time string (e.g., "00:30:00").
 * @returns The total number of minutes.
 */
export const convertTimeToMinutes = (timeStr?: string): number => {
  if (!timeStr) return 0;
  const [hours, minutes, seconds] = timeStr.split(":").map(Number);
  return hours * 60 + minutes + seconds / 60;
};

/**
 * Calculates the percentage breakdown of moderate and vigorous activity times.
 *
 * Given the moderate and vigorous activity times in "HH:MM:SS" format, this function computes
 * the percentage each activity type contributes relative to the total activity time.
 *
 * @param moderateTime - The duration of moderate activity (e.g., "00:30:00").
 * @param vigorousTime - The duration of vigorous activity (e.g., "01:00:00").
 * @returns An object containing the percentages for moderate and vigorous activity.
 */
export const calculateActivityPercentages = (
  moderateTime: string,
  vigorousTime: string
): { moderatePercentage: number; vigorousPercentage: number } => {
  const moderateMinutes = convertTimeToMinutes(moderateTime);
  const vigorousMinutes = convertTimeToMinutes(vigorousTime);
  const totalMinutes = moderateMinutes + vigorousMinutes;

  if (totalMinutes === 0) {
    return { moderatePercentage: 0, vigorousPercentage: 0 };
  }

  const moderatePercentage = (moderateMinutes / totalMinutes) * 100;
  const vigorousPercentage = (vigorousMinutes / totalMinutes) * 100;

  return {
    moderatePercentage: Number(moderatePercentage.toFixed(4)),
    vigorousPercentage: Number(vigorousPercentage.toFixed(4)),
  };
};

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
