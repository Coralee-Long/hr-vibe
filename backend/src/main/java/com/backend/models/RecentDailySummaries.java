package com.backend.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;
import java.util.List;

@Document(collection = "recent_daily_summaries") // Stores pre-processed 7-day history
public record RecentDailySummaries(
    @Id String id, // Unique MongoDB ID
    LocalDate latestDay, // Most recent date in the dataset

    List<Integer> hrMin, // Last 7 days of min heart rate
    List<Integer> hrMax, // Last 7 days of max heart rate
    List<Integer> hrAvg, // Last 7 days of avg heart rate

    List<Integer> rhrMin, // Last 7 days of min resting heart rate
    List<Integer> rhrMax, // Last 7 days of max resting heart rate
    List<Integer> rhrAvg, // Last 7 days of avg resting heart rate

    List<Integer> inactiveHrMin, // Last 7 days of min inactive heart rate
    List<Integer> inactiveHrMax, // Last 7 days of max inactive heart rate
    List<Integer> inactiveHrAvg, // Last 7 days of avg inactive heart rate

    List<Integer> caloriesAvg, // Last 7 days of avg daily calories burned
    List<Integer> caloriesGoal, // Last 7 days of calorie burn goals
    List<Integer> caloriesBmrAvg, // Last 7 days of avg BMR
    List<Integer> caloriesConsumedAvg, // Last 7 days of avg calories consumed
    List<Integer> caloriesActiveAvg, // Last 7 days of avg active calories burned
    List<Integer> activitiesCalories, // Last 7 days of calories burned in activities

    List<Double> weightMin, // Last 7 days of min weight recorded
    List<Double> weightMax, // Last 7 days of max weight recorded
    List<Double> weightAvg, // Last 7 days of avg weight recorded

    List<Integer> hydrationGoal, // Last 7 days of hydration goals (ml)
    List<Integer> hydrationIntake, // Last 7 days of total hydration intake (ml)
    List<Integer> hydrationAvg, // Last 7 days of avg daily hydration intake
    List<Integer> sweatLoss, // Last 7 days of total sweat loss (ml)
    List<Integer> sweatLossAvg, // Last 7 days of avg sweat loss (ml)

    List<Integer> bbMin, // Last 7 days of min body battery
    List<Integer> bbMax, // Last 7 days of max body battery
    List<Integer> stressAvg, // Last 7 days of avg stress level

    List<Integer> rrMin, // Last 7 days of min respiration rate (RR)
    List<Integer> rrMax, // Last 7 days of max respiration rate (RR)
    List<Integer> rrWakingAvg, // Last 7 days of avg waking respiration rate (RR)

    List<String> sleepMin, // Last 7 days of min sleep duration (HH:MM:SS)
    List<String> sleepMax, // Last 7 days of max sleep duration (HH:MM:SS)
    List<String> sleepAvg, // Last 7 days of avg sleep duration (HH:MM:SS)

    List<String> remSleepMin, // Last 7 days of min REM sleep duration (HH:MM:SS)
    List<String> remSleepMax, // Last 7 days of max REM sleep duration (HH:MM:SS)
    List<String> remSleepAvg, // Last 7 days of avg REM sleep duration (HH:MM:SS)

    List<Integer> spo2Min, // Last 7 days of min SpO2
    List<Integer> spo2Avg, // Last 7 days of avg SpO2

    List<Integer> stepsGoal, // Last 7 days of step goals
    List<Integer> steps, // Last 7 days of steps taken
    List<Integer> floorsGoal, // Last 7 days of floors climbed goals
    List<Integer> floors, // Last 7 days of floors climbed

    List<Integer> activities, // Last 7 days of logged activities
    List<Double> activitiesDistance, // Last 7 days of total distance traveled (km) (nullable)

    List<String> intensityTimeGoal, // Last 7 days of total intensity time goal (HH:MM:SS)
    List<String> intensityTime, // Last 7 days of total intensity minutes (HH:MM:SS)
    List<String> moderateActivityTime, // Last 7 days of total time in moderate activity (HH:MM:SS)
    List<String> vigorousActivityTime // Last 7 days of total time in vigorous activity (HH:MM:SS)
) {}
