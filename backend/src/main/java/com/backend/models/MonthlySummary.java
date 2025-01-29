package com.backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "monthly_summaries") // Stores monthly aggregated data
public record MonthlySummary(
    @Id String id, // Unique MongoDB ID
    LocalDate firstDay, // First day of the month (YYYY-MM-01)

    Double hrMax, // Maximum heart rate recorded during the month
    Double caloriesActiveAvg, // Average active calories burned
    Double hydrationIntake, // Total hydration intake (ml)
    String remSleepMax, // Maximum REM sleep duration (HH:MM:SS)
    String intensityTimeGoal, // Monthly intensity time goal (HH:MM:SS)
    Double caloriesAvg, // Average daily calories burned
    Double hydrationAvg, // Average daily hydration intake
    Integer caloriesGoal, // Monthly calorie burn goal
    Double sweatLoss, // Total sweat loss during the month

    Double rhrMax, // Maximum resting heart rate
    Double inactiveHrAvg, // Monthly average inactive heart rate
    Double inactiveHrMin, // Monthly minimum inactive heart rate
    Integer stepsGoal, // Monthly step goal
    Double caloriesBmrAvg, // Monthly average Basal Metabolic Rate (BMR)
    Double floors, // Total floors climbed
    Double caloriesConsumedAvg, // Monthly average calories consumed
    Integer hydrationGoal, // Monthly hydration goal (ml)

    String remSleepAvg, // Monthly average REM sleep duration (HH:MM:SS)
    String remSleepMin, // Monthly minimum REM sleep duration (HH:MM:SS)
    Integer stressAvg, // Monthly average stress level
    Double rrMax, // Maximum respiration rate (RR)
    Integer bbMin, // Minimum body battery during the month
    Double inactiveHrMax, // Maximum inactive heart rate
    Double weightMin, // Minimum weight recorded in the month
    Double sweatLossAvg, // Average sweat loss (ml)

    String sleepAvg, // Monthly average sleep duration (HH:MM:SS)
    String sleepMin, // Monthly minimum sleep duration (HH:MM:SS)
    Double rrMin, // Minimum respiration rate (RR)
    Integer bbMax, // Maximum body battery during the month
    Double spo2Avg, // Monthly average SpO2 (oxygen saturation %)
    Double spo2Min, // Monthly minimum SpO2
    Double weightAvg, // Monthly average weight recorded
    Double activitiesDistance, // Total distance traveled in activities (km)

    Integer steps, // Total steps taken during the month
    Double rrWakingAvg, // Monthly average waking respiration rate (RR)
    Double floorsGoal, // Monthly floors goal
    Double weightMax, // Maximum weight recorded
    Double hrAvg, // Monthly average heart rate
    Double hrMin, // Monthly minimum heart rate
    String vigorousActivityTime, // Monthly total time in vigorous activity (HH:MM:SS)

    Integer activities, // Total number of activities logged
    String moderateActivityTime, // Monthly total time in moderate activity (HH:MM:SS)
    String intensityTime, // Monthly total intensity minutes (HH:MM:SS)
    Double rhrAvg, // Monthly average resting heart rate
    Double rhrMin, // Monthly minimum resting heart rate
    String sleepMax, // Maximum sleep duration during the month (HH:MM:SS)
    Double activitiesCalories // Total calories burned during activities
) {}
