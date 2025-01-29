package com.backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "recent_daily_summaries") // Stores the last 7 days of daily data
public record RecentDailySummaries(
    @Id String id, // Unique MongoDB ID
    List<CurrentDaySummary> last7Days // Array of daily summaries (latest 7 days)
) {}
