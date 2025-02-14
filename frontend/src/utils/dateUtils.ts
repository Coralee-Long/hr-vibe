// src/utils/dateUtils.ts

/**
 * Generates an array of formatted date strings representing the last 7 days.
 *
 * @param referenceDate - The latest date as a string (e.g., "2025-01-24").
 * @returns An array of 7 strings, each formatted as "DD.MM", representing the last 7 days.
 */
export const generateLast7DaysCategories = (referenceDate: string): string[] => {
  const latestDate = new Date(referenceDate);
  return Array.from({ length: 7 }, (_, i) => {
    const date = new Date(latestDate);
    // Adjust so that the leftmost label is 6 days before the reference date.
    date.setDate(date.getDate() - (6 - i));
    const day = date.getDate().toString().padStart(2, "0");
    const month = (date.getMonth() + 1).toString().padStart(2, "0");
    return `${day}.${month}`;
  });
};

/**
 * Formats a date string from "YYYY-MM-DD" format to "DD Month YYYY" format.
 *
 * @param dateStr - The date string in "YYYY-MM-DD" format (e.g., "2025-04-13").
 * @returns The formatted date string (e.g., "13 April 2025").
 */
export function formatDate(dateStr: string): string {
  // Split the date string into its components
  const [year, month, day] = dateStr.split('-');

  // Array of month names for conversion, zero-indexed (i.e., January is at index 0)
  const monthNames = [
    'January', 'February', 'March', 'April', 'May', 'June',
    'July', 'August', 'September', 'October', 'November', 'December'
  ];

  // Convert month to a zero-indexed number and parse day as integer
  const monthIndex = parseInt(month, 10) - 1;
  const dayNumber = parseInt(day, 10);

  // Return the formatted date string
  return `${dayNumber} ${monthNames[monthIndex]} ${year}`;
}

