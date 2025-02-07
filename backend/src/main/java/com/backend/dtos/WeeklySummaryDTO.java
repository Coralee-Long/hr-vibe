package com.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * DTO representing a weekly summary.
 *
 * Contains the starting day of the week and the embedded {@code BaseSummaryDTO} data.
 */
public record WeeklySummaryDTO(
    @NotBlank
    String id, // Unique MongoDB ID

    @NotNull
    String firstDay, // Start date of the week (YYYY-MM-DD)

    @NotNull
    BaseSummaryDTO summary // Embedded validated summary data
) {
    /**
     * Converts a {@link com.backend.models.WeeklySummary} record to a {@link WeeklySummaryDTO}.
     *
     * @param model the WeeklySummary model.
     * @return a new WeeklySummaryDTO populated with values from the model.
     */
    public static WeeklySummaryDTO fromModel(com.backend.models.WeeklySummary model) {
        return new WeeklySummaryDTO(
            model.id(),
            model.firstDay().toString(), // Convert LocalDate to String
            BaseSummaryDTO.fromModel(model.summary())
        );
    }
}
