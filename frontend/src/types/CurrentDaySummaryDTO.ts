import { BaseSummaryDTO } from "./BaseSummaryDTO";

/**
 * DTO representing a summary for a specific day.
 * This type wraps a BaseSummaryDTO for detailed health metrics.
 */
export type CurrentDaySummaryDTO = {
  // Unique MongoDB ID
  id: string;
  // Specific date for the summary (YYYY-MM-DD)
  day: string;
  // Embedded summary data containing health metrics
  summary: BaseSummaryDTO;
}
