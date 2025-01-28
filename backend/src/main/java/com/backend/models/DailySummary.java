package com.backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "daily_summary")
public record DailySummary(
    @Id String id, // MongoDB-generated unique identifier
    int step_goal,
    int hr_max,
    double distance,
    int hydration_intake,
    String intensity_time_goal,
    int calories_total,
    String description,
    int calories_goal,
    int sweat_loss,
    double floors_up,
    int hydration_goal,
    String day, // The date field in "YYYY-MM-DD" format
    int stress_avg,
    int bb_charged,
    double rr_max,
    int calories_bmr,
    int bb_min,
    double rr_min,
    int bb_max,
    double spo2_avg,
    double spo2_min,
    int steps,
    double floors_down,
    Integer calories_consumed, // Nullable
    double rr_waking_avg,
    int calories_active,
    double floors_goal,
    int hr_min,
    String vigorous_activity_time,
    String moderate_activity_time,
    int rhr
) {}
