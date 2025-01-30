package com.backend.dtos;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

public record RecentDailySummariesDTO(
    @NotBlank String id,

    @NotNull LocalDate latestDay, // Most recent date in the dataset

    @Size(min = 7, max = 7) List<@PositiveOrZero Double> hrMax, // Last 7 days of max heart rate
    @Size(min = 7, max = 7) List<@PositiveOrZero Double> caloriesActiveAvg, // Last 7 days of active calories burned
    @Size(min = 7, max = 7) List<Double> hydrationIntake, // Nullable hydration intake

    @Size(min = 7, max = 7) List<@Pattern(regexp = TIME_PATTERN, message = "Invalid time format (HH:MM:SS)") String> remSleepMax,
    @Size(min = 7, max = 7) List<@Pattern(regexp = TIME_PATTERN, message = "Invalid time format (HH:MM:SS)") String> intensityTimeGoal,

    @Size(min = 7, max = 7) List<@PositiveOrZero Double> caloriesAvg,
    @Size(min = 7, max = 7) List<Double> hydrationAvg,
    @Size(min = 7, max = 7) List<@PositiveOrZero Integer> caloriesGoal,
    @Size(min = 7, max = 7) List<Double> sweatLoss,

    @Size(min = 7, max = 7) List<@PositiveOrZero Double> rhrMax,
    @Size(min = 7, max = 7) List<@PositiveOrZero Double> inactiveHrAvg,
    @Size(min = 7, max = 7) List<@PositiveOrZero Double> inactiveHrMin,

    @Size(min = 7, max = 7) List<@PositiveOrZero Integer> stepsGoal,
    @Size(min = 7, max = 7) List<@PositiveOrZero Double> caloriesBmrAvg,
    @Size(min = 7, max = 7) List<@PositiveOrZero Double> floors,
    @Size(min = 7, max = 7) List<Double> caloriesConsumedAvg,
    @Size(min = 7, max = 7) List<@PositiveOrZero Integer> hydrationGoal,

    @Size(min = 7, max = 7) List<@Pattern(regexp = TIME_PATTERN) String> remSleepAvg,
    @Size(min = 7, max = 7) List<@Pattern(regexp = TIME_PATTERN) String> remSleepMin,

    @Size(min = 7, max = 7) List<@PositiveOrZero Double> stressAvg,
    @Size(min = 7, max = 7) List<@PositiveOrZero Double> rrMax,
    @Size(min = 7, max = 7) List<@PositiveOrZero Double> bbMin,
    @Size(min = 7, max = 7) List<@PositiveOrZero Double> inactiveHrMax,
    @Size(min = 7, max = 7) List<Double> weightMin,
    @Size(min = 7, max = 7) List<Double> sweatLossAvg,

    @Size(min = 7, max = 7) List<@Pattern(regexp = TIME_PATTERN) String> sleepAvg,
    @Size(min = 7, max = 7) List<@Pattern(regexp = TIME_PATTERN) String> sleepMin,

    @Size(min = 7, max = 7) List<@PositiveOrZero Double> rrMin,
    @Size(min = 7, max = 7) List<@PositiveOrZero Double> bbMax,
    @Size(min = 7, max = 7) List<Double> spo2Avg,
    @Size(min = 7, max = 7) List<Double> spo2Min,
    @Size(min = 7, max = 7) List<Double> weightAvg,
    @Size(min = 7, max = 7) List<Double> activitiesDistance,

    @Size(min = 7, max = 7) List<@PositiveOrZero Integer> steps,
    @Size(min = 7, max = 7) List<@PositiveOrZero Double> rrWakingAvg,
    @Size(min = 7, max = 7) List<@PositiveOrZero Double> floorsGoal,
    @Size(min = 7, max = 7) List<Double> weightMax,

    @Size(min = 7, max = 7) List<@PositiveOrZero Double> hrAvg,
    @Size(min = 7, max = 7) List<@PositiveOrZero Double> hrMin,

    @Size(min = 7, max = 7) List<@Pattern(regexp = TIME_PATTERN) String> vigorousActivityTime,
    @Size(min = 7, max = 7) List<@PositiveOrZero Integer> activities,

    @Size(min = 7, max = 7) List<@Pattern(regexp = TIME_PATTERN) String> moderateActivityTime,
    @Size(min = 7, max = 7) List<@Pattern(regexp = TIME_PATTERN) String> intensityTime,

    @Size(min = 7, max = 7) List<@PositiveOrZero Double> rhrAvg,
    @Size(min = 7, max = 7) List<@PositiveOrZero Double> rhrMin,
    @Size(min = 7, max = 7) List<@Pattern(regexp = TIME_PATTERN) String> sleepMax,

    @Size(min = 7, max = 7) List<Double> activitiesCalories
) {
   private static final String TIME_PATTERN = "^([0-9]{2}):([0-5][0-9]):([0-5][0-9])$";
}
