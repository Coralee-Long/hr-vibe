package com.backend.utils;

import com.backend.exceptions.GarminDataParsingException;
import com.backend.exceptions.JsonParsingException;
import com.backend.models.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 📌 DataParsingUtilsTest - Unit tests for DataParsingUtils.

 * 1️⃣ Tests for cleanTimeFormat():
 *    - ✅ givenTimeWithMilliseconds_whenCleanTimeFormat_thenRemovesMilliseconds
 *    - ✅ givenCleanTime_whenCleanTimeFormat_thenReturnsSameString
 *    - ❌ givenNullTime_whenCleanTimeFormat_thenReturnsNull

 * 2️⃣ Tests for getString():
 *    - ✅ givenValidStringValue_whenGetString_thenReturnsString
 *    - ❌ givenMissingKey_whenGetString_thenReturnsNull
 *    - ❌ givenNullValueInMap_whenGetString_thenReturnsNull

 * 3️⃣ Tests for getInteger():
 *    - ✅ givenValidIntegerValue_whenGetInteger_thenReturnsInteger
 *    - ✅ givenDoubleValue_whenGetInteger_thenRoundsToNearestInteger
 *    - ❌ givenNullValueInMap_whenGetInteger_thenReturnsNull

 * 4️⃣ Tests for getDouble():
 *    - ✅ givenValidDoubleValue_whenGetDouble_thenReturnsDouble
 *    - ❌ givenInvalidDoubleString_whenGetDouble_thenReturnsNull
 *    - ❌ givenNullValueInMap_whenGetDouble_thenReturnsNull

 * 5️⃣ Tests for mapToBaseSummary():
 *    - ✅ givenValidDataMap_whenMapToBaseSummary_thenReturnsCorrectBaseSummary
 *    - ❌ givenNullMap_whenMapToBaseSummary_thenThrowsException
 *    - ❌ givenPartialNullData_whenMapToBaseSummary_thenHandlesGracefully

 * 6️⃣ Tests for mapToCurrentDaySummary():
 *    - ✅ givenValidDataMap_whenMapToCurrentDaySummary_thenReturnsCurrentDaySummary

 * 7️⃣ Tests for mapToWeeklySummary():
 *    - ✅ givenValidDataMap_whenMapToWeeklySummary_thenReturnsWeeklySummary

 * 8️⃣ Tests for mapToMonthlySummary():
 *    - ✅ givenValidDataMap_whenMapToMonthlySummary_thenReturnsMonthlySummary (date normalized to the 1st)

 * 9️⃣ Tests for mapToYearlySummary():
 *    - ✅ givenValidDataMap_whenMapToYearlySummary_thenReturnsYearlySummary

 * 🔟 Tests for mapToRecentDailySummaries():
 *    - ✅ givenSevenCurrentDaySummaries_whenMapToRecentDailySummaries_thenReturnsCorrectModel
 *    - ❌ givenEmptyList_whenMapToRecentDailySummaries_thenThrowsGarminException
 *    - ❌ givenNullList_whenMapToRecentDailySummaries_thenThrowsGarminException
 *    - ❌ givenSummariesWithNullFields_whenMapToRecentDailySummaries_thenHandlesGracefully

 * 1️⃣1️⃣ Tests for JsonUtils.parseJsonToList():
 *    - ✅ givenValidJson_whenParseJsonToList_thenReturnsListOfMaps
 *    - ❌ givenInvalidJson_whenParseJsonToList_thenThrowsJsonParsingException
 */
class DataParsingUtilsTest {

   /**
    * ✅ Test Case: givenTimeWithMilliseconds_whenCleanTimeFormat_thenRemovesMilliseconds
    */
   @Test
   void givenTimeWithMilliseconds_whenCleanTimeFormat_thenRemovesMilliseconds() {
      // GIVEN a time string with milliseconds
      String rawTime = "12:34:56.789000";
      // WHEN the method is called
      String cleanedTime = DataParsingUtils.cleanTimeFormat(rawTime);
      // THEN it should return the time without milliseconds
      assertEquals("12:34:56", cleanedTime);
   }

   /**
    * ✅ Test Case: givenCleanTime_whenCleanTimeFormat_thenReturnsSameString
    */
   @Test
   void givenCleanTime_whenCleanTimeFormat_thenReturnsSameString() {
      // GIVEN a properly formatted time string
      String rawTime = "12:34:56";
      // WHEN the method is called
      String cleanedTime = DataParsingUtils.cleanTimeFormat(rawTime);
      // THEN it should remain unchanged
      assertEquals("12:34:56", cleanedTime);
   }

