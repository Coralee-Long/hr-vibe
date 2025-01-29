package com.backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Document(collection = "yearly_summaries") // Stores yearly aggregated data
public record YearlySummary(
    @Id String id, // Unique MongoDB ID
    LocalDate firstDay, // First day of the year (YYYY-01-01)

    Double hrMax, // Maximum heart rate recorded during the year
    Double caloriesActiveAvg, // Average active calories burned
    Double hydrationIntake, // Total hydration intake (ml)
    String remSleepMax, // Maximum REM sleep duration (HH:MM:SS)
    String intensityTimeGoal, // Yearly intensity time goal (HH:MM:SS)
    Double caloriesAvg, // Average daily calories burned
    Double hydrationAvg, // Average daily hydration intake
    Integer caloriesGoal, // Yearly calorie burn goal
    Double sweatLoss, // Total sweat loss during the year

    Double rhrMax, // Maximum resting heart rate
    Double inactiveHrAvg, // Yearly average inactive heart rate
    Double inactiveHrMin, // Yearly minimum inactive heart rate
    Integer stepsGoal, // Yearly step goal
    Double caloriesBmrAvg, // Yearly average Basal Metabolic Rate (BMR)
    Double floors, // Total floors climbed
    Double caloriesConsumedAvg, // Yearly average calories consumed
    Integer hydrationGoal, // Yearly hydration goal (ml)

    String remSleepAvg, // Yearly average REM sleep duration (HH:MM:SS)
    String remSleepMin, // Yearly minimum REM sleep duration (HH:MM:SS)
    Integer stressAvg, // Yearly average stress level
    Double rrMax, // Maximum respiration rate (RR)
    Integer bbMin, // Minimum body battery during the year
    Double inactiveHrMax, // Maximum inactive heart rate
    Double weightMin, // Minimum weight recorded in the year
    Double sweatLossAvg, // Average sweat loss (ml)

    String sleepAvg, // Yearly average sleep duration (HH:MM:SS)
    String sleepMin, // Yearly minimum sleep duration (HH:MM:SS)
    Double rrMin, // Minimum respiration rate (RR)
    Integer bbMax, // Maximum body battery during the year
    Double spo2Avg, // Yearly average SpO2 (oxygen saturation %)
    Double spo2Min, // Yearly minimum SpO2
    Double weightAvg, // Yearly average weight recorded
    Double activitiesDistance, // Total distance traveled in activities (km)

    Integer steps, // Total steps taken during the year
    Double rrWakingAvg, // Yearly average waking respiration rate (RR)
    Double floorsGoal, // Yearly floors goal
    Double weightMax, // Maximum weight recorded
    Double hrAvg, // Yearly average heart rate
    Double hrMin, // Yearly minimum heart rate
    String vigorousActivityTime, // Yearly total time in vigorous activity (HH:MM:SS)

    Integer activities, // Total number of activities logged
    String moderateActivityTime, // Yearly total time in moderate activity (HH:MM:SS)
    String intensityTime, // Yearly total intensity minutes (HH:MM:SS)
    Double rhrAvg, // Yearly average resting heart rate
    Double rhrMin, // Yearly minimum resting heart rate
    String sleepMax, // Maximum sleep duration during the year (HH:MM:SS)
    Double activitiesCalories // Total calories burned during activities
) {}
