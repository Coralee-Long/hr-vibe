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

   private static Logger logger = LoggerFactory.getLogger(SummaryUtils.class);

   private SummaryUtils() {}

   public static Optional<Map<String, Object>> getLatestData(List<Map<String, Object>> rawData, String dateColumn) {
      if (rawData == null || rawData.isEmpty()) {
         logger.error("❌ Input data list is null or empty.");
         return Optional.empty();
      }

      try {
         return rawData.stream()
             .filter(data -> {
                if (!data.containsKey(dateColumn) || data.get(dateColumn) == null) {
                   logger.error("❌ Missing or null value for column '{}'", dateColumn);
                   return false;
                }
                return true;
             })
             .filter(data -> {
                try {
                   LocalDate.parse(data.get(dateColumn).toString());
                   return true;
                } catch (DateTimeParseException e) {
                   logger.error("❌ Error parsing date for column '{}': {}", dateColumn, e.getMessage());
                   return false;
                }
             })
             .max(Comparator.comparing(data -> LocalDate.parse(data.get(dateColumn).toString())));
      } catch (Exception e) {
         logger.error("❌ Unexpected error processing column '{}': {}", dateColumn, e.getMessage());
         return Optional.empty();
      }
   }
}
