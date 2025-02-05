package com.backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "weekly_summaries") // Stores weekly aggregated data
public record WeeklySummary(
    @Id String id, // Unique MongoDB ID
    LocalDate firstDay, // Start date of the week (YYYY-MM-DD)
    BaseSummary summary // Embedded summary data
) {}