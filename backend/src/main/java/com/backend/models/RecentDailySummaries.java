package com.backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.util.List;

@Document(collection = "recent_daily_summaries") // Stores pre-processed 7-day history
public record RecentDailySummaries(
    @Id String id, // Unique MongoDB ID
    LocalDate latestDay, // Most recent date in the dataset

    List<Double> hrMax, // Last 7 days of max heart rate
    List<Double> caloriesActiveAvg, // Last 7 days of active calories burned
    List<Double> hydrationIntake, // Last 7 days of hydration intake (ml)

    List<String> remSleepMax, // Last 7 days of max REM sleep duration (HH:MM:SS)
    List<String> intensityTimeGoal, // Last 7 days of intensity time goal (HH:MM:SS)

    List<Double> caloriesAvg, // Last 7 days of avg daily calories burned
    List<Double> hydrationAvg, // Last 7 days of avg daily hydration intake
    List<Integer> caloriesGoal, // Last 7 days of calorie burn goals
    List<Double> sweatLoss, // Last 7 days of sweat loss

    List<Double> rhrMax, // Last 7 days of max resting heart rate
    List<Double> inactiveHrAvg, // Last 7 days of avg inactive heart rate
    List<Double> inactiveHrMin, // Last 7 days of min inactive heart rate

    List<Integer> stepsGoal, // Last 7 days of step goals
    List<Double> caloriesBmrAvg, // Last 7 days of avg BMR
    List<Double> floors, // Last 7 days of floors climbed
    List<Double> caloriesConsumedAvg, // Last 7 days of avg calories consumed
    List<Integer> hydrationGoal, // Last 7 days of hydration goals

    List<String> remSleepAvg, // Last 7 days of avg REM sleep duration (HH:MM:SS)
    List<String> remSleepMin, // Last 7 days of min REM sleep duration (HH:MM:SS)

    List<Double> stressAvg, // Last 7 days of avg stress level
    List<Double> rrMax, // Last 7 days of max respiration rate
    List<Double> bbMin, // Last 7 days of min body battery
    List<Double> inactiveHrMax, // Last 7 days of max inactive heart rate
    List<Double> weightMin, // Last 7 days of min weight recorded
    List<Double> sweatLossAvg, // Last 7 days of avg sweat loss

    List<String> sleepAvg, // Last 7 days of avg sleep duration (HH:MM:SS)
    List<String> sleepMin, // Last 7 days of min sleep duration (HH:MM:SS)

    List<Double> rrMin, // Last 7 days of min respiration rate
    List<Double> bbMax, // Last 7 days of max body battery
    List<Double> spo2Avg, // Last 7 days of avg SpO2
    List<Double> spo2Min, // Last 7 days of min SpO2
    List<Double> weightAvg, // Last 7 days of avg weight recorded
    List<Double> activitiesDistance, // Last 7 days of total distance traveled

    List<Integer> steps, // Last 7 days of steps taken
    List<Double> rrWakingAvg, // Last 7 days of avg waking respiration rate
    List<Double> floorsGoal, // Last 7 days of floors climbed goals
    List<Double> weightMax, // Last 7 days of max weight recorded

    List<Double> hrAvg, // Last 7 days of avg heart rate
    List<Double> hrMin, // Last 7 days of min heart rate

    List<String> vigorousActivityTime, // Last 7 days of vigorous activity duration (HH:MM:SS)
    List<Integer> activities, // Last 7 days of activities logged

    List<String> moderateActivityTime, // Last 7 days of moderate activity duration (HH:MM:SS)
    List<String> intensityTime, // Last 7 days of total intensity time (HH:MM:SS)

    List<Double> rhrAvg, // Last 7 days of avg resting heart rate
    List<Double> rhrMin, // Last 7 days of min resting heart rate
    List<String> sleepMax, // Last 7 days of max sleep duration (HH:MM:SS)

    List<Double> activitiesCalories // Last 7 days of calories burned in activities
) {}
