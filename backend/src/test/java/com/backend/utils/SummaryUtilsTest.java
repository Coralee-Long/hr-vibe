package com.backend.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SummaryUtilsTest {

   private Logger mockLogger;

   @BeforeEach
   void setUp() {
      mockLogger = mock(Logger.class);
      SummaryUtils.setLogger(mockLogger); // Inject mock logger
   }

   @AfterEach
   void tearDown() {
      SummaryUtils.setLogger(LoggerFactory.getLogger(SummaryUtils.class)); // Reset after test
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
      verify(mockLogger).error("❌ Input data list is null or empty.");
   }

   @Test
   void givenNullList_whenGetLatestData_thenReturnsEmptyAndLogsError() {
      Optional<Map<String, Object>> result = SummaryUtils.getLatestData(null, "day");

      assertTrue(result.isEmpty());
      verify(mockLogger).error("❌ Input data list is null or empty.");
   }

   @Test
   void givenMissingDateColumn_whenGetLatestData_thenReturnsEmptyAndLogsError() {
      List<Map<String, Object>> data = List.of(Map.of("value", 100));

      Optional<Map<String, Object>> result = SummaryUtils.getLatestData(data, "day");

      assertTrue(result.isEmpty());
      verify(mockLogger).error("❌ Missing or null value for column '{}'", "day");
   }

   @Test
   void givenInvalidDateFormat_whenGetLatestData_thenReturnsEmptyAndLogsError() {
      // GIVEN data with invalid date format
      List<Map<String, Object>> data = List.of(
          Map.of("day", "INVALID-DATE", "value", 100)
                                              );

      // Reset the logger to avoid conflicts
      reset(mockLogger);

      // WHEN calling getLatestData
      Optional<Map<String, Object>> result = SummaryUtils.getLatestData(data, "day");

      // THEN it should return Optional.empty()
      assertTrue(result.isEmpty());

      // Ensure some error was logged
      verify(mockLogger, atLeastOnce()).error(anyString(), any(), any());
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
