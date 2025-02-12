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
