package com.backend.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDate;
import java.util.List;

@Document(collection = "recent_daily_summaries") // Stores pre-processed 7-day history
@JsonInclude(JsonInclude.Include.ALWAYS)  // Ensures null fields are included
public record RecentDailySummaries(
    @Id String id, // Unique MongoDB ID

    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Field ("latestDay") LocalDate latestDay, // Most recent date in the dataset : Stored as LocalDate but formatted as a string in API responses


    // ------ Heart Rate ----- //
    @Field("hrMin") List<Integer> hrMin, // Last 7 days of min heart rate
    @Field("hrMax") List<Integer> hrMax, // Last 7 days of max heart rate
    @Field("hrAvg") List<Integer> hrAvg, // Last 7 days of avg heart rate
    @Field("rhrMin") List<Integer> rhrMin, // Last 7 days of min resting heart rate
    @Field("rhrMax") List<Integer> rhrMax, // Last 7 days of max resting heart rate
    @Field("rhrAvg") List<Integer> rhrAvg, // Last 7 days of avg resting heart rate
    @Field("inactiveHrMin") List<Integer> inactiveHrMin, // Last 7 days of min inactive heart rate
    @Field("inactiveHrMax") List<Integer> inactiveHrMax, // Last 7 days of max inactive heart rate
    @Field("inactiveHrAvg") List<Integer> inactiveHrAvg, // Last 7 days of avg inactive heart rate

    // ------ Calories ----- //
    @Field("caloriesAvg") List<Integer> caloriesAvg, // Last 7 days of avg daily calories burned
    @Field("caloriesGoal") List<Integer> caloriesGoal, // Last 7 days of calorie burn goals
    @Field("caloriesBmrAvg") List<Integer> caloriesBmrAvg, // Last 7 days of avg BMR
    @Field("caloriesConsumedAvg") List<Integer> caloriesConsumedAvg, // Last 7 days of avg calories consumed
    @Field("caloriesActiveAvg") List<Integer> caloriesActiveAvg, // Last 7 days of avg active calories burned
    @Field("activitiesCalories") List<Integer> activitiesCalories, // Last 7 days of calories burned in activities

    // ------ Weight ----- //
    @Field("weightMin") List<Double> weightMin, // Last 7 days of min weight recorded
    @Field("weightMax") List<Double> weightMax, // Last 7 days of max weight recorded
    @Field("weightAvg") List<Double> weightAvg, // Last 7 days of avg weight recorded

    // ------ Hydration ----- //
    @Field("hydrationGoal") List<Integer> hydrationGoal, // Last 7 days of hydration goals (ml)
    @Field("hydrationIntake") List<Integer> hydrationIntake, // Last 7 days of total hydration intake (ml)
    @Field("hydrationAvg") List<Integer> hydrationAvg, // Last 7 days of avg daily hydration intake
    @Field("sweatLoss") List<Integer> sweatLoss, // Last 7 days of total sweat loss (ml)
    @Field("sweatLossAvg") List<Integer> sweatLossAvg, // Last 7 days of avg sweat loss (ml)

    // ------ Stress & Body Battery ----- //
    @Field("bbMin") List<Integer> bbMin, // Last 7 days of min body battery
    @Field("bbMax") List<Integer> bbMax, // Last 7 days of max body battery
    @Field("stressAvg") List<Integer> stressAvg, // Last 7 days of avg stress level

    // ------ Respiration & SPO2 ----- //
    @Field("rrMin") List<Integer> rrMin, // Last 7 days of min respiration rate (RR)
    @Field("rrMax") List<Integer> rrMax, // Last 7 days of max respiration rate (RR)
    @Field("rrWakingAvg") List<Integer> rrWakingAvg, // Last 7 days of avg waking respiration rate (RR)
    @Field("spo2Min") List<Integer> spo2Min, // Last 7 days of min SpO2
    @Field("spo2Avg") List<Integer> spo2Avg, // Last 7 days of avg SpO2

    // ------ Sleep ----- //
    @Field("sleepMin") List<String> sleepMin, // Last 7 days of min sleep duration (HH:MM:SS)
    @Field("sleepMax") List<String> sleepMax, // Last 7 days of max sleep duration (HH:MM:SS)
    @Field("sleepAvg") List<String> sleepAvg, // Last 7 days of avg sleep duration (HH:MM:SS)
    @Field("remSleepMin") List<String> remSleepMin, // Last 7 days of min REM sleep duration (HH:MM:SS)
    @Field("remSleepMax") List<String> remSleepMax, // Last 7 days of max REM sleep duration (HH:MM:SS)
    @Field("remSleepAvg") List<String> remSleepAvg, // Last 7 days of avg REM sleep duration (HH:MM:SS)

    // ------ Steps & Floors ----- //
    @Field("stepsGoal") List<Integer> stepsGoal, // Last 7 days of step goals
    @Field("steps") List<Integer> steps, // Last 7 days of steps taken
    @Field("floorsGoal") List<Integer> floorsGoal, // Last 7 days of floors climbed goals
    @Field("floors") List<Integer> floors, // Last 7 days of floors climbed

    // ------ Activities ----- //
    @Field("activities") List<Integer> activities, // Last 7 days of logged activities
    @Field("activitiesDistance") List<Double> activitiesDistance, // Last 7 days of total distance traveled (km) (nullable)
    @Field("intensityTimeGoal") List<String> intensityTimeGoal, // Last 7 days of total intensity time goal (HH:MM:SS)
    @Field("intensityTime") List<String> intensityTime, // Last 7 days of total intensity minutes (HH:MM:SS)
    @Field("moderateActivityTime") List<String> moderateActivityTime, // Last 7 days of total time in moderate activity (HH:MM:SS)
    @Field("vigorousActivityTime") List<String> vigorousActivityTime // Last 7 days of total time in vigorous activity (HH:MM:SS)
) {}
