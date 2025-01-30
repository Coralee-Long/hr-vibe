package com.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record WeeklySummaryDTO(
    @NotBlank String id,

    @NotNull LocalDate firstDay, // Start date of the week (YYYY-MM-DD)

    @NotNull BaseSummaryDTO summary // Embedded validated summary data
) {}
