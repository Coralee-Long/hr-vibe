package com.backend.dtos;

import jakarta.validation.constraints.*;

public record BaseSummaryDTO(
    @PositiveOrZero Double hrMax, // Max heart rate recorded

    @PositiveOrZero Double caloriesActiveAvg, // Avg active calories burned
    Double hydrationIntake, // Nullable hydration intake (ml)

    @Pattern(regexp = TIME_PATTERN, message = "Invalid time format (HH:MM:SS)")
    String remSleepMax, // Max REM sleep duration

    @Pattern(regexp = TIME_PATTERN, message = "Invalid time format (HH:MM:SS)")
    String intensityTimeGoal, // Total intensity time goal

    @PositiveOrZero Double caloriesAvg, // Avg daily calories burned
    Double hydrationAvg, // Nullable avg daily hydration intake
    @PositiveOrZero Integer caloriesGoal, // Target calorie burn goal
    Double sweatLoss, // Nullable total sweat loss (ml)

    @PositiveOrZero Double rhrMax, // Max resting heart rate
    @PositiveOrZero Double inactiveHrAvg, // Avg inactive heart rate
    @PositiveOrZero Double inactiveHrMin, // Min inactive heart rate

    @PositiveOrZero Integer stepsGoal, // Target steps goal
    @PositiveOrZero Double caloriesBmrAvg, // Avg Basal Metabolic Rate (BMR)
    @PositiveOrZero Double floors, // Total floors climbed
    Double caloriesConsumedAvg, // Nullable avg calories consumed
    @PositiveOrZero Integer hydrationGoal, // Target hydration intake (ml)

    @Pattern(regexp = TIME_PATTERN, message = "Invalid time format (HH:MM:SS)")
    String remSleepAvg, // Avg REM sleep duration

    @Pattern(regexp = TIME_PATTERN, message = "Invalid time format (HH:MM:SS)")
    String remSleepMin, // Min REM sleep duration

    @PositiveOrZero Double stressAvg, // Avg stress level
    @PositiveOrZero Double rrMax, // Max respiration rate (RR)
    @PositiveOrZero Double bbMin, // Min body battery

    @PositiveOrZero Double inactiveHrMax, // Max inactive heart rate
    Double weightMin, // Nullable min weight recorded
    Double sweatLossAvg, // Nullable avg sweat loss (ml)

    @Pattern(regexp = TIME_PATTERN, message = "Invalid time format (HH:MM:SS)")
    String sleepAvg, // Avg sleep duration

    @Pattern(regexp = TIME_PATTERN, message = "Invalid time format (HH:MM:SS)")
    String sleepMin, // Min sleep duration

    @PositiveOrZero Double rrMin, // Min respiration rate (RR)
    @PositiveOrZero Double bbMax, // Max body battery
    Double spo2Avg, // Nullable avg SpO2
    Double spo2Min, // Nullable min SpO2
    Double weightAvg, // Nullable avg weight recorded
    Double activitiesDistance, // Nullable total distance traveled (km)

    @PositiveOrZero Integer steps, // Total steps taken
    @PositiveOrZero Double rrWakingAvg, // Avg waking respiration rate (RR)
    @PositiveOrZero Double floorsGoal, // Target floors climbed
    Double weightMax, // Nullable max weight recorded

    @PositiveOrZero Double hrAvg, // Avg heart rate
    @PositiveOrZero Double hrMin, // Min heart rate

    @Pattern(regexp = TIME_PATTERN, message = "Invalid time format (HH:MM:SS)")
    String vigorousActivityTime, // Total time in vigorous activity

    @PositiveOrZero Integer activities, // Total logged activities

    @Pattern(regexp = TIME_PATTERN, message = "Invalid time format (HH:MM:SS)")
    String moderateActivityTime, // Total time in moderate activity

    @Pattern(regexp = TIME_PATTERN, message = "Invalid time format (HH:MM:SS)")
    String intensityTime, // Total intensity minutes

    @PositiveOrZero Double rhrAvg, // Avg resting heart rate
    @PositiveOrZero Double rhrMin, // Min resting heart rate

    @Pattern(regexp = TIME_PATTERN, message = "Invalid time format (HH:MM:SS)")
    String sleepMax, // Max sleep duration

    Double activitiesCalories // Nullable total calories burned in activities
) {
   private static final String TIME_PATTERN = "^([0-9]{2}):([0-5][0-9]):([0-5][0-9])$";
}
