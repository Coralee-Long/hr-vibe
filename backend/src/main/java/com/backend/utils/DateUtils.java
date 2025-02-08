package com.backend.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for formatting dates.
 */
public class DateUtils {

   // Define a formatter that produces strings in the desired "yyyy-MM-dd" format.
   private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

   /**
    * Formats a LocalDate as a string in the format "yyyy-MM-dd".
    *
    * @param date the LocalDate to format.
    * @return a formatted date string, or null if the date is null.
    */
   public static String formatLocalDate(LocalDate date) {
      return (date == null) ? null : date.format(FORMATTER);
   }
}
