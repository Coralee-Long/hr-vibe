package com.backend.models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document // This will be embedded in other summary documents
public record BaseSummary(
    Double hrMax, // Max heart rate recorded

    Double caloriesActiveAvg, // Avg active calories burned
    Double hydrationIntake, // Total hydration intake (ml) (nullable)

    String remSleepMax, // Max REM sleep duration (HH:MM:SS)
    String intensityTimeGoal, // Total intensity time goal (HH:MM:SS)

    Double caloriesAvg, // Avg daily calories burned
    Double hydrationAvg, // Avg daily hydration intake (nullable)
    Integer caloriesGoal, // Target calorie burn goal
    Double sweatLoss, // Total sweat loss (ml) (nullable)

    Double rhrMax, // Max resting heart rate
    Double inactiveHrAvg, // Avg inactive heart rate
    Double inactiveHrMin, // Min inactive heart rate

    Integer stepsGoal, // Target steps goal
    Double caloriesBmrAvg, // Avg Basal Metabolic Rate (BMR)
    Double floors, // Total floors climbed
    Double caloriesConsumedAvg, // Avg calories consumed (nullable)
    Integer hydrationGoal, // Target hydration intake (ml)

    String remSleepAvg, // Avg REM sleep duration (HH:MM:SS)
    String remSleepMin, // Min REM sleep duration (HH:MM:SS)

    Double stressAvg, // Avg stress level
    Double rrMax, // Max respiration rate (RR)
    Double bbMin, // Min body battery

    Double inactiveHrMax, // Max inactive heart rate
    Double weightMin, // Min weight recorded (nullable)
    Double sweatLossAvg, // Avg sweat loss (ml) (nullable)

    String sleepAvg, // Avg sleep duration (HH:MM:SS)
    String sleepMin, // Min sleep duration (HH:MM:SS)

    Double rrMin, // Min respiration rate (RR)
    Double bbMax, // Max body battery
    Double spo2Avg, // Avg SpO2 (nullable)
    Double spo2Min, // Min SpO2 (nullable)
    Double weightAvg, // Avg weight recorded (nullable)
    Double activitiesDistance, // Total distance traveled (km) (nullable)

    Integer steps, // Total steps taken
    Double rrWakingAvg, // Avg waking respiration rate (RR)
    Double floorsGoal, // Target floors climbed
    Double weightMax, // Max weight recorded (nullable)

    Double hrAvg, // Avg heart rate
    Double hrMin, // Min heart rate

    String vigorousActivityTime, // Total time in vigorous activity (HH:MM:SS)
    Integer activities, // Total logged activities

    String moderateActivityTime, // Total time in moderate activity (HH:MM:SS)
    String intensityTime, // Total intensity minutes (HH:MM:SS)

    Double rhrAvg, // Avg resting heart rate
    Double rhrMin, // Min resting heart rate
    String sleepMax, // Max sleep duration (HH:MM:SS)

    Double activitiesCalories // Total calories burned in activities (nullable)
) {}
