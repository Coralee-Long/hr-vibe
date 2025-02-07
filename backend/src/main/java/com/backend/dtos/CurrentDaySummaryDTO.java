package com.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * DTO representing a summary for a specific day.
 *
 * This record wraps a {@code BaseSummaryDTO} for detailed health metrics.
 */
public record CurrentDaySummaryDTO(
    @NotBlank
    String id, // Unique MongoDB ID

    @NotNull
    String day, // Specific date for the summary (YYYY-MM-DD)

    @NotNull
    BaseSummaryDTO summary // Embedded summary data containing health metrics
) {
    /**
     * Converts a {@link com.backend.models.CurrentDaySummary} record to a {@link CurrentDaySummaryDTO}.
     *
     * @param model the CurrentDaySummary model.
     * @return a new CurrentDaySummaryDTO with values extracted from the model.
     */
    public static CurrentDaySummaryDTO fromModel(com.backend.models.CurrentDaySummary model) {
        return new CurrentDaySummaryDTO(
            model.id(),
            model.day().toString(), // Convert LocalDate to String
            BaseSummaryDTO.fromModel(model.summary())
        );
    }
}
