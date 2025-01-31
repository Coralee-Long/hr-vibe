import { createContext, useContext, useState, ReactNode } from "react";
import { CurrentDaySummaryDTO } from "@/types/CurrentDaySummary";
import { mockCurrentDaySummary } from "@/mocks/mockCurrentDaySummary";

// Define the shape of the context
type CurrentDaySummaryContextType = {
  summary: CurrentDaySummaryDTO;
  setSummary: (summary: CurrentDaySummaryDTO) => void;
};

// Create the context with default values
const CurrentDaySummaryContext = createContext<CurrentDaySummaryContextType | undefined>(undefined);

// Provider component
export const CurrentDaySummaryProvider = ({ children }: { children: ReactNode }) => {
  const [summary, setSummary] = useState<CurrentDaySummaryDTO>(mockCurrentDaySummary);

  return (
    <CurrentDaySummaryContext.Provider value={{ summary, setSummary }}>
      {children}
    </CurrentDaySummaryContext.Provider>
  );
};

// Custom hook to use the context
export const useCurrentDaySummary = (): CurrentDaySummaryContextType => {
  const context = useContext(CurrentDaySummaryContext);
  if (!context) {
    throw new Error("useCurrentDaySummary must be used within a CurrentDaySummaryProvider");
  }
  return context;
};
