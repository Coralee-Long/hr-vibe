import { BaseSummaryDTO } from "./BaseSummaryDTO";

/**
 * DTO representing a summary for a specific year.
 * This type wraps a BaseSummaryDTO for detailed health metrics.
 */
export type YearlySummaryDTO = {
  // Unique MongoDB ID
  id: string;
  // Specific date for the summary (YYYY-MM-DD)
  day: string;
  // Embedded summary data containing health metrics
  summary: BaseSummaryDTO;
}