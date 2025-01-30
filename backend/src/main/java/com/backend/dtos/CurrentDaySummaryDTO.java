package com.backend.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record CurrentDaySummaryDTO(
    @NotBlank String id,

    @NotNull LocalDate day, // Garmin provides "day", not "date"

    @NotNull BaseSummaryDTO summary // Embedded validated summary data
) {}
