package com.backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record CreateDailySummariesDTO(
    @JsonProperty("daily_summaries") List<DailySummaryDTO> summaries
) {}
