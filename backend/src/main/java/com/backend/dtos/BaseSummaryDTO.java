package com.backend.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

import static com.backend.utils.ValidationPatterns.TIME_PATTERN;

/**
 * A summary of key health metrics.
 * <p>
 * Includes heart rate, resting heart rate, calorie burn, weight,
 * hydration, stress, respiration rate, sleep duration, and SpO2 levels.
 * Each field has predefined validation constraints to ensure correct data.
 * </p>
 *
 * This DTO is built from the corresponding model record {@code com.backend.models.BaseSummary}.
 */
public record BaseSummaryDTO(
    // ------ Heart Rate ----- //
    @Min(20) @Max(250)
    Integer hrMin,    // Min heart rate (20-250 bpm)
    @Min(20) @Max(250)
    Integer hrMax,    // Max heart rate (20-250 bpm)
    @Min(20) @Max(250)
    Integer hrAvg,    // Avg heart rate (20-250 bpm)
    @Min(20) @Max(150)
    Integer rhrMin,   // Min resting heart rate (20-150 bpm)
    @Min(20) @Max(150)
    Integer rhrMax,   // Max resting heart rate (20-150 bpm)
    @Min(20) @Max(150)
    Integer rhrAvg,   // Avg resting heart rate (20-150 bpm)
    @Min(20) @Max(150)
    Integer inactiveHrMin, // Min inactive heart rate
    @Min(20) @Max(150)
    Integer inactiveHrMax, // Max inactive heart rate
    @Min(20) @Max(150)
    Integer inactiveHrAvg, // Avg inactive heart rate

    // ------ Calories ----- //
    @PositiveOrZero
    Integer caloriesAvg,    // Avg daily calories burned (≥0)
    @PositiveOrZero
    Integer caloriesGoal,   // Target calorie burn goal (≥0)
    @PositiveOrZero
    Integer caloriesBmrAvg, // Avg Basal Metabolic Rate (BMR) (≥0)
    @PositiveOrZero
    Integer caloriesConsumedAvg, // Avg daily calories consumed (≥0)
    @PositiveOrZero
    Integer caloriesActiveAvg,  // Avg active calories burned (≥0)
    @PositiveOrZero
    Integer activitiesCalories, // Total calories burned in activities (≥0)

    // ------ Weight ----- //
    @Min(0) @Max(300)
    Double weightMin,   // Min weight recorded (0-300 kg)
    @Min(0) @Max(300)
    Double weightMax,   // Max weight recorded (0-300 kg)
    @Min(0) @Max(300)
    Double weightAvg,   // Avg weight recorded (0-300 kg)

    // ------ Hydration ----- //
    @PositiveOrZero
    Integer hydrationGoal,   // Target hydration intake (ml, ≥0)
    @PositiveOrZero
    Integer hydrationIntake, // Total hydration intake (ml, ≥0)
    @PositiveOrZero
    Integer hydrationAvg,    // Avg daily hydration intake (ml, ≥0)
    @PositiveOrZero
    Integer sweatLoss,       // Total sweat loss (ml, ≥0)
    @PositiveOrZero
    Integer sweatLossAvg,    // Avg sweat loss (ml, ≥0)

    // ------ Stress & Body Battery ----- //
    @Min(0) @Max(100)
    Integer bbMin,    // Min body battery (0-100)
    @Min(0) @Max(100)
    Integer bbMax,    // Max body battery (0-100)
    @Min(0) @Max(100)
    Integer stressAvg, // Avg stress level (0-100)

    // ------ Respiration & SPO ----- //
    @Min(5) @Max(50)
    Integer rrMin,    // Min respiration rate (5-50 bpm)
    @Min(5) @Max(50)
    Integer rrMax,    // Max respiration rate (5-50 bpm)
    @Min(5) @Max(50)
    Integer rrWakingAvg,  // Avg waking respiration rate (5-50 bpm)
    @Min(70) @Max(100)
    Integer spo2Min, // Min SpO2 level (70-100%)
    @Min(70) @Max(100)
    Integer spo2Avg, // Avg SpO2 level (70-100%)

    // ------ Sleep ----- //
    @Pattern(regexp = TIME_PATTERN, message = "Invalid time format (HH:MM:SS)")
    String sleepMin,   // Min sleep duration (HH:MM:SS format)
    @Pattern(regexp = TIME_PATTERN, message = "Invalid time format (HH:MM:SS)")
    String sleepMax,   // Max sleep duration (HH:MM:SS format)
    @Pattern(regexp = TIME_PATTERN, message = "Invalid time format (HH:MM:SS)")
    String sleepAvg,   // Avg sleep duration (HH:MM:SS format)
    @Pattern(regexp = TIME_PATTERN, message = "Invalid time format (HH:MM:SS)")
    String remSleepMin, // Min REM sleep duration (HH:MM:SS format)
    @Pattern(regexp = TIME_PATTERN, message = "Invalid time format (HH:MM:SS)")
    String remSleepMax, // Max REM sleep duration (HH:MM:SS format)
    @Pattern(regexp = TIME_PATTERN, message = "Invalid time format (HH:MM:SS)")
    String remSleepAvg, // Avg REM sleep duration (HH:MM:SS format)

    // ------ Steps & Floors ----- //
    @PositiveOrZero
    Integer stepsGoal,  // Target step count (≥0)
    @PositiveOrZero
    Integer steps,      // Total steps taken (≥0)
    @PositiveOrZero
    Integer floorsGoal, // Target floors climbed (≥0)
    @PositiveOrZero
    Integer floors,     // Total floors climbed (≥0)

    // ------ Activities ----- //
    @PositiveOrZero
    Integer activities, // Total logged activities (≥0)
    @PositiveOrZero
    Double activitiesDistance, // Total distance traveled (km, ≥0)
    @Pattern(regexp = TIME_PATTERN, message = "Invalid time format (HH:MM:SS)")
    String intensityTimeGoal, // Total intensity time goal (HH:MM:SS format)
    @Pattern(regexp = TIME_PATTERN, message = "Invalid time format (HH:MM:SS)")
    String intensityTime, // Total intensity minutes (HH:MM:SS format)
    @Pattern(regexp = TIME_PATTERN, message = "Invalid time format (HH:MM:SS)")
    String moderateActivityTime, // Total time in moderate activity (HH:MM:SS format)
    @Pattern(regexp = TIME_PATTERN, message = "Invalid time format (HH:MM:SS)")
    String vigorousActivityTime // Total time in vigorous activity (HH:MM:SS format)
) {
    /**
     * Converts a {@link com.backend.models.BaseSummary} record to a {@link BaseSummaryDTO}.
     *
     * @param model the BaseSummary model from the persistence layer.
     * @return a new BaseSummaryDTO populated with values from the model.
     */
    public static BaseSummaryDTO fromModel(com.backend.models.BaseSummary model) {
        return new BaseSummaryDTO(
            model.hrMin(),
            model.hrMax(),
            model.hrAvg(),
            model.rhrMin(),
            model.rhrMax(),
            model.rhrAvg(),
            model.inactiveHrMin(),
            model.inactiveHrMax(),
            model.inactiveHrAvg(),
            model.caloriesAvg(),
            model.caloriesGoal(),
            model.caloriesBmrAvg(),
            model.caloriesConsumedAvg(),
            model.caloriesActiveAvg(),
            model.activitiesCalories(),
            model.weightMin(),
            model.weightMax(),
            model.weightAvg(),
            model.hydrationGoal(),
            model.hydrationIntake(),
            model.hydrationAvg(),
            model.sweatLoss(),
            model.sweatLossAvg(),
            model.bbMin(),
            model.bbMax(),
            model.stressAvg(),
            model.rrMin(),
            model.rrMax(),
            model.rrWakingAvg(),
            model.spo2Min(),
            model.spo2Avg(),
            model.sleepMin(),
            model.sleepMax(),
            model.sleepAvg(),
            model.remSleepMin(),
            model.remSleepMax(),
            model.remSleepAvg(),
            model.stepsGoal(),
            model.steps(),
            model.floorsGoal(),
            model.floors(),
            model.activities(),
            model.activitiesDistance(),
            model.intensityTimeGoal(),
            model.intensityTime(),
            model.moderateActivityTime(),
            model.vigorousActivityTime()
        );
    }
}
