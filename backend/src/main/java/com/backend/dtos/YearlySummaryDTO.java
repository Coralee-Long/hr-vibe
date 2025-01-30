package com.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record YearlySummaryDTO(
    @NotBlank String id,

    @NotNull LocalDate firstDay, // First day of the year (YYYY-01-01)

    @NotNull BaseSummaryDTO summary // Embedded validated summary data
) {}
