package com.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record MonthlySummaryDTO(
    @NotBlank String id,

    @NotNull LocalDate firstDay, // First day of the month (YYYY-MM-01)

    @NotNull BaseSummaryDTO summary // Embedded validated summary data
) {}
