import { createContext, useContext, useState, ReactNode } from "react";
import GarminDataService from "@/api/services/garminDataService";
import { CurrentDaySummaryDTO } from "@/types/CurrentDaySummaryDTO";
import { WeeklySummaryDTO } from "@/types/WeeklySummaryDTO";
import { MonthlySummaryDTO } from "@/types/MonthlySummaryDTO";
import { YearlySummaryDTO } from "@/types/YearlySummaryDTO";

interface GarminDataContextProps {
  daySummaries: CurrentDaySummaryDTO[];
  weekSummaries: WeeklySummaryDTO[];
  monthSummaries: MonthlySummaryDTO[];
  yearSummaries: YearlySummaryDTO[];
  fetchDaySummaries: (limit?: number) => Promise<void>;
  fetchWeekSummaries: (limit?: number) => Promise<void>;
  fetchMonthSummaries: (year?: number) => Promise<void>;
  fetchYearSummaries: () => Promise<void>;
}

const GarminDataContext = createContext<GarminDataContextProps | undefined>(undefined);

export const GarminDataProvider = ({ children }: { children: ReactNode }) => {
  const [daySummaries, setDaySummaries] = useState<CurrentDaySummaryDTO[]>([]);
  const [weekSummaries, setWeekSummaries] = useState<WeeklySummaryDTO[]>([]);
  const [monthSummaries, setMonthSummaries] = useState<MonthlySummaryDTO[]>([]);
  const [yearSummaries, setYearSummaries] = useState<YearlySummaryDTO[]>([]);

  const fetchDaySummaries = async (limit: number = 30) => {
    try {
      const data = await GarminDataService.getAllDaySummaries(limit);
      setDaySummaries(data);
    } catch (error) {
      console.error("Error fetching day summaries:", error);
    }
  };

  const fetchWeekSummaries = async (limit: number = 30) => {
    try {
      const data = await GarminDataService.getAllWeekSummaries(limit);
      setWeekSummaries(data);
    } catch (error) {
      console.error("Error fetching week summaries:", error);
    }
  };

  const fetchMonthSummaries = async (year?: number) => {
    try {
      const data = await GarminDataService.getMonthSummaries(year);
      setMonthSummaries(data);
    } catch (error) {
      console.error("Error fetching month summaries:", error);
    }
  };

  const fetchYearSummaries = async () => {
    try {
      const data = await GarminDataService.getYearSummaries();
      setYearSummaries(data);
    } catch (error) {
      console.error("Error fetching year summaries:", error);
    }
  };

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

export const useGarminData = (): GarminDataContextProps => {
  const context = useContext(GarminDataContext);
  if (!context) {
    throw new Error("useGarminData must be used within a GarminDataProvider");
  }
  return context;
};