   /**
    * ❌ Test Case: givenNullTime_whenCleanTimeFormat_thenReturnsNull
    */
   @Test
   void givenNullTime_whenCleanTimeFormat_thenReturnsNull() {
      // GIVEN a null input
      String rawTime = null;
      // WHEN the method is called
      String cleanedTime = DataParsingUtils.cleanTimeFormat(rawTime);
      // THEN it should return null
      assertNull(cleanedTime);
   }

   /**
    * ✅ Test Case: givenValidStringValue_whenGetString_thenReturnsString
    */
   @Test
   void givenValidStringValue_whenGetString_thenReturnsString() {
      // GIVEN a map with a valid string entry
      Map<String, Object> data = new HashMap<>();
      data.put("name", "Garmin");
      // WHEN the method is called
      String result = DataParsingUtils.getString(data, "name");
      // THEN it should return the correct string
      assertEquals("Garmin", result);
   }

   /**
    * ❌ Test Case: givenMissingKey_whenGetString_thenReturnsNull
    */
   @Test
   void givenMissingKey_whenGetString_thenReturnsNull() {
      // GIVEN an empty map
      Map<String, Object> data = new HashMap<>();
      // WHEN the method is called with a non-existing key
      String result = DataParsingUtils.getString(data, "name");
      // THEN it should return null
      assertNull(result);
   }

   /**
    * ❌ Test Case: givenNullValueInMap_whenGetString_thenReturnsNull
    */
   @Test
   void givenNullValueInMap_whenGetString_thenReturnsNull() {
      // GIVEN a map where the key exists but has a null value
      Map<String, Object> data = new HashMap<>();
      data.put("testKey", null);
      // WHEN calling getString
      String result = DataParsingUtils.getString(data, "testKey");
      // THEN it should return null
      assertNull(result);
   }

   /**
    * ✅ Test Case: givenValidIntegerValue_whenGetInteger_thenReturnsInteger
    */
   @Test
   void givenValidIntegerValue_whenGetInteger_thenReturnsInteger() {
      // GIVEN a map with a valid integer value
      Map<String, Object> data = new HashMap<>();
      data.put("hr_min", 50);
      // WHEN the method is called
      Integer result = DataParsingUtils.getInteger(data, "hr_min");
      // THEN it should return the correct integer
      assertEquals(50, result);
   }

   /**
    * ✅ Test Case: givenDoubleValue_whenGetInteger_thenRoundsToNearestInteger
    */
   @Test
   void givenDoubleValue_whenGetInteger_thenRoundsToNearestInteger() {
      // GIVEN a map with a double value
      Map<String, Object> data = new HashMap<>();
      data.put("hr_min", 50.6);
      // WHEN the method is called
      Integer result = DataParsingUtils.getInteger(data, "hr_min");
      // THEN it should return the rounded integer
      assertEquals(51, result);
   }

   /**
    * ❌ Test Case: givenNullValueInMap_whenGetInteger_thenReturnsNull
    */
   @Test
   void givenNullValueInMap_whenGetInteger_thenReturnsNull() {
      // GIVEN a map where the key exists but has a null value
      Map<String, Object> data = new HashMap<>();
      data.put("hr_min", null);
      // WHEN calling getInteger
      Integer result = DataParsingUtils.getInteger(data, "hr_min");
      // THEN it should return null
      assertNull(result);
   }

   /**
    * ✅ Test Case: givenValidDoubleValue_whenGetDouble_thenReturnsDouble
    */
   @Test
   void givenValidDoubleValue_whenGetDouble_thenReturnsDouble() {
      // GIVEN a map with a valid double value
      Map<String, Object> data = new HashMap<>();
      data.put("weight", 72.5);
      // WHEN the method is called
      Double result = DataParsingUtils.getDouble(data, "weight");
      // THEN it should return the correct double
      assertEquals(72.5, result);
   }

   /**
    * ❌ Test Case: givenInvalidDoubleString_whenGetDouble_thenReturnsNull
    */
   @Test
   void givenInvalidDoubleString_whenGetDouble_thenReturnsNull() {
      // GIVEN a map with a non-numeric string
      Map<String, Object> data = new HashMap<>();
      data.put("weight", "not_a_number");
      // WHEN calling getDouble
      Double result = DataParsingUtils.getDouble(data, "weight");
      // THEN it should return null (and not throw an exception)
      assertNull(result);
   }

