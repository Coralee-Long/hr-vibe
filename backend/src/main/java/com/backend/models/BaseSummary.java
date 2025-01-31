package com.backend.models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document // This will be embedded in other summary documents
public record BaseSummary(

    // ------ Heart Rate ----- //
    Integer hrMin, // Min heart rate
    Integer hrMax, // Max heart rate
    Integer hrAvg, // Avg heart rate
    Integer rhrMin, // Min resting heart rate
    Integer rhrMax, // Max resting heart rate
    Integer rhrAvg, // Avg resting heart rate
    Integer inactiveHrMin, // Min inactive heart rate
    Integer inactiveHrMax, // Max inactive heart rate
    Integer inactiveHrAvg, // Avg inactive heart rate

    // ------ Calories ----- //
    Integer caloriesAvg, // Avg daily calories burned
    Integer caloriesGoal, // Target calorie burn goal
    Integer caloriesBmrAvg, // Avg Basal Metabolic Rate (BMR)
    Integer caloriesConsumedAvg, // Avg calories consumed
    Integer caloriesActiveAvg, // Avg active calories burned
    Integer activitiesCalories, // Total calories burned in activities

    // ------ Weight ----- //
    Double weightMin, // Min weight recorded
    Double weightMax, // Max weight recorded
    Double weightAvg, // Avg weight recorded

    // ------ Hydration ----- //
    Integer hydrationGoal, // Target hydration intake (ml)
    Integer hydrationIntake, // Total hydration intake (ml)
    Integer hydrationAvg, // Avg daily hydration intake
    Integer sweatLoss, // Total sweat loss (ml)
    Integer sweatLossAvg, // Avg sweat loss (ml)

    // ------ Stress & Body Battery ----- //
    Integer bbMin, // Min body battery
    Integer bbMax, // Max body battery
    Integer stressAvg, // Avg stress level

    // ------ Respiration & SPO ----- //
    Integer rrMin, // Min respiration rate (RR)
    Integer rrMax, // Max respiration rate (RR)
    Integer rrWakingAvg, // Avg waking respiration rate (RR)
    Integer spo2Min, // Min SpO2
    Integer spo2Avg, // Avg SpO2

    // ------ Sleep ----- //
    String sleepMin, // Min sleep duration (HH:MM:SS)
    String sleepMax, // Max sleep duration (HH:MM:SS)
    String sleepAvg, // Avg sleep duration (HH:MM:SS)
    String remSleepMin, // Min REM sleep duration (HH:MM:SS)
    String remSleepMax, // Max REM sleep duration (HH:MM:SS)
    String remSleepAvg, // Avg REM sleep duration (HH:MM:SS)

    // ------ Steps & Floors ----- //
    Integer stepsGoal, // Target steps goal
    Integer steps, // Total steps taken
    Integer floorsGoal, // Target floors climbed
    Integer floors, // Total floors climbed

    // ------ Activities ----- //
    Integer activities, // Total logged activities
    Double activitiesDistance, // Total distance traveled (km)
    String intensityTimeGoal, // Total intensity time goal (HH:MM:SS)
    String intensityTime, // Total intensity minutes (HH:MM:SS)
    String moderateActivityTime, // Total time in moderate activity (HH:MM:SS)
    String vigorousActivityTime // Total time in vigorous activity (HH:MM:SS)
) {}
