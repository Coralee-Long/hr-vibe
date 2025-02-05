package com.backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "yearly_summaries") // Stores yearly aggregated data
public record YearlySummary(
    @Id String id, // Unique MongoDB ID
    LocalDate firstDay, // First day of the year (YYYY-01-01)
    BaseSummary summary // Embedded summary data
) {}
