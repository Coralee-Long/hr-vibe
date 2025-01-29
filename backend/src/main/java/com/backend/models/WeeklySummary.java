package com.backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "weekly_summaries") // Stores weekly aggregated data
public record WeeklySummary(
    @Id String id, // Unique MongoDB ID
    LocalDate firstDay, // Start date of the week (e.g., 2024-01-01)

    Double hrMax, // Maximum heart rate recorded during the week
    Double caloriesActiveAvg, // Average active calories burned
    Double hydrationIntake, // Total hydration intake (ml)
    String remSleepMax, // Maximum REM sleep duration (HH:MM:SS)
    String intensityTimeGoal, // Weekly intensity time goal (HH:MM:SS)
    Double caloriesAvg, // Average daily calories burned
    Double hydrationAvg, // Average daily hydration intake
    Integer caloriesGoal, // Weekly calorie burn goal
    Double sweatLoss, // Total sweat loss during the week

    Double rhrMax, // Maximum resting heart rate
    Double inactiveHrAvg, // Weekly average inactive heart rate
    Double inactiveHrMin, // Weekly minimum inactive heart rate
    Integer stepsGoal, // Weekly step goal
    Double caloriesBmrAvg, // Weekly average Basal Metabolic Rate (BMR)
    Double floors, // Total floors climbed
    Double caloriesConsumedAvg, // Weekly average calories consumed
    Integer hydrationGoal, // Weekly hydration goal (ml)

    String remSleepAvg, // Weekly average REM sleep duration (HH:MM:SS)
    String remSleepMin, // Weekly minimum REM sleep duration (HH:MM:SS)
    Integer stressAvg, // Weekly average stress level
    Double rrMax, // Maximum respiration rate (RR)
    Integer bbMin, // Minimum body battery during the week
    Double inactiveHrMax, // Maximum inactive heart rate
    Double weightMin, // Minimum weight recorded in the week
    Double sweatLossAvg, // Average sweat loss (ml)

    String sleepAvg, // Weekly average sleep duration (HH:MM:SS)
    String sleepMin, // Weekly minimum sleep duration (HH:MM:SS)
    Double rrMin, // Minimum respiration rate (RR)
    Integer bbMax, // Maximum body battery during the week
    Double spo2Avg, // Weekly average SpO2 (oxygen saturation %)
    Double spo2Min, // Weekly minimum SpO2
    Double weightAvg, // Weekly average weight recorded
    Double activitiesDistance, // Total distance traveled in activities (km)

    Integer steps, // Total steps taken during the week
    Double rrWakingAvg, // Weekly average waking respiration rate (RR)
    Double floorsGoal, // Weekly floors goal
    Double weightMax, // Maximum weight recorded
    Double hrAvg, // Weekly average heart rate
    Double hrMin, // Weekly minimum heart rate
    String vigorousActivityTime, // Weekly total time in vigorous activity (HH:MM:SS)

    Integer activities, // Total number of activities logged
    String moderateActivityTime, // Weekly total time in moderate activity (HH:MM:SS)
    String intensityTime, // Weekly total intensity minutes (HH:MM:SS)
    Double rhrAvg, // Weekly average resting heart rate
    Double rhrMin, // Weekly minimum resting heart rate
    String sleepMax, // Maximum sleep duration during the week (HH:MM:SS)
    Double activitiesCalories // Total calories burned during activities
) {}