   /**
    * ❌ Test Case: givenNullValueInMap_whenGetDouble_thenReturnsNull
    */
   @Test
   void givenNullValueInMap_whenGetDouble_thenReturnsNull() {
      // GIVEN a map where the key exists but has a null value
      Map<String, Object> data = new HashMap<>();
      data.put("weight", null);
      // WHEN calling getDouble
      Double result = DataParsingUtils.getDouble(data, "weight");
      // THEN it should return null
      assertNull(result);
   }

   /**
    * ✅ Test Case: givenValidDataMap_whenMapToBaseSummary_thenReturnsCorrectBaseSummary
    */
   @Test
   void givenValidDataMap_whenMapToBaseSummary_thenReturnsCorrectBaseSummary() {
      // GIVEN a map with valid data
      Map<String, Object> data = new HashMap<>();
      data.put("hr_min", 50);
      data.put("hr_max", 100);
      data.put("hr_avg", 75);
      data.put("calories_avg", 2000);
      data.put("weight_min", 70.5);
      data.put("weight_max", 72.5);
      data.put("weight_avg", 71.5);
      // WHEN the method is called
      BaseSummary summary = DataParsingUtils.mapToBaseSummary(data);
      // THEN it should correctly populate the BaseSummary object
      assertEquals(50, summary.hrMin());
      assertEquals(100, summary.hrMax());
      assertEquals(75, summary.hrAvg());
      assertEquals(2000, summary.caloriesAvg());
      assertEquals(70.5, summary.weightMin());
      assertEquals(72.5, summary.weightMax());
      assertEquals(71.5, summary.weightAvg());
   }

   /**
    * ❌ Test Case: givenNullMap_whenMapToBaseSummary_thenThrowsException
    */
   @Test
   void givenNullMap_whenMapToBaseSummary_thenThrowsException() {
      // GIVEN a null input
      Map<String, Object> data = null;
      // WHEN calling mapToBaseSummary THEN it should throw GarminDataParsingException
      assertThrows(GarminDataParsingException.class,
                   () -> DataParsingUtils.mapToBaseSummary(data));
   }

   /**
    * ❌ Test Case: givenPartialNullData_whenMapToBaseSummary_thenHandlesGracefully
    */
   @Test
   void givenPartialNullData_whenMapToBaseSummary_thenHandlesGracefully() {
      // GIVEN a map with some null values
      Map<String, Object> data = new HashMap<>();
      data.put("hr_min", 50);
      data.put("hr_max", null);  // Missing field
      data.put("hr_avg", 75);
      // WHEN calling mapToBaseSummary
      BaseSummary summary = DataParsingUtils.mapToBaseSummary(data);
      // THEN valid values should be correctly assigned
      assertEquals(50, summary.hrMin());
      assertEquals(75, summary.hrAvg());
      // THEN missing values should be mapped as null
      assertNull(summary.hrMax());
   }

   /**
    * ✅ Test Case: givenValidDataMap_whenMapToCurrentDaySummary_thenReturnsCurrentDaySummary
    */
   @Test
   void givenValidDataMap_whenMapToCurrentDaySummary_thenReturnsCurrentDaySummary() {
      // GIVEN a map with valid data for a current day summary
      Map<String, Object> data = new HashMap<>();
      data.put("day", "2025-01-15");
      data.put("hr_min", 50);
      data.put("hr_max", 100);
      data.put("hr_avg", 75);
      // WHEN calling mapToCurrentDaySummary
      CurrentDaySummary result = DataParsingUtils.mapToCurrentDaySummary(data);
      // THEN it should return a valid CurrentDaySummary
      assertNotNull(result);
      assertEquals(LocalDate.parse("2025-01-15"), result.day());
   }

   /**
    * ✅ Test Case: givenValidDataMap_whenMapToWeeklySummary_thenReturnsWeeklySummary
    */
   @Test
   void givenValidDataMap_whenMapToWeeklySummary_thenReturnsWeeklySummary() {
      // GIVEN a map with valid data for a weekly summary
      Map<String, Object> data = new HashMap<>();
      data.put("first_day", "2025-01-20");
      data.put("hr_min", 50);
      data.put("hr_max", 100);
      data.put("hr_avg", 75);
      // WHEN calling mapToWeeklySummary
      WeeklySummary result = DataParsingUtils.mapToWeeklySummary(data);
      // THEN it should return a valid WeeklySummary
      assertNotNull(result);
      assertEquals(LocalDate.parse("2025-01-20"), result.firstDay());
   }

