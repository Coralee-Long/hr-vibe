package com.backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "monthly_summaries") // Stores monthly aggregated data
public record MonthlySummary(
    @Id String id,
    LocalDate firstDay, // First day of the month (YYYY-MM-01)
    Double hrMax,
    Double caloriesActiveAvg,
    Double hydrationIntake,
    String remSleepMax,
    String intensityTimeGoal,
    Double caloriesAvg,
    Double hydrationAvg,
    Integer caloriesGoal,
    Double sweatLoss,
    Double rhrMax,
    Double inactiveHrAvg,
    Double inactiveHrMin,
    Integer stepsGoal,
    Double caloriesBmrAvg,
    Double floors,
    Double caloriesConsumedAvg,
    Integer hydrationGoal,
    String remSleepAvg,
    String remSleepMin,
    Integer stressAvg,
    Double rrMax,
    Integer bbMin,
    Double inactiveHrMax,
    Double weightMin,
    Double sweatLossAvg,
    String sleepAvg,
    String sleepMin,
    Double rrMin,
    Integer bbMax,
    Double spo2Avg,
    Double spo2Min,
    Double weightAvg,
    Double activitiesDistance,
    Integer steps,
    Double rrWakingAvg,
    Double floorsGoal,
    Double weightMax,
    Double hrAvg,
    Double hrMin,
    String vigorousActivityTime,
    Integer activities,
    String moderateActivityTime,
    String intensityTime,
    Double rhrAvg,
    Double rhrMin,
    String sleepMax,
    Double activitiesCalories
) {}
