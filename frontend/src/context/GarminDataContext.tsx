import { createContext, useContext, useState, ReactNode } from "react";

import GarminDataService from "@/api/services/garminDataService";
import { CurrentDaySummaryDTO } from "@/types/CurrentDaySummaryDTO";
import { WeeklySummaryDTO } from "@/types/WeeklySummaryDTO";
import { MonthlySummaryDTO } from "@/types/MonthlySummaryDTO";
import { YearlySummaryDTO } from "@/types/YearlySummaryDTO";

/**
 * Type definition for the Garmin Data Context.
 * This includes the summaries for days, weeks, months, and years,
 * along with methods to fetch each type of summary.
 */
interface GarminDataContextProps {
  // Array of daily summary objects.
  daySummaries: CurrentDaySummaryDTO[];
  // Array of weekly summary objects.
  weekSummaries: WeeklySummaryDTO[];
  // Array of monthly summary objects.
  monthSummaries: MonthlySummaryDTO[];
  // Array of yearly summary objects.
  yearSummaries: YearlySummaryDTO[];
  /**
   * Fetch the day summaries.
   * @param limit - (Optional) Maximum number of day summaries to fetch.
   */
  fetchDaySummaries: (limit?: number) => Promise<void>;
  /**
   * Fetch the week summaries.
   * @param limit - (Optional) Maximum number of week summaries to fetch.
   */
  fetchWeekSummaries: (limit?: number) => Promise<void>;
  /**
   * Fetch the month summaries.
   * @param year - (Optional) Filter summaries for a specific year.
   */
  fetchMonthSummaries: (year?: number) => Promise<void>;
  /**
   * Fetch the year summaries.
   */
  fetchYearSummaries: () => Promise<void>;
}

// Create a context for Garmin data with an initial undefined value.
// Consumers must be wrapped in the GarminDataProvider.
const GarminDataContext = createContext<GarminDataContextProps | undefined>(undefined);

/**
 * GarminDataProvider is a context provider component that manages the state
 * for Garmin data summaries and provides functions to fetch the data.
 *
 * @param children - The React child components that need access to Garmin data.
 * @returns A provider component wrapping the children.
 */
export const GarminDataProvider = ({ children }: { children: ReactNode }) => {
  // Initialize state for each type of summary.
  const [daySummaries, setDaySummaries] = useState<CurrentDaySummaryDTO[]>([]);
  const [weekSummaries, setWeekSummaries] = useState<WeeklySummaryDTO[]>([]);
  const [monthSummaries, setMonthSummaries] = useState<MonthlySummaryDTO[]>([]);
  const [yearSummaries, setYearSummaries] = useState<YearlySummaryDTO[]>([]);

  /**
   * Fetches daily summaries from the GarminDataService.
   * Uses a default limit of 30 if no limit is provided.
   */
  const fetchDaySummaries = async (limit: number = 30) => {
    try {
      const data = await GarminDataService.getAllDaySummaries(limit);
      setDaySummaries(data);
    } catch (error) {
      console.error("Error fetching day summaries:", error);
    }
  };

  /**
   * Fetches weekly summaries from the GarminDataService.
   * Uses a default limit of 30 if no limit is provided.
   */
  const fetchWeekSummaries = async (limit: number = 30) => {
    try {
      const data = await GarminDataService.getAllWeekSummaries(limit);
      setWeekSummaries(data);
    } catch (error) {
      console.error("Error fetching week summaries:", error);
    }
  };

  /**
   * Fetches monthly summaries from the GarminDataService.
   * Can optionally filter summaries by a specific year.
   */
  const fetchMonthSummaries = async (year?: number) => {
    try {
      const data = await GarminDataService.getMonthSummaries(year);
      setMonthSummaries(data);
    } catch (error) {
      console.error("Error fetching month summaries:", error);
    }
  };

  /**
   * Fetches yearly summaries from the GarminDataService.
   */
  const fetchYearSummaries = async () => {
    try {
      const data = await GarminDataService.getYearSummaries();
      setYearSummaries(data);
    } catch (error) {
      console.error("Error fetching year summaries:", error);
    }
  };

  // The context provider passes down the state and fetch functions.
  return (
    <GarminDataContext.Provider
      value={{
        daySummaries,
        weekSummaries,
        monthSummaries,
        yearSummaries,
        fetchDaySummaries,
        fetchWeekSummaries,
        fetchMonthSummaries,
        fetchYearSummaries,
      }}
    >
      {children}
    </GarminDataContext.Provider>
  );
};

/**
 * Custom hook to access the GarminDataContext.
 * Ensures that the hook is used within a GarminDataProvider.
 *
 * @returns The GarminDataContext value.
 * @throws Error if the hook is used outside the GarminDataProvider.
 */
export const useGarminData = (): GarminDataContextProps => {
  const context = useContext(GarminDataContext);
  if (!context) {
    throw new Error("useGarminData must be used within a GarminDataProvider");
  }
  return context;
};
