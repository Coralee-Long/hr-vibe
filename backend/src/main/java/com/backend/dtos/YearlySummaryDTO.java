package com.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * DTO representing a yearly summary.
 *
 * Contains the first day of the year and the embedded {@code BaseSummaryDTO} data.
 */
public record YearlySummaryDTO(
    @NotBlank
    String id, // Unique MongoDB ID

    @NotNull
    String firstDay, // First day of the year (YYYY-01-01)

    @NotNull
    BaseSummaryDTO summary // Embedded validated summary data
) {
    /**
     * Converts a {@link com.backend.models.YearlySummary} record to a {@link YearlySummaryDTO}.
     *
     * @param model the YearlySummary model.
     * @return a new YearlySummaryDTO populated with values from the model.
     */
    public static YearlySummaryDTO fromModel(com.backend.models.YearlySummary model) {
        return new YearlySummaryDTO(
            model.id(),
            model.firstDay().toString(), // Convert LocalDate to String
            BaseSummaryDTO.fromModel(model.summary())
        );
    }
}