   /**
    * ✅ Test Case: givenValidDataMap_whenMapToMonthlySummary_thenReturnsMonthlySummary
    * (Note: The date is normalized to the 1st of the month)
    */
   @Test
   void givenValidDataMap_whenMapToMonthlySummary_thenReturnsMonthlySummary() {
      // GIVEN a map with valid data for a monthly summary
      Map<String, Object> data = new HashMap<>();
      data.put("first_day", "2025-01-15");
      data.put("hr_min", 50);
      data.put("hr_max", 100);
      data.put("hr_avg", 75);
      // WHEN calling mapToMonthlySummary
      MonthlySummary result = DataParsingUtils.mapToMonthlySummary(data);
      // THEN it should return a valid MonthlySummary with date normalized to the first day of the month
      assertNotNull(result);
      assertEquals(LocalDate.parse("2025-01-01"), result.firstDay());
   }

   /**
    * ✅ Test Case: givenValidDataMap_whenMapToYearlySummary_thenReturnsYearlySummary
    */
   @Test
   void givenValidDataMap_whenMapToYearlySummary_thenReturnsYearlySummary() {
      // GIVEN a map with valid data for a yearly summary
      Map<String, Object> data = new HashMap<>();
      data.put("first_day", "2025-05-15");
      data.put("hr_min", 50);
      data.put("hr_max", 100);
      data.put("hr_avg", 75);
      // WHEN calling mapToYearlySummary
      YearlySummary result = DataParsingUtils.mapToYearlySummary(data);
      // THEN it should return a valid YearlySummary
      assertNotNull(result);
      assertEquals(LocalDate.parse("2025-05-15"), result.firstDay());
   }

   /**
    * ✅ Test Case: givenSevenCurrentDaySummaries_whenMapToRecentDailySummaries_thenReturnsCorrectModel
    */
   @Test
   void givenSevenCurrentDaySummaries_whenMapToRecentDailySummaries_thenReturnsCorrectModel() {
      // GIVEN a list of 7 CurrentDaySummary records with mock data
      List<CurrentDaySummary> summaries = List.of(
          new CurrentDaySummary(null, LocalDate.of(2025, 1, 30), createMockBaseSummary(50, 100, 75)),
          new CurrentDaySummary(null, LocalDate.of(2025, 1, 29), createMockBaseSummary(51, 101, 76)),
          new CurrentDaySummary(null, LocalDate.of(2025, 1, 28), createMockBaseSummary(52, 102, 77)),
          new CurrentDaySummary(null, LocalDate.of(2025, 1, 27), createMockBaseSummary(53, 103, 78)),
          new CurrentDaySummary(null, LocalDate.of(2025, 1, 26), createMockBaseSummary(54, 104, 79)),
          new CurrentDaySummary(null, LocalDate.of(2025, 1, 25), createMockBaseSummary(55, 105, 80)),
          new CurrentDaySummary(null, LocalDate.of(2025, 1, 24), createMockBaseSummary(56, 106, 81))
                                                 );
      // WHEN mapping to RecentDailySummaries
      RecentDailySummaries result = DataParsingUtils.mapToRecentDailySummaries(summaries);
      // THEN the latest day should match the most recent date
      assertEquals(LocalDate.of(2025, 1, 30), result.latestDay());
      // THEN all lists should contain exactly 7 values
      assertEquals(7, result.hrMin().size());
      assertEquals(7, result.hrMax().size());
      assertEquals(7, result.hrAvg().size());
      // THEN check if values are mapped correctly (first record)
      assertEquals(50, result.hrMin().get(0));
      assertEquals(56, result.hrMin().get(6));
   }

   /**
    * ❌ Test Case: givenEmptyList_whenMapToRecentDailySummaries_thenThrowsGarminException
    */
   @Test
   void givenEmptyList_whenMapToRecentDailySummaries_thenThrowsGarminException() {
      // GIVEN an empty list
      List<CurrentDaySummary> summaries = List.of();
      // WHEN mapping to RecentDailySummaries THEN it should throw GarminDataParsingException
      Exception exception = assertThrows(GarminDataParsingException.class,
                                         () -> DataParsingUtils.mapToRecentDailySummaries(summaries));
      // THEN verify exception message contains the expected text
      assertNotNull(exception);
      assertTrue(exception.getMessage().contains("Summaries list cannot be null or empty"));
   }

