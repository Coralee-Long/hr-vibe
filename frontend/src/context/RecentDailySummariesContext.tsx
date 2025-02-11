import { createContext, useContext, useState, ReactNode } from "react";
import { RecentDailySummariesDTO } from "@/types/RecentDailySummariesDTO";
import { mockRecentDailySummaries } from "@/mocks/mockRecentDailySummaries.ts";

// Define the shape of the context
type RecentDailySummariesContextType = {
  summaries: RecentDailySummariesDTO;
  setSummaries: (summaries: RecentDailySummariesDTO) => void;
};

// Create the context with default values
const RecentDailySummariesContext = createContext<
  RecentDailySummariesContextType | undefined
>(undefined);

// Provider component
export const RecentDailySummariesProvider = ({
  children,
}: {
  children: ReactNode;
}) => {
  const [summaries, setSummaries] = useState<RecentDailySummariesDTO>(
    mockRecentDailySummaries
  );

  return (
    <RecentDailySummariesContext.Provider value={{ summaries, setSummaries }}>
      {children}
    </RecentDailySummariesContext.Provider>
  );
};

// Custom hook to use the context
export const useRecentDailySummaries = (): RecentDailySummariesContextType => {
  const context = useContext(RecentDailySummariesContext);
  if (!context) {
    throw new Error(
      "useRecentDailySummaries must be used within a RecentDailySummariesProvider"
    );
  }
  return context;
};
