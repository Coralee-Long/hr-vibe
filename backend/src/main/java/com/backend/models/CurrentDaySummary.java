package com.backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document(collection = "current_day_summaries") // Stores the latest day's summary
public record CurrentDaySummary(
    @Id String id, // Unique MongoDB ID

    LocalDate date, // The date of the summary

    Double hrMax, // Maximum heart rate recorded
    Double caloriesActiveAvg, // Average active calories burned
    Double hydrationIntake, // Total hydration intake (ml)
    String remSleepMax, // Maximum REM sleep time (HH:MM:SS)
    String intensityTimeGoal, // Target intensity minutes (HH:MM:SS)
    Double caloriesAvg, // Average daily calories burned
    Double hydrationAvg, // Average daily hydration intake
    Integer caloriesGoal, // Goal for daily calorie burn
    Double sweatLoss, // Total sweat loss (ml)

    Double rhrMax, // Maximum resting heart rate
    Double inactiveHrAvg, // Average inactive heart rate
    Double inactiveHrMin, // Minimum inactive heart rate
    Integer stepsGoal, // Daily step goal
    Double caloriesBmrAvg, // Average Basal Metabolic Rate (BMR)
    Double floors, // Floors climbed
    Double caloriesConsumedAvg, // Average calories consumed
    Integer hydrationGoal, // Hydration goal (ml)

    String remSleepAvg, // Average REM sleep duration (HH:MM:SS)
    String remSleepMin, // Minimum REM sleep duration (HH:MM:SS)
    Integer stressAvg, // Average stress level
    Double rrMax, // Maximum respiration rate (RR)
    Integer bbMin, // Minimum body battery
    Double inactiveHrMax, // Maximum inactive heart rate
    Double weightMin, // Minimum weight recorded
    Double sweatLossAvg, // Average sweat loss (ml)

    String sleepAvg, // Average sleep duration (HH:MM:SS)
    String sleepMin, // Minimum sleep duration (HH:MM:SS)
    Double rrMin, // Minimum respiration rate (RR)
    Integer bbMax, // Maximum body battery
    Double spo2Avg, // Average SpO2 (oxygen saturation %)
    Double spo2Min, // Minimum SpO2 (oxygen saturation %)
    Double weightAvg, // Average weight recorded
    Double activitiesDistance, // Distance traveled in activities (km)

    Integer steps, // Steps taken today
    Double rrWakingAvg, // Waking respiration rate (RR)
    Double floorsGoal, // Floors goal
    Double weightMax, // Maximum weight recorded
    Double hrAvg, // Average heart rate
    Double hrMin, // Minimum heart rate
    String vigorousActivityTime, // Time spent in vigorous activity (HH:MM:SS)

    Integer activities, // Number of activities logged
    String moderateActivityTime, // Time spent in moderate activity (HH:MM:SS)
    String intensityTime, // Total intensity minutes (HH:MM:SS)
    Double rhrAvg, // Average resting heart rate
    Double rhrMin, // Minimum resting heart rate
    String sleepMax, // Maximum sleep duration (HH:MM:SS)
    Double activitiesCalories // Calories burned during activities
) {}
