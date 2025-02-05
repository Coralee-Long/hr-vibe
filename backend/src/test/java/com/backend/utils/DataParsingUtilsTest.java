package com.backend.utils;

import com.backend.exceptions.GarminDataParsingException;
import com.backend.models.BaseSummary;
import com.backend.models.CurrentDaySummary;
import com.backend.models.RecentDailySummaries;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class DataParsingUtilsTest {

   // 1. --------------------------- cleanTimeFormat() --------------------------- //
   /**
    * ✅ Test `cleanTimeFormat()` correctly removes milliseconds.
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
    * ✅ Test `cleanTimeFormat()` returns the same string if no milliseconds are present.
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
    * ✅ Test `cleanTimeFormat()` when input is null.
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

   // 2. --------------------------- getString() --------------------------- //

   /**
    * ✅ Test `getString()` correctly extracts a string value from a map.
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
    * ✅ Test `getString()` returns null if the key is missing.
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
    * ✅ Test `getString()` when value is null in the map.
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

   // 3. --------------------------- getInteger() --------------------------- //

   /**
    * ✅ Test `getInteger()` correctly extracts an integer.
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
    * ✅ Test `getInteger()` rounds a double value correctly.
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
    *  ✅ Test `getInteger()` when value is null.
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

   // 4. --------------------------- getDouble() --------------------------- //

   /**
    * ✅ Test `getDouble()` correctly extracts a double.
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
    * ✅ Test `getDouble()` returns null for invalid data instead of throwing an exception.
    */
   @Test
   void givenInvalidDoubleString_whenGetDouble_thenReturnsNull() {
      // GIVEN a map with a non-numeric string
      Map<String, Object> data = new HashMap<>();
      data.put("weight", "not_a_number");

      // WHEN calling getDouble
      Double result = DataParsingUtils.getDouble(data, "weight");

      // THEN it should return null (not throw an exception)
      assertNull(result);
   }

   /**
    *  ✅ Test `getDouble()` when value is null.
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

   // 5. --------------------------- mapToBaseSummary() --------------------------- //

   /**
    * ✅ Test `mapToBaseSummary()` correctly maps a data map into a `BaseSummary`.
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
    * ✅ Test `mapToBaseSummary()` when input map is null.
    * Expected: Should throw GarminDataParsingException.
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
    * ✅ Test `mapToBaseSummary()` when some fields in the input map are null.
    * Expected: Should return a valid BaseSummary with nulls in missing fields.
    */
   @Test
   void givenPartialNullData_whenMapToBaseSummary_thenHandlesGracefully() {
      // GIVEN a map with some null values
      Map<String, Object> data = new HashMap<>();
      data.put("hr_min", 50);
      data.put("hr_max", null);  // This field is missing
      data.put("hr_avg", 75);

      // WHEN calling mapToBaseSummary
      BaseSummary summary = DataParsingUtils.mapToBaseSummary(data);

      // THEN valid values should be correctly assigned
      assertEquals(50, summary.hrMin());
      assertEquals(75, summary.hrAvg());

      // THEN missing values should be mapped as null, not cause an error
      assertNull(summary.hrMax());
   }

   // 6. --------------------------- mapToRecentDailySummaries() --------------------------- //

   /**
    * ✅ Test `mapToRecentDailySummaries()` correctly maps a list of 7 summaries.
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

      // THEN all lists should contain exactly 7 values (one per day)
      assertEquals(7, result.hrMin().size());
      assertEquals(7, result.hrMax().size());
      assertEquals(7, result.hrAvg().size());

      // THEN check if values are mapped correctly
      assertEquals(50, result.hrMin().get(0)); // First day (latest)
      assertEquals(56, result.hrMin().get(6)); // Last day (oldest)
   }

   /**
    * ✅ Test `mapToRecentDailySummaries()` throws `GarminDataParsingException`
    * when given an empty list.
    */
   @Test
   void givenEmptyList_whenMapToRecentDailySummaries_thenThrowsGarminException() {
      // GIVEN an empty list
      List<CurrentDaySummary> summaries = List.of();

      // WHEN mapping to RecentDailySummaries THEN it should throw GarminDataParsingException
      Exception exception = assertThrows(GarminDataParsingException.class,
                                         () -> DataParsingUtils.mapToRecentDailySummaries(summaries));

      // THEN verify exception message contains the expected text
      assertNotNull(exception); // Ensure exception is not null
      assertTrue(exception.getMessage().contains("Summaries list cannot be null or empty"));
   }


   /**
    * ✅ Test `mapToRecentDailySummaries()` throws `GarminDataParsingException`
    * when given a null list.
    */
   @Test
   void givenNullList_whenMapToRecentDailySummaries_thenThrowsGarminException() {
      // GIVEN a null input list
      List<CurrentDaySummary> summaries = null;

      // WHEN calling mapToRecentDailySummaries THEN it should throw GarminDataParsingException
      Exception exception = assertThrows(GarminDataParsingException.class,
                                         () -> DataParsingUtils.mapToRecentDailySummaries(summaries));

      // THEN verify exception message contains the expected text
      assertNotNull(exception); // Ensure exception is not null
      assertTrue(exception.getMessage().contains("Summaries list cannot be null or empty"));
   }


   /**
    * ✅ Test `mapToRecentDailySummaries()` when some fields inside records are null.
    * Expected: Should return a valid RecentDailySummaries with nulls where applicable.
    */
   @Test
   void givenSummariesWithNullFields_whenMapToRecentDailySummaries_thenHandlesGracefully() {
      // GIVEN a list with some summaries having null fields
      List<CurrentDaySummary> summaries = List.of(
          new CurrentDaySummary(null, LocalDate.of(2025, 1, 30), createMockBaseSummary(50, 100, null)), // hrAvg is null
          new CurrentDaySummary(null, LocalDate.of(2025, 1, 29), createMockBaseSummary(null, 101, 76)), // hrMin is null
          new CurrentDaySummary(null, LocalDate.of(2025, 1, 28), createMockBaseSummary(52, null, 77))  // hrMax is null
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
      assertEquals(50, result.hrMin().get(0));  // First day hrMin is 50
      assertNull(result.hrMin().get(1));        // Second day hrMin was null
      assertEquals(52, result.hrMin().get(2));  // Third day hrMin is 52

      assertEquals(100, result.hrMax().get(0)); // First day hrMax is 100
      assertEquals(101, result.hrMax().get(1)); // Second day hrMax is 101
      assertNull(result.hrMax().get(2));        // Third day hrMax was null

      assertNull(result.hrAvg().get(0));        // First day hrAvg was null
      assertEquals(76, result.hrAvg().get(1));  // Second day hrAvg is 76
      assertEquals(77, result.hrAvg().get(2));  // Third day hrAvg is 77
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