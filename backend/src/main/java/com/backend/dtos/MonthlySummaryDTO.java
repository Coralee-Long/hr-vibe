package com.backend.dtos;

import com.backend.utils.DateUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO representing a monthly summary.
 *
 * This record contains the first day of the month and an embedded {@code BaseSummaryDTO}.
 */
public record MonthlySummaryDTO(
    @NotBlank
    String id, // Unique MongoDB ID

    @NotNull
    String firstDay, // First day of the month (YYYY-MM-DD)

    @NotNull
    BaseSummaryDTO summary // Embedded validated summary data
) {
    /**
     * Converts a {@link com.backend.models.MonthlySummary} record to a {@link MonthlySummaryDTO}.
     *
     * @param model the MonthlySummary model.
     * @return a new MonthlySummaryDTO populated with data from the model.
     */
    public static MonthlySummaryDTO fromModel(com.backend.models.MonthlySummary model) {
        return new MonthlySummaryDTO(
            model.id(),
            DateUtils.formatLocalDate(model.firstDay()), // Use DateUtils to format the date
            BaseSummaryDTO.fromModel(model.summary())
        );
    }
}
