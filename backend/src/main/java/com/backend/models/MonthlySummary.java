package com.backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "monthly_summaries") // Stores monthly aggregated data
public record MonthlySummary(
    @Id String id, // Unique MongoDB ID
    LocalDate firstDay, // First day of the month (YYYY-MM-01)
    BaseSummary summary // Embedded summary data
) {}
