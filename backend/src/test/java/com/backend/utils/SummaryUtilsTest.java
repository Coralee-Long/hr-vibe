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


class SummaryUtilsTest {

   private ListAppender<ILoggingEvent> logAppender;
   private ch.qos.logback.classic.Logger logger;

   @BeforeEach
   void setUp() {
      logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(SummaryUtils.class);
      logAppender = new ListAppender<>();
      logAppender.start();
      logger.addAppender(logAppender);
      logAppender.list.clear(); // Ensure logs are cleared before each test
   }

   @AfterEach
   void tearDown() {
      logger.detachAppender(logAppender);
   }

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

   @Test
   void givenNullList_whenGetLatestData_thenReturnsEmptyAndLogsError() {
      Optional<Map<String, Object>> result = SummaryUtils.getLatestData(null, "day");

      assertTrue(result.isEmpty());

      boolean logFound = logAppender.list.stream()
          .anyMatch(event -> event.getFormattedMessage().contains("❌ Input data list is null or empty."));
      assertTrue(logFound, "Expected log message not found!");
   }

   @Test
   void givenMissingDateColumn_whenGetLatestData_thenReturnsEmptyAndLogsError() {
      List<Map<String, Object>> data = List.of(Map.of("value", 100));

      Optional<Map<String, Object>> result = SummaryUtils.getLatestData(data, "day");

      assertTrue(result.isEmpty());

      boolean logFound = logAppender.list.stream()
          .anyMatch(event -> event.getFormattedMessage().contains("❌ Missing or null value for column 'day'"));
      assertTrue(logFound, "Expected log message not found!");
   }

   @Test
   void givenInvalidDateFormat_whenGetLatestData_thenReturnsEmptyAndLogsError() {
      List<Map<String, Object>> data = List.of(Map.of("day", "INVALID-DATE", "value", 100));

      Optional<Map<String, Object>> result = SummaryUtils.getLatestData(data, "day");

      assertTrue(result.isEmpty());

      boolean logFound = logAppender.list.stream()
          .anyMatch(event -> event.getFormattedMessage().contains("❌ Error parsing date for column 'day'"));
      assertTrue(logFound, "Expected log message not found!");
   }

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
}
