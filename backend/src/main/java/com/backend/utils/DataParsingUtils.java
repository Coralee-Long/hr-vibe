package com.backend.utils;

import com.backend.models.BaseSummary;
import com.backend.models.CurrentDaySummary;
import com.backend.models.RecentDailySummaries;

import java.util.List;
import java.util.Map;

public class DataParsingUtils {

   private DataParsingUtils() {} // Prevent instantiation

   /**
    * Removes milliseconds from time formats.
    * Expected format: HH:MM:SS.000000 → Returns HH:MM:SS
    */
   public static String cleanTimeFormat(String time) {
      return (time != null && time.contains(".")) ? time.substring(0, 8) : time;
   }

   /** Safely extracts a String value, cleaning time formats if necessary. */
   public static String getString(Map<String, Object> data, String key) {
      Object value = data.getOrDefault(key, null);
      if (value == null) return null;

      String stringValue = value.toString();
      return key.contains("time") || key.contains("sleep") ? cleanTimeFormat(stringValue) : stringValue;
   }

   /** Safely extracts an Integer value, rounding if the raw data is a decimal. */
   public static Integer getInteger(Map<String, Object> data, String key) {
      try {
         Object value = data.getOrDefault(key, null);
         if (value == null) return null;

         if (value instanceof Double) {
            return (int) Math.round((Double) value); // Round decimals
         }
         return Integer.valueOf(value.toString());
      } catch (NumberFormatException e) {
         return null;
      }
   }

   /** Safely extracts a Double value. */
   public static Double getDouble(Map<String, Object> data, String key) {
      try {
         return data.getOrDefault(key, null) != null ? Double.valueOf(data.get(key).toString()) : null;
      } catch (NumberFormatException e) {
         return null;
      }
   }

   /**
    * Converts raw data into a `BaseSummary` object.
    */
   public static BaseSummary mapToBaseSummary(Map<String, Object> data) {
      return new BaseSummary(
          getInteger(data, "hr_min"),
          getInteger(data, "hr_max"),
          getInteger(data, "hr_avg"),

          getInteger(data, "rhr_min"),
          getInteger(data, "rhr_max"),
          getInteger(data, "rhr_avg"),

          getInteger(data, "inactive_hr_min"),
          getInteger(data, "inactive_hr_max"),
          getInteger(data, "inactive_hr_avg"),

          getInteger(data, "calories_avg"),
          getInteger(data, "calories_goal"),
          getInteger(data, "calories_bmr_avg"),
          getInteger(data, "calories_consumed_avg"),
          getInteger(data, "calories_active_avg"),
          getInteger(data, "activities_calories"),

          getDouble(data, "weight_min"),
          getDouble(data, "weight_max"),
          getDouble(data, "weight_avg"),

          getInteger(data, "hydration_goal"),
          getInteger(data, "hydration_intake"),
          getInteger(data, "hydration_avg"),
          getInteger(data, "sweat_loss"),
          getInteger(data, "sweat_loss_avg"),

          getInteger(data, "bb_min"),
          getInteger(data, "bb_max"),
          getInteger(data, "stress_avg"),

          getInteger(data, "rr_min"),
          getInteger(data, "rr_max"),
          getInteger(data, "rr_waking_avg"),

          getString(data, "sleep_min"),
          getString(data, "sleep_max"),
          getString(data, "sleep_avg"),

          getString(data, "rem_sleep_min"),
          getString(data, "rem_sleep_max"),
          getString(data, "rem_sleep_avg"),

          getInteger(data, "spo2_min"),
          getInteger(data, "spo2_avg"),

          getInteger(data, "steps_goal"),
          getInteger(data, "steps"),
          getInteger(data, "floors_goal"),
          getInteger(data, "floors"),

          getInteger(data, "activities"),
          getDouble(data, "activities_distance"),

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
      return new RecentDailySummaries(
          null, // MongoDB will generate the ID
          summaries.getFirst().day(), // Most recent day

          summaries.stream().map(s -> s.summary().hrMin()).toList(),
          summaries.stream().map(s -> s.summary().hrMax()).toList(),
          summaries.stream().map(s -> s.summary().hrAvg()).toList(),

          summaries.stream().map(s -> s.summary().rhrMin()).toList(),
          summaries.stream().map(s -> s.summary().rhrMax()).toList(),
          summaries.stream().map(s -> s.summary().rhrAvg()).toList(),

          summaries.stream().map(s -> s.summary().inactiveHrMin()).toList(),
          summaries.stream().map(s -> s.summary().inactiveHrMax()).toList(),
          summaries.stream().map(s -> s.summary().inactiveHrAvg()).toList(),

          summaries.stream().map(s -> s.summary().caloriesAvg()).toList(),
          summaries.stream().map(s -> s.summary().caloriesGoal()).toList(),
          summaries.stream().map(s -> s.summary().caloriesBmrAvg()).toList(),
          summaries.stream().map(s -> s.summary().caloriesConsumedAvg()).toList(),
          summaries.stream().map(s -> s.summary().caloriesActiveAvg()).toList(),
          summaries.stream().map(s -> s.summary().activitiesCalories()).toList(),

          summaries.stream().map(s -> s.summary().weightMin()).toList(),
          summaries.stream().map(s -> s.summary().weightMax()).toList(),
          summaries.stream().map(s -> s.summary().weightAvg()).toList(),

          summaries.stream().map(s -> s.summary().hydrationGoal()).toList(),
          summaries.stream().map(s -> s.summary().hydrationIntake()).toList(),
          summaries.stream().map(s -> s.summary().hydrationAvg()).toList(),
          summaries.stream().map(s -> s.summary().sweatLoss()).toList(),
          summaries.stream().map(s -> s.summary().sweatLossAvg()).toList(),

          summaries.stream().map(s -> s.summary().bbMin()).toList(),
          summaries.stream().map(s -> s.summary().bbMax()).toList(),
          summaries.stream().map(s -> s.summary().stressAvg()).toList(),

          summaries.stream().map(s -> s.summary().rrMin()).toList(),
          summaries.stream().map(s -> s.summary().rrMax()).toList(),
          summaries.stream().map(s -> s.summary().rrWakingAvg()).toList(),

          summaries.stream().map(s -> s.summary().sleepMin()).toList(),
          summaries.stream().map(s -> s.summary().sleepMax()).toList(),
          summaries.stream().map(s -> s.summary().sleepAvg()).toList(),

          summaries.stream().map(s -> s.summary().remSleepMin()).toList(),
          summaries.stream().map(s -> s.summary().remSleepMax()).toList(),
          summaries.stream().map(s -> s.summary().remSleepAvg()).toList(),

          summaries.stream().map(s -> s.summary().spo2Min()).toList(),
          summaries.stream().map(s -> s.summary().spo2Avg()).toList(),

          summaries.stream().map(s -> s.summary().stepsGoal()).toList(),
          summaries.stream().map(s -> s.summary().steps()).toList(),
          summaries.stream().map(s -> s.summary().floorsGoal()).toList(),
          summaries.stream().map(s -> s.summary().floors()).toList(),

          summaries.stream().map(s -> s.summary().activities()).toList(),
          summaries.stream().map(s -> s.summary().activitiesDistance()).toList(),

          summaries.stream().map(s -> s.summary().intensityTimeGoal()).toList(),
          summaries.stream().map(s -> s.summary().intensityTime()).toList(),
          summaries.stream().map(s -> s.summary().moderateActivityTime()).toList(),
          summaries.stream().map(s -> s.summary().vigorousActivityTime()).toList()
      );
   }
}
