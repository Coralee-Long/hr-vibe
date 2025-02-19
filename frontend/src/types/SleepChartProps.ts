/**
 * Props for the Sleep component.
 *
 * @property loading - Indicates if data is still being fetched.
 * @property referenceDate - The current reference date (e.g., "YYYY-MM-DD").
 * @property categories - Pre-calculated x-axis labels (e.g., last 7 dates formatted as "DD.MM").
 */
export type SleepChartProps = {
  loading: boolean;
  referenceDate: string;
  categories: string[];
};