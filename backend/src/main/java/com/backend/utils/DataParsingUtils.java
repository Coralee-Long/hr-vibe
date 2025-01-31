package com.backend.utils;

import com.backend.models.BaseSummary;
import com.backend.models.CurrentDaySummary;
import com.backend.models.RecentDailySummaries;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class DataParsingUtils {

   private DataParsingUtils() {} // Prevent instantiation

   public static String cleanTimeFormat(String time) {
      return (time != null && time.contains(".")) ? time.substring(0, 8) : time;
   }

   public static String getString(Map<String, Object> data, String key) {
      Object value = data.getOrDefault(key, null);
      if (value == null) return null;

      String stringValue = value.toString();
      return key.contains("time") || key.contains("sleep") ? cleanTimeFormat(stringValue) : stringValue;
   }

   public static Integer getInteger(Map<String, Object> data, String key) {
      Object value = data.getOrDefault(key, null);
      if (value == null) return null;

      if (value instanceof Double) {
         return roundDoubleToInteger((Double) value);
      }
      return Integer.valueOf(value.toString());
   }

   public static Double getDouble(Map<String, Object> data, String key) {
      Object value = data.getOrDefault(key, null);
      if (value == null) return null;

      try {
         return Double.valueOf(value.toString());
      } catch (NumberFormatException e) {
         return null; // ✅ Gracefully handle invalid numeric input
      }
   }

   public static Integer roundDoubleToInteger(Double value) {
      return (value != null) ? (int) Math.round(value) : null;
   }

   /** Safely extracts a number (Double or Integer) and returns it as a Double */
   public static Double getNumber(Map<String, Object> data, String key) {
      Object value = data.getOrDefault(key, null);
      if (value == null) return null;

      if (value instanceof Integer) {
         return ((Integer) value).doubleValue(); // Convert Integer → Double
      }
      if (value instanceof Double) {
         return (Double) value;
      }
      try {
         return Double.valueOf(value.toString()); // Handle unexpected string numbers
      } catch (NumberFormatException e) {
         return null;
      }
   }


   public static BaseSummary mapToBaseSummary(Map<String, Object> data) {
      if (data == null) {
         throw new IllegalArgumentException("Data map cannot be null");
      }

      return new BaseSummary(
          // ------ Heart Rate (Convert to Integer) ----- //
          roundDoubleToInteger(getNumber(data, "hr_min")),
          roundDoubleToInteger(getNumber(data, "hr_max")),
          roundDoubleToInteger(getNumber(data, "hr_avg")),
          roundDoubleToInteger(getNumber(data, "rhr_min")),
          roundDoubleToInteger(getNumber(data, "rhr_max")),
          roundDoubleToInteger(getNumber(data, "rhr_avg")),
          roundDoubleToInteger(getNumber(data, "inactive_hr_min")),
          roundDoubleToInteger(getNumber(data, "inactive_hr_max")),
          roundDoubleToInteger(getNumber(data, "inactive_hr_avg")),

          // ------ Calories (Convert to Integer) ----- //
          roundDoubleToInteger(getNumber(data, "calories_avg")),
          roundDoubleToInteger(getNumber(data, "calories_goal")),
          roundDoubleToInteger(getNumber(data, "calories_bmr_avg")),
          roundDoubleToInteger(getNumber(data, "calories_consumed_avg")),
          roundDoubleToInteger(getNumber(data, "calories_active_avg")),
          roundDoubleToInteger(getNumber(data, "activities_calories")),

          // ------ Weight (Keep as Double) ----- //
          getNumber(data, "weight_min"),
          getNumber(data, "weight_max"),
          getNumber(data, "weight_avg"),

          // ------ Hydration (Convert to Integer) ----- //
          roundDoubleToInteger(getNumber(data, "hydration_goal")),
          roundDoubleToInteger(getNumber(data, "hydration_intake")),
          roundDoubleToInteger(getNumber(data, "hydration_avg")),
          roundDoubleToInteger(getNumber(data, "sweat_loss")),
          roundDoubleToInteger(getNumber(data, "sweat_loss_avg")),

          // ------ Stress & Body Battery (Convert to Integer) ----- //
          roundDoubleToInteger(getNumber(data, "bb_min")),
          roundDoubleToInteger(getNumber(data, "bb_max")),
          roundDoubleToInteger(getNumber(data, "stress_avg")),

          // ------ Respiration & SPO2 (Convert to Integer) ----- //
          roundDoubleToInteger(getNumber(data, "rr_min")),
          roundDoubleToInteger(getNumber(data, "rr_max")),
          roundDoubleToInteger(getNumber(data, "rr_waking_avg")),
          roundDoubleToInteger(getNumber(data, "spo2_min")),
          roundDoubleToInteger(getNumber(data, "spo2_avg")),

          // ------ Sleep (Keep as String) ----- //
          getString(data, "sleep_min"),
          getString(data, "sleep_max"),
          getString(data, "sleep_avg"),
          getString(data, "rem_sleep_min"),
          getString(data, "rem_sleep_max"),
          getString(data, "rem_sleep_avg"),

          // ------ Steps & Floors (Convert to Integer) ----- //
          roundDoubleToInteger(getNumber(data, "steps_goal")),
          roundDoubleToInteger(getNumber(data, "steps")),
          roundDoubleToInteger(getNumber(data, "floors_goal")),
          roundDoubleToInteger(getNumber(data, "floors")),

          // ------ Activities (Convert to Integer) ----- //
          roundDoubleToInteger(getNumber(data, "activities")),
          getNumber(data, "activities_distance"),

          // ------ Activity Time (Keep as String) ----- //
          getString(data, "intensity_time_goal"),
          getString(data, "intensity_time"),
          getString(data, "moderate_activity_time"),
          getString(data, "vigorous_activity_time")
      );
   }

   /**
    * Maps a list of the last 7 `CurrentDaySummary` records into a `RecentDailySummaries` model.
    */
   public static RecentDailySummaries mapToRecentDailySummaries(List<CurrentDaySummary> summaries) {
      if (summaries == null || summaries.isEmpty()) {
         throw new NoSuchElementException("Summaries list cannot be null or empty");
      }

      return new RecentDailySummaries(
          null,
          summaries.getFirst().day(),

          // ------ Heart Rate ----- //
          summaries.stream().map(s -> s.summary().hrMin()).toList(),
          summaries.stream().map(s -> s.summary().hrMax()).toList(),
          summaries.stream().map(s -> s.summary().hrAvg()).toList(),
          summaries.stream().map(s -> s.summary().rhrMin()).toList(),
          summaries.stream().map(s -> s.summary().rhrMax()).toList(),
          summaries.stream().map(s -> s.summary().rhrAvg()).toList(),
          summaries.stream().map(s -> s.summary().inactiveHrMin()).toList(),
          summaries.stream().map(s -> s.summary().inactiveHrMax()).toList(),
          summaries.stream().map(s -> s.summary().inactiveHrAvg()).toList(),

          // ------ Calories ----- //
          summaries.stream().map(s -> s.summary().caloriesAvg()).toList(),
          summaries.stream().map(s -> s.summary().caloriesGoal()).toList(),
          summaries.stream().map(s -> s.summary().caloriesBmrAvg()).toList(),
          summaries.stream().map(s -> s.summary().caloriesConsumedAvg()).toList(),
          summaries.stream().map(s -> s.summary().caloriesActiveAvg()).toList(),
          summaries.stream().map(s -> s.summary().activitiesCalories()).toList(),

          // ------ Weight (Keep as List<Double>) ----- //
          summaries.stream().map(s -> s.summary().weightMin()).toList(),
          summaries.stream().map(s -> s.summary().weightMax()).toList(),
          summaries.stream().map(s -> s.summary().weightAvg()).toList(),

          // ------ Hydration ----- //
          summaries.stream().map(s -> s.summary().hydrationGoal()).toList(),
          summaries.stream().map(s -> s.summary().hydrationIntake()).toList(),
          summaries.stream().map(s -> s.summary().hydrationAvg()).toList(),
          summaries.stream().map(s -> s.summary().sweatLoss()).toList(),
          summaries.stream().map(s -> s.summary().sweatLossAvg()).toList(),

          // ------ Stress & Body Battery ----- //
          summaries.stream().map(s -> s.summary().bbMin()).toList(),
          summaries.stream().map(s -> s.summary().bbMax()).toList(),
          summaries.stream().map(s -> s.summary().stressAvg()).toList(),

          // ------ Respiration & SPO2 ----- //
          summaries.stream().map(s -> s.summary().rrMin()).toList(),
          summaries.stream().map(s -> s.summary().rrMax()).toList(),
          summaries.stream().map(s -> s.summary().rrWakingAvg()).toList(),
          summaries.stream().map(s -> s.summary().spo2Min()).toList(),
          summaries.stream().map(s -> s.summary().spo2Avg()).toList(),

          // ------ Sleep (Keep as List<String>) ----- //
          summaries.stream().map(s -> s.summary().sleepMin()).toList(),
          summaries.stream().map(s -> s.summary().sleepMax()).toList(),
          summaries.stream().map(s -> s.summary().sleepAvg()).toList(),
          summaries.stream().map(s -> s.summary().remSleepMin()).toList(),
          summaries.stream().map(s -> s.summary().remSleepMax()).toList(),
          summaries.stream().map(s -> s.summary().remSleepAvg()).toList(),

          // ------ Steps & Floors ----- //
          summaries.stream().map(s -> s.summary().stepsGoal()).toList(),
          summaries.stream().map(s -> s.summary().steps()).toList(),
          summaries.stream().map(s -> s.summary().floorsGoal()).toList(),
          summaries.stream().map(s -> s.summary().floors()).toList(),

          // ------ Activities ----- //
          summaries.stream().map(s -> s.summary().activities()).toList(),
          summaries.stream().map(s -> s.summary().activitiesDistance()).toList(),
          summaries.stream().map(s -> s.summary().intensityTimeGoal()).toList(),
          summaries.stream().map(s -> s.summary().intensityTime()).toList(),
          summaries.stream().map(s -> s.summary().moderateActivityTime()).toList(),
          summaries.stream().map(s -> s.summary().vigorousActivityTime()).toList()
      );
   }
}
