package com.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CurrentDaySummaryDTO(

    @NotBlank
    String id, // Unique MongoDB ID

    @NotNull
    LocalDate day, // Specific date for the summary (YYYY-MM-DD)

    @NotNull
    BaseSummaryDTO summary // Embedded summary data containing health metrics
) {}

