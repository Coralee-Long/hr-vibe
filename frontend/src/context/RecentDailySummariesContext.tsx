// RecentDailySummariesContext.tsx
import { createContext, useContext, useState, ReactNode, useCallback, useEffect } from "react";
import GarminDataService from "@/api/services/garminDataService";
import { RecentDailySummariesDTO } from "@/types/RecentDailySummariesDTO";
import { CurrentDaySummaryDTO } from "@/types/CurrentDaySummaryDTO";

interface RecentDailySummariesContextProps {
  summaries: RecentDailySummariesDTO | null;
  currentDayData: CurrentDaySummaryDTO | null;
  latestDateAvailable: string | null;
  fetchRecentDailySummaries: (referenceDate: string) => Promise<void>;
  fetchCurrentDayData: (day: string) => Promise<void>;
}

const RecentDailySummariesContext = createContext<RecentDailySummariesContextProps | undefined>(undefined);

export const RecentDailySummariesProvider = ({ children }: { children: ReactNode }) => {
  const [summaries, setSummaries] = useState<RecentDailySummariesDTO | null>(null);
  const [currentDayData, setCurrentDayData] = useState<CurrentDaySummaryDTO | null>(null);
  const [latestDateAvailable, setLatestDateAvailable] = useState<string | null>(null);

  const fetchRecentDailySummaries = useCallback(async (referenceDate: string) => {
    try {
      const data = await GarminDataService.getRecentDailySummaries(referenceDate);
      console.log("Fetched recent daily summaries data:", data);
      setSummaries(data);
      if (data.latestDay) {
        console.log("Setting latestDateAvailable to:", data.latestDay);
        setLatestDateAvailable(data.latestDay);
      } else {
        console.warn("latestDay property not found in data");
        setLatestDateAvailable(null);
      }
    } catch (error) {
      console.error("Error fetching recent daily summaries:", error);
    }
  }, []);

  const fetchCurrentDayData = useCallback(async (day: string) => {
    try {
      const data = await GarminDataService.getDaySummary(day);
      setCurrentDayData(data);
    } catch (error) {
      console.error("Error fetching current day data:", error);
    }
  }, []);

  // Log whenever latestDateAvailable changes.
  useEffect(() => {
    console.log("Latest Date Available in context:", latestDateAvailable);
  }, [latestDateAvailable]);

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

export const useRecentDailySummaries = (): RecentDailySummariesContextProps => {
  const context = useContext(RecentDailySummariesContext);
  if (!context) {
    throw new Error("useRecentDailySummaries must be used within a RecentDailySummariesProvider");
  }
  return context;
};
