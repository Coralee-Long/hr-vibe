import { createContext, useContext, useState, ReactNode, useCallback, useEffect } from "react";

import GarminDataService from "@/api/services/garminDataService";
import { RecentDailySummariesDTO } from "@/types/RecentDailySummariesDTO";
import { CurrentDaySummaryDTO } from "@/types/CurrentDaySummaryDTO";

/**
 * Type definition for the Recent Daily Summaries context.
 * Provides:
 * - summaries: Recent daily summaries data (or null if not loaded).
 * - currentDayData: Data for the current day (or null if not loaded).
 * - latestDateAvailable: The latest available date as a string (or null).
 * - fetchRecentDailySummaries: Function to fetch summaries based on a reference date.
 * - fetchCurrentDayData: Function to fetch data for a specific day.
 */
interface RecentDailySummariesContextProps {
  summaries: RecentDailySummariesDTO | null;
  currentDayData: CurrentDaySummaryDTO | null;
  latestDateAvailable: string | null;
  /**
   * Fetch recent daily summaries from the API.
   * @param referenceDate - The date to base the summaries on.
   */
  fetchRecentDailySummaries: (referenceDate: string) => Promise<void>;
  /**
   * Fetch current day data from the API.
   * @param day - The specific day (as a string) for which to fetch data.
   */
  fetchCurrentDayData: (day: string) => Promise<void>;
}

// Create a context with an initial undefined value.
const RecentDailySummariesContext = createContext<RecentDailySummariesContextProps | undefined>(undefined);

/**
 * RecentDailySummariesProvider is a context provider component that manages the state
 * for recent daily summaries, current day data, and the latest date available.
 * It provides functions to fetch this data from the GarminDataService.
 *
 * @param children - The React child components that need access to recent daily summaries.
 * @returns A provider component wrapping the children.
 */
export const RecentDailySummariesProvider = ({ children }: { children: ReactNode }) => {
  // State to hold the fetched recent daily summaries (null if not yet fetched).
  const [summaries, setSummaries] = useState<RecentDailySummariesDTO | null>(null);
  // State to hold the current day's summary data (null if not yet fetched).
  const [currentDayData, setCurrentDayData] = useState<CurrentDaySummaryDTO | null>(null);
  // State to hold the latest date available from the summaries (null if not provided).
  const [latestDateAvailable, setLatestDateAvailable] = useState<string | null>(null);

  /**
   * Fetches recent daily summaries based on a reference date.
   * The function is memoized using useCallback to prevent unnecessary re-renders.
   *
   * @param referenceDate - The reference date string used to fetch summaries.
   */
  const fetchRecentDailySummaries = useCallback(async (referenceDate: string) => {
    try {
      // Call the GarminDataService API to get recent daily summaries.
      const data = await GarminDataService.getRecentDailySummaries(referenceDate);
      // console.log("Fetched recent daily summaries data:", data);
      // Update the summaries state.
      setSummaries(data);
      // If the data includes a 'latestDay' property, update latestDateAvailable.
      if (data.latestDay) {
        // console.log("Setting latestDateAvailable to:", data.latestDay);
        setLatestDateAvailable(data.latestDay);
      } else {
        console.warn("latestDay property not found in data");
        setLatestDateAvailable(null);
      }
    } catch (error) {
      console.error("Error fetching recent daily summaries:", error);
    }
  }, []);

  /**
   * Fetches the current day's summary data.
   * The function is memoized using useCallback to optimize rendering.
   *
   * @param day - The specific day (as a string) for which to fetch the summary.
   */
  const fetchCurrentDayData = useCallback(async (day: string) => {
    try {
      // Call the GarminDataService API to get the current day's summary.
      const data = await GarminDataService.getDaySummary(day);
      // Update the currentDayData state.
      setCurrentDayData(data);
    } catch (error) {
      console.error("Error fetching current day data:", error);
    }
  }, []);

  // Log changes to latestDateAvailable for debugging purposes.
  useEffect(() => {
    // console.log("Latest Date Available in context:", latestDateAvailable);
  }, [latestDateAvailable]);

  // Provide the context values (state and fetching functions) to child components.
  return (
    <RecentDailySummariesContext.Provider
      value={{
        summaries,
        currentDayData,
        latestDateAvailable,
        fetchRecentDailySummaries,
        fetchCurrentDayData,
      }}
    >
      {children}
    </RecentDailySummariesContext.Provider>
  );
};

/**
 * Custom hook to consume the RecentDailySummariesContext.
 * Ensures that the hook is used within a RecentDailySummariesProvider.
 *
 * @returns The context value containing recent daily summaries and fetching functions.
 * @throws Error if used outside the RecentDailySummariesProvider.
 */
export const useRecentDailySummaries = (): RecentDailySummariesContextProps => {
  const context = useContext(RecentDailySummariesContext);
  if (!context) {
    throw new Error("useRecentDailySummaries must be used within a RecentDailySummariesProvider");
  }
  return context;
};
