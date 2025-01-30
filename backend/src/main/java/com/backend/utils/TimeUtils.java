package com.backend.utils;

public class TimeUtils {
   private TimeUtils() {} // Prevent instantiation

   // time format before cleaning: HH:MM:SS:000000

   public static String cleanTimeFormat(String time) {
      if (time != null && time.contains(".")) {
         return time.substring(0, 8); // Keeps only HH:MM:SS
      }
      return time;
   }
}
