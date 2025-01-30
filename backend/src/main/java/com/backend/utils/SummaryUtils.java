package com.backend.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SummaryUtils {

   private static final Logger logger = LoggerFactory.getLogger(SummaryUtils.class);

   private SummaryUtils() {} // Prevent instantiation

   /**
    * Extracts the latest data row based on the given date column.
    *
    * @param rawData List of data rows from the SQLite database.
    * @param dateColumn The column name containing date values.
    * @return The most recent row based on the date column.
    */
   public static Optional<Map<String, Object>> getLatestData(List<Map<String, Object>> rawData, String dateColumn) {
      try {
         return rawData.stream()
             .max(Comparator.comparing(data -> LocalDate.parse(data.get(dateColumn).toString())));
      } catch (DateTimeParseException | NullPointerException e) {
         logger.error("❌ Error parsing date for column '{}': {}", dateColumn, e.getMessage());
         return Optional.empty();
      }
   }
}
