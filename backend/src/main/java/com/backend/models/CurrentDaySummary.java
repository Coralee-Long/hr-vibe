package com.backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "current_day_summaries") // Stores the latest day's summary
public record CurrentDaySummary(
    @Id String id, // Unique MongoDB ID
    LocalDate day, // Specific day for the summary (YYYY-MM-DD)
    BaseSummary summary // Embedded summary data
) {}
