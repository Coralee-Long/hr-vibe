import { createContext, useContext, useState, ReactNode } from "react";
import { CurrentDaySummaryDTO } from "@/types/CurrentDaySummary";
import { mockCurrentDaySummary } from "@/mocks/mockCurrentDaySummary.ts";

// Define the shape of the context
type CurrentDaySummaryContextType = {
  currentDayData: CurrentDaySummaryDTO;
  setCurrentDayData: (data: CurrentDaySummaryDTO) => void;
};

// Create the context with default values
const CurrentDaySummaryContext = createContext<
  CurrentDaySummaryContextType | undefined
>(undefined);

// Provider component
export const CurrentDaySummaryProvider = ({
  children,
}: {
  children: ReactNode;
}) => {
  const [currentDayData, setCurrentDayData] = useState<CurrentDaySummaryDTO>(
    mockCurrentDaySummary
  );

  return (
    <CurrentDaySummaryContext.Provider
      value={{ currentDayData, setCurrentDayData }}
    >
      {children}
    </CurrentDaySummaryContext.Provider>
  );
};

// Custom hook to use the context
export const useCurrentDaySummary = (): CurrentDaySummaryContextType => {
  const context = useContext(CurrentDaySummaryContext);
  if (!context) {
    throw new Error(
      "useCurrentDaySummary must be used within a CurrentDaySummaryProvider"
    );
  }
  return context;
};