   /**
    * ❌ Test Case: givenNullList_whenMapToRecentDailySummaries_thenThrowsGarminException
    */
   @Test
   void givenNullList_whenMapToRecentDailySummaries_thenThrowsGarminException() {
      // GIVEN a null input list
      List<CurrentDaySummary> summaries = null;
      // WHEN mapping to RecentDailySummaries THEN it should throw GarminDataParsingException
      Exception exception = assertThrows(GarminDataParsingException.class,
                                         () -> DataParsingUtils.mapToRecentDailySummaries(summaries));
      // THEN verify exception message contains the expected text
      assertNotNull(exception);
      assertTrue(exception.getMessage().contains("Summaries list cannot be null or empty"));
   }

   /**
    * ❌ Test Case: givenSummariesWithNullFields_whenMapToRecentDailySummaries_thenHandlesGracefully
    * Expected: Should return a valid RecentDailySummaries with nulls where applicable.
    */
   @Test
   void givenSummariesWithNullFields_whenMapToRecentDailySummaries_thenHandlesGracefully() {
      // GIVEN a list with some summaries having null fields
      List<CurrentDaySummary> summaries = List.of(
          new CurrentDaySummary(null, LocalDate.of(2025, 1, 30), createMockBaseSummary(50, 100, null)),
          new CurrentDaySummary(null, LocalDate.of(2025, 1, 29), createMockBaseSummary(null, 101, 76)),
          new CurrentDaySummary(null, LocalDate.of(2025, 1, 28), createMockBaseSummary(52, null, 77))
                                                 );
      // WHEN mapping to RecentDailySummaries
      RecentDailySummaries result = DataParsingUtils.mapToRecentDailySummaries(summaries);
      // THEN the latest day should match the most recent date
      assertEquals(LocalDate.of(2025, 1, 30), result.latestDay());
      // THEN lists should have the correct number of elements
      assertEquals(3, result.hrMin().size());
      assertEquals(3, result.hrMax().size());
      assertEquals(3, result.hrAvg().size());
      // THEN check correct handling of null values
      assertEquals(50, result.hrMin().get(0));
      assertNull(result.hrMin().get(1));
      assertEquals(52, result.hrMin().get(2));
      assertEquals(100, result.hrMax().get(0));
      assertEquals(101, result.hrMax().get(1));
      assertNull(result.hrMax().get(2));
      assertNull(result.hrAvg().get(0));
      assertEquals(76, result.hrAvg().get(1));
      assertEquals(77, result.hrAvg().get(2));
   }

   /**
    * ✅ Test Case: givenValidJson_whenParseJsonToList_thenReturnsListOfMaps
    */
   @Test
   void givenValidJson_whenParseJsonToList_thenReturnsListOfMaps() throws IOException {
      // GIVEN a valid JSON string representing a list of maps
      String json = "[{\"key\":\"value\"}, {\"number\":123}]";
      // WHEN parsing the JSON
      List<Map<String, Object>> result = DataParsingUtils.JsonUtils.parseJsonToList(json);
      // THEN it should return a non-null list with the correct size and values
      assertNotNull(result);
      assertEquals(2, result.size());
      assertEquals("value", result.get(0).get("key"));
   }

   /**
    * ❌ Test Case: givenInvalidJson_whenParseJsonToList_thenThrowsJsonParsingException
    */
   @Test
   void givenInvalidJson_whenParseJsonToList_thenThrowsJsonParsingException() {
      // GIVEN an invalid JSON string
      String invalidJson = "not a json";
      // WHEN parsing the JSON THEN it should throw JsonParsingException
      assertThrows(JsonParsingException.class, () -> DataParsingUtils.JsonUtils.parseJsonToList(invalidJson));
   }

   /**
    * 🛠️ Helper method to create mock BaseSummary objects.
    */
   private BaseSummary createMockBaseSummary(Integer hrMin, Integer hrMax, Integer hrAvg) {
      return new BaseSummary(
          hrMin, hrMax, hrAvg,
          null, null, null, // rhr values
          null, null, null, // inactive hr values
          null, null, null, null, null, null, // calories
          null, null, null, // weight
          null, null, null, null, null, // hydration
          null, null, null, // bb and stress
          null, null, null, // rr
          null, null, null, // sleep
          null, null, null, // rem sleep
          null, null, // spo2
          null, null, null, null, // steps & floors
          null, null, // activities
          null, null, null, null // intensity time
      );
   }
}
