package com.backend.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 📌 SummaryUtilsTest - Unit tests for SummaryUtils.

 * 1️⃣ Tests for getLatestData():
 *    - ✅ givenValidData_whenGetLatestData_thenReturnsLatestEntry
 *    - ❌ givenEmptyList_whenGetLatestData_thenReturnsEmptyAndLogsError
 *    - ❌ givenNullList_whenGetLatestData_thenReturnsEmptyAndLogsError
 *    - ❌ givenMissingDateColumn_whenGetLatestData_thenReturnsEmptyAndLogsError
 *    - ❌ givenInvalidDateFormat_whenGetLatestData_thenReturnsEmptyAndLogsError
 *    - ✅ givenNullDateValues_whenGetLatestData_thenIgnoresNullsAndFindsLatest
 *    - ❌ givenExceptionDuringProcessing_whenGetLatestData_thenReturnsEmptyAndLogsUnexpectedError
 */

class SummaryUtilsTest {

   private ListAppender<ILoggingEvent> logAppender;
   private ch.qos.logback.classic.Logger logger;

   @BeforeEach
   void setUp() {
      logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(SummaryUtils.class);
      logAppender = new ListAppender<>();
      logAppender.start();
      logger.addAppender(logAppender);
      logAppender.list.clear(); // Clear logs before each test
   }

   @AfterEach
   void tearDown() {
      logger.detachAppender(logAppender);
   }

   /**
    * ✅ Test Case: givenValidData_whenGetLatestData_thenReturnsLatestEntry
    */
   @Test
   void givenValidData_whenGetLatestData_thenReturnsLatestEntry() {
      List<Map<String, Object>> data = List.of(
          Map.of("day", "2025-01-30", "value", 100),
          Map.of("day", "2025-01-29", "value", 90),
          Map.of("day", "2025-01-31", "value", 110)
                                              );

      Optional<Map<String, Object>> result = SummaryUtils.getLatestData(data, "day");

      assertTrue(result.isPresent());
      assertEquals("2025-01-31", result.get().get("day"));
      assertEquals(110, result.get().get("value"));
   }

   /**
    * ❌ Test Case: givenEmptyList_whenGetLatestData_thenReturnsEmptyAndLogsError
    */
   @Test
   void givenEmptyList_whenGetLatestData_thenReturnsEmptyAndLogsError() {
      List<Map<String, Object>> data = List.of();
      Optional<Map<String, Object>> result = SummaryUtils.getLatestData(data, "day");

      assertTrue(result.isEmpty());

      // Capture log messages
      boolean logFound = logAppender.list.stream()
          .anyMatch(event -> event.getFormattedMessage().contains("❌ Input data list is null or empty."));
      assertTrue(logFound, "Expected log message not found!");
   }

   /**
    * ❌ Test Case: givenNullList_whenGetLatestData_thenReturnsEmptyAndLogsError
    */
   @Test
   void givenNullList_whenGetLatestData_thenReturnsEmptyAndLogsError() {
      Optional<Map<String, Object>> result = SummaryUtils.getLatestData(null, "day");

      assertTrue(result.isEmpty());

      boolean logFound = logAppender.list.stream()
          .anyMatch(event -> event.getFormattedMessage().contains("❌ Input data list is null or empty."));
      assertTrue(logFound, "Expected log message not found!");
   }

   /**
    * ❌ Test Case: givenMissingDateColumn_whenGetLatestData_thenReturnsEmptyAndLogsError
    */
   @Test
   void givenMissingDateColumn_whenGetLatestData_thenReturnsEmptyAndLogsError() {
      List<Map<String, Object>> data = List.of(Map.of("value", 100));

      Optional<Map<String, Object>> result = SummaryUtils.getLatestData(data, "day");

      assertTrue(result.isEmpty());

      boolean logFound = logAppender.list.stream()
          .anyMatch(event -> event.getFormattedMessage().contains("❌ Missing or null value for column 'day'"));
      assertTrue(logFound, "Expected log message not found!");
   }

   /**
    * ❌ Test Case: givenInvalidDateFormat_whenGetLatestData_thenReturnsEmptyAndLogsError
    */
   @Test
   void givenInvalidDateFormat_whenGetLatestData_thenReturnsEmptyAndLogsError() {
      List<Map<String, Object>> data = List.of(Map.of("day", "INVALID-DATE", "value", 100));

      Optional<Map<String, Object>> result = SummaryUtils.getLatestData(data, "day");

      assertTrue(result.isEmpty());

      boolean logFound = logAppender.list.stream()
          .anyMatch(event -> event.getFormattedMessage().contains("❌ Error parsing date for column 'day'"));
      assertTrue(logFound, "Expected log message not found!");
   }

   /**
    * ✅ Test Case: givenNullDateValues_whenGetLatestData_thenIgnoresNullsAndFindsLatest
    */
   @Test
   void givenNullDateValues_whenGetLatestData_thenIgnoresNullsAndFindsLatest() {
      List<Map<String, Object>> data = List.of(
          new HashMap<>(Map.of("value", 100)) {{ put("day", null); }},
          Map.of("day", "2025-01-29", "value", 90),
          Map.of("day", "2025-01-31", "value", 110)
                                              );

      Optional<Map<String, Object>> result = SummaryUtils.getLatestData(data, "day");

      assertTrue(result.isPresent());
      assertEquals("2025-01-31", result.get().get("day"));
      assertEquals(110, result.get().get("value"));
   }

   /**
    * ❌ Test Case: givenExceptionDuringProcessing_whenGetLatestData_thenReturnsEmptyAndLogsUnexpectedError
    */
   @Test
   void givenExceptionDuringProcessing_whenGetLatestData_thenReturnsEmptyAndLogsUnexpectedError() {
      // GIVEN a map that has a key "day" but throws an exception when get("day") is called
      Map<String, Object> faultyMap = new HashMap<>() {
         @Override
         public Object get(Object key) {
            if ("day".equals(key)) {
               throw new RuntimeException("Forced exception");
            }
            return super.get(key);
         }
      };
      faultyMap.put("day", "dummyValue"); // Ensure containsKey("day") returns true
      faultyMap.put("value", 123);
      List<Map<String, Object>> data = List.of(faultyMap);

      Optional<Map<String, Object>> result = SummaryUtils.getLatestData(data, "day");
      assertTrue(result.isEmpty());

      boolean logFound = logAppender.list.stream()
          .anyMatch(event -> event.getFormattedMessage().contains("❌ Unexpected error processing column 'day'"));
      assertTrue(logFound, "Expected unexpected error log message not found!");
   }
}
