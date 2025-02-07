package com.backend.dtos;

import com.backend.models.RecentDailySummaries;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

import static com.backend.utils.ValidationPatterns.TIME_PATTERN;

public record RecentDailySummariesDTO(

    @NotBlank
    String id, // Unique MongoDB ID

    @NotNull
    String latestDay, // Most recent date in the dataset

    // ------ Heart Rate ----- //
    @Size(min = 7, max = 7)
    List<@Min(20) @Max(250) Integer> hrMin, // Last 7 days of min heart rate
    @Size(min = 7, max = 7)
    List<@Min(20) @Max(250) Integer> hrMax, // Last 7 days of max heart rate
    @Size(min = 7, max = 7)
    List<@Min(20) @Max(250) Integer> hrAvg, // Last 7 days of avg heart rate
    @Size(min = 7, max = 7)
    List<@Min(20) @Max(250) Integer> rhrMin, // Last 7 days of min resting heart rate
    @Size(min = 7, max = 7)
    List<@Min(20) @Max(250) Integer> rhrMax, // Last 7 days of max resting heart rate
    @Size(min = 7, max = 7)
    List<@Min(20) @Max(250) Integer> rhrAvg, // Last 7 days of avg resting heart rate
    @Size(min = 7, max = 7)
    List<@Min(20) @Max(250) Integer> inactiveHrMin, // Last 7 days of min inactive heart rate
    @Size(min = 7, max = 7)
    List<@Min(20) @Max(250) Integer> inactiveHrMax, // Last 7 days of max inactive heart rate
    @Size(min = 7, max = 7)
    List<@Min(20) @Max(250) Integer> inactiveHrAvg, // Last 7 days of avg inactive heart rate


    // ------ Calories ----- //
    @Size(min = 7, max = 7)
    List<@PositiveOrZero Integer> caloriesAvg, // Last 7 days of avg daily calories burned
    @Size(min = 7, max = 7)
    List<@PositiveOrZero Integer> caloriesGoal, // Last 7 days of calorie burn goals
    @Size(min = 7, max = 7)
    List<@PositiveOrZero Integer> caloriesBmrAvg, // Last 7 days of avg BMR
    @Size(min = 7, max = 7)
    List<Integer> caloriesConsumedAvg, // Last 7 days of avg calories consumed
    @Size(min = 7, max = 7)
    List<@PositiveOrZero Integer> caloriesActiveAvg, // Last 7 days of avg active calories burned
    @Size(min = 7, max = 7)
    List<Integer> activitiesCalories, // Last 7 days of calories burned in activities


    // ------ Weight ----- //
    @Size(min = 7, max = 7)
    List<@Min(0) @Max(300) Double> weightMin, // Last 7 days of min weight recorded
    @Size(min = 7, max = 7)
    List<@Min(0) @Max(300) Double> weightMax, // Last 7 days of max weight recorded
    @Size(min = 7, max = 7)
    List<@Min(0) @Max(300) Double> weightAvg, // Last 7 days of avg weight recorded


    // ------ Hydration ----- //
    @Size(min = 7, max = 7)
    List<@PositiveOrZero Integer> hydrationGoal, // Last 7 days of hydration goals (ml)
    @Size(min = 7, max = 7)
    List<@PositiveOrZero Integer> hydrationIntake, // Last 7 days of total hydration intake (ml)
    @Size(min = 7, max = 7)
    List<@PositiveOrZero Integer> hydrationAvg, // Last 7 days of avg daily hydration intake
    @Size(min = 7, max = 7)
    List<@PositiveOrZero Integer> sweatLoss, // Last 7 days of total sweat loss (ml)
    @Size(min = 7, max = 7)
    List<@PositiveOrZero Integer> sweatLossAvg, // Last 7 days of avg sweat loss (ml)


    // ------ Stress & Body Battery ----- //
    @Size(min = 7, max = 7)
    List<@Min(0) @Max(100) Integer> bbMin, // Last 7 days of min body battery
    @Size(min = 7, max = 7)
    List<@Min(0) @Max(100) Integer> bbMax, // Last 7 days of max body battery
    @Size(min = 7, max = 7)
    List<@Min(0) @Max(100) Integer> stressAvg, // Last 7 days of avg stress level


    // ------ Respiration & SPO ----- //
    @Size(min = 7, max = 7)
    List<@Min(5) @Max(50) Integer> rrMin, // Last 7 days of min respiration rate (RR)
    @Size(min = 7, max = 7)
    List<@Min(5) @Max(50) Integer> rrMax, // Last 7 days of max respiration rate (RR)
    @Size(min = 7, max = 7)
    List<@Min(5) @Max(50) Integer> rrWakingAvg, // Last 7 days of avg waking respiration rate (RR)
    @Size(min = 7, max = 7)
    List<@Min(70) @Max(100) Integer> spo2Min, // Last 7 days of min SpO2 level
    @Size(min = 7, max = 7)
    List<@Min(70) @Max(100) Integer> spo2Avg, // Last 7 days of avg SpO2 level


    // ------ Sleep ----- //
    @Size(min = 7, max = 7)
    List<@Pattern(regexp = TIME_PATTERN, message = "Invalid time format (HH:MM:SS)") String> sleepMin, // Last 7 days of min sleep duration (HH:MM:SS)
    @Size(min = 7, max = 7)
    List<@Pattern(regexp = TIME_PATTERN, message = "Invalid time format (HH:MM:SS)") String> sleepMax, // Last 7 days of max sleep duration (HH:MM:SS)
    @Size(min = 7, max = 7)
    List<@Pattern(regexp = TIME_PATTERN, message = "Invalid time format (HH:MM:SS)") String> sleepAvg, // Last 7 days of avg sleep duration (HH:MM:SS)
    @Size(min = 7, max = 7)
    List<@Pattern(regexp = TIME_PATTERN, message = "Invalid time format (HH:MM:SS)") String> remSleepMin, // Last 7 days of min REM sleep duration (HH:MM:SS)
    @Size(min = 7, max = 7)
    List<@Pattern(regexp = TIME_PATTERN, message = "Invalid time format (HH:MM:SS)") String> remSleepMax, // Last 7 days of max REM sleep duration (HH:MM:SS)
    @Size(min = 7, max = 7)
    List<@Pattern(regexp = TIME_PATTERN, message = "Invalid time format (HH:MM:SS)") String> remSleepAvg, // Last 7 days of avg REM sleep duration (HH:MM:SS)


    // ------ Steps & Floors ----- //
    @Size(min = 7, max = 7)
    List<@PositiveOrZero Integer> stepsGoal, // Last 7 days of steps goal
    @Size(min = 7, max = 7)
    List<@PositiveOrZero Integer> steps, // Last 7 days of total steps taken
    @Size(min = 7, max = 7)
    List<@PositiveOrZero Integer> floorsGoal, // Last 7 days of floors climbed goal
    @Size(min = 7, max = 7)
    List<@PositiveOrZero Integer> floors, // Last 7 days of total floors climbed


    // ------ Activities ----- //
    @Size(min = 7, max = 7)
    List<@PositiveOrZero Integer> activities, // Last 7 days of total logged activities
    @Size(min = 7, max = 7)
    List<@PositiveOrZero Double> activitiesDistance, // Last 7 days of total distance traveled (km)
    @Size(min = 7, max = 7)
    List<@Pattern(regexp = TIME_PATTERN, message = "Invalid time format (HH:MM:SS)") String> intensityTimeGoal, // Last 7 days of intensity time goal (HH:MM:SS)
    @Size(min = 7, max = 7)
    List<@Pattern(regexp = TIME_PATTERN, message = "Invalid time format (HH:MM:SS)") String> intensityTime, // Last 7 days of total intensity minutes (HH:MM:SS)
    @Size(min = 7, max = 7)
    List<@Pattern(regexp = TIME_PATTERN, message = "Invalid time format (HH:MM:SS)") String> moderateActivityTime, // Last 7 days of total moderate activity time (HH:MM:SS)
    @Size(min = 7, max = 7)
    List<@Pattern(regexp = TIME_PATTERN, message = "Invalid time format (HH:MM:SS)") String> vigorousActivityTime // Last 7 days of total vigorous activity time (HH:MM:SS)
)  {
    public static RecentDailySummariesDTO fromModel (RecentDailySummaries model) {
        return new RecentDailySummariesDTO(
            model.id(),
            model.latestDay().toString(), // Convert LocalDate to String
            model.hrMin(), model.hrMax(), model.hrAvg(),
            model.rhrMin(), model.rhrMax(), model.rhrAvg(),
            model.inactiveHrMin(), model.inactiveHrMax(), model.inactiveHrAvg(),
            model.caloriesAvg(), model.caloriesGoal(), model.caloriesBmrAvg(),
            model.caloriesConsumedAvg(), model.caloriesActiveAvg(), model.activitiesCalories(),
            model.weightMin(), model.weightMax(), model.weightAvg(),
            model.hydrationGoal(), model.hydrationIntake(), model.hydrationAvg(),
            model.sweatLoss(), model.sweatLossAvg(),
            model.bbMin(), model.bbMax(), model.stressAvg(),
            model.rrMin(), model.rrMax(), model.rrWakingAvg(),
            model.spo2Min(), model.spo2Avg(),
            model.sleepMin(), model.sleepMax(), model.sleepAvg(),
            model.remSleepMin(), model.remSleepMax(), model.remSleepAvg(),
            model.stepsGoal(), model.steps(), model.floorsGoal(), model.floors(),
            model.activities(), model.activitiesDistance(),
            model.intensityTimeGoal(), model.intensityTime(),
            model.moderateActivityTime(), model.vigorousActivityTime()
        );
    }
}