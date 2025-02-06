package com.backend.services;

import com.backend.exceptions.GarminProcessingException;
import com.backend.models.*;
import com.backend.repos.MongoDB.*;
import com.backend.repos.SQL.GarminSQLiteRepo;
import com.backend.utils.DataParsingUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 📌 GarminServiceTest - Unit tests for GarminService.

 * 1️⃣ Tests for `processAndSaveSummary` (Generic Method)
 *    - ✅ `givenValidData_whenProcessAndSaveSummary_thenSavesSuccessfully`
 *    - ❌ `givenNoData_whenProcessAndSaveSummary_thenThrowsException`
 *    - ❌ `givenInvalidData_whenProcessAndSaveSummary_thenThrowsValidationException`
 *    - ❌ `givenDatabaseException_whenProcessAndSaveSummary_thenThrowsProcessingException`

 * 2️⃣ Tests for `processAndSaveCurrentDaySummary`
 *    - ✅ `givenValidData_whenProcessAndSaveCurrentDaySummary_thenSavesSuccessfully`
 *    - ❌ `givenNoData_whenProcessAndSaveCurrentDaySummary_thenThrowsException`

 * 3️⃣ Tests for `processAndSaveWeeklySummary`
 *    - ✅ `givenValidData_whenProcessAndSaveWeeklySummary_thenSavesSuccessfully`
 *    - ❌ `givenNoData_whenProcessAndSaveWeeklySummary_thenThrowsException`

 * 4️⃣ Tests for `processAndSaveMonthlySummary`
 *    - ✅ `givenValidData_whenProcessAndSaveMonthlySummary_thenSavesSuccessfully`
 *    - ❌ `givenNoData_whenProcessAndSaveMonthlySummary_thenThrowsException`

 * 5️⃣ Tests for `processAndSaveYearlySummary`
 *    - ✅ `givenValidData_whenProcessAndSaveYearlySummary_thenSavesSuccessfully`
 *    - ❌ `givenNoData_whenProcessAndSaveYearlySummary_thenThrowsException`

 * 6️⃣ Tests for `processAndSaveRecentDailySummaries`
 *    - ✅ `givenValidData_whenProcessAndSaveRecentDailySummaries_thenSavesSuccessfully`
 *    - ❌ `givenNoData_whenProcessAndSaveRecentDailySummaries_thenLogsWarningAndSkipsSaving`
 */

@ExtendWith(MockitoExtension.class)
class GarminServiceTest {

   @Mock
   private GarminSQLiteRepo garminSQLiteRepo;

   @Mock
   private CurrentDaySummaryRepo currentDaySummaryRepo;

   @Mock
   private RecentDailySummariesRepo recentDailySummariesRepo;

   @Mock
   private WeeklySummaryRepo weeklySummaryRepo;

   @Mock MonthlySummaryRepo monthlySummaryRepo;

   @Mock YearlySummaryRepo yearlySummaryRepo;

   @Mock
   private ValidationService validationService;

   @InjectMocks
   private GarminService garminService;

   // Fields to hold the parsed mock data
   private List<Map<String, Object>> mockSQLiteDataDay;
   private List<Map<String, Object>> mockSQLiteDataWeek;
   private List<Map<String, Object>> mockSQLiteDataMonth;
   private List<Map<String, Object>> mockSQLiteDataYear;

   @BeforeEach
   void setUp() throws IOException {
      // Reset mocks if needed
      // (Assuming you're using Mockito's reset() as required)
      // reset(garminSQLiteRepo, currentDaySummaryRepo, weeklySummaryRepo, monthlySummaryRepo, yearlySummaryRepo, validationService);

      // Load daily summary mock data
      String jsonDayData = new String(Files.readAllBytes(Paths.get("src/test/resources/sqlite_mock_days_summary.json")));
      mockSQLiteDataDay = DataParsingUtils.JsonUtils.parseJsonToList(jsonDayData);

      // Load weekly summary mock data
      String jsonWeekData = new String(Files.readAllBytes(Paths.get("src/test/resources/sqlite_mock_weeks_summary.json")));
      mockSQLiteDataWeek = DataParsingUtils.JsonUtils.parseJsonToList(jsonWeekData);

      // Load monthly summary mock data
      String jsonMonthData = new String(Files.readAllBytes(Paths.get("src/test/resources/sqlite_mock_months_summary.json")));
      mockSQLiteDataMonth = DataParsingUtils.JsonUtils.parseJsonToList(jsonMonthData);

      // Load yearly summary mock data
      String jsonYearData = new String(Files.readAllBytes(Paths.get("src/test/resources/sqlite_mock_years_summary.json")));
      mockSQLiteDataYear = DataParsingUtils.JsonUtils.parseJsonToList(jsonYearData);
   }


   /**
    * ✅ Test Case: Given valid data, when processAndSaveSummary is called, then data is saved successfully.
    */
   @Test
   void givenValidData_whenProcessAndSaveSummary_thenSavesSuccessfully() {
      String databaseName = "testDB";
      String tableName = "daily_summary";


      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(mockSQLiteDataDay);

      garminService.processAndSaveCurrentDaySummary(databaseName, tableName);

      // Capture the saved entity
      ArgumentCaptor<CurrentDaySummary> captor = ArgumentCaptor.forClass(CurrentDaySummary.class);
      verify(currentDaySummaryRepo).save(captor.capture());

      CurrentDaySummary savedSummary = captor.getValue();
      assertNotNull(savedSummary);

      // Ensure validation was called before saving
      verify(validationService).validate(savedSummary);
   }

   /**
    * ❌ Test Case: Given no data, when processAndSaveSummary is called, then an exception is thrown.
    */
   @Test
   void givenNoData_whenProcessAndSaveSummary_thenThrowsException() {
      String databaseName = "testDB";
      String tableName = "daily_summary";

      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(List.of());

      GarminProcessingException exception = assertThrows(GarminProcessingException.class, () ->
          garminService.processAndSaveCurrentDaySummary(databaseName, tableName));

      assertEquals("No data found in table: daily_summary", exception.getMessage());
   }

   /**
    * ❌ Test Case: Given invalid data, when processAndSaveSummary is called, then validation exception is thrown.
    */
   @Test
   void givenInvalidData_whenProcessAndSaveSummary_thenThrowsValidationException() {
      String databaseName = "testDB";
      String tableName = "daily_summary";

      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(mockSQLiteDataDay);
      doThrow(new RuntimeException("Validation failed"))
          .when(validationService).validate(any(CurrentDaySummary.class));

      assertThrows(RuntimeException.class, () ->
          garminService.processAndSaveCurrentDaySummary(databaseName, tableName));
   }

   /**
    * ❌ Test Case: Given a database exception, when processAndSaveSummary is called, then GarminProcessingException is thrown.
    */
   @Test
   void givenDatabaseException_whenProcessAndSaveSummary_thenThrowsProcessingException() {
      String databaseName = "testDB";
      String tableName = "daily_summary";

      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenThrow(new RuntimeException("Database error"));

      assertThrows(GarminProcessingException.class, () ->
          garminService.processAndSaveCurrentDaySummary(databaseName, tableName));
   }

   /**
    * ✅ Test Case: Given valid data, when processAndSaveCurrentDaySummary is called, then data is saved successfully.
    */
   @Test
   void givenValidData_whenProcessAndSaveCurrentDaySummary_thenSavesSuccessfully() {
      String databaseName = "testDB";
      String tableName = "daily_summary";

      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(mockSQLiteDataDay);

      garminService.processAndSaveCurrentDaySummary(databaseName, tableName);

      // Capture the saved entity
      ArgumentCaptor<CurrentDaySummary> captor = ArgumentCaptor.forClass(CurrentDaySummary.class);
      verify(currentDaySummaryRepo).save(captor.capture());

      CurrentDaySummary savedSummary = captor.getValue();
      assertNotNull(savedSummary);

      // Ensure validation was called before saving
      verify(validationService).validate(savedSummary);
   }

   /**
    * ❌ Test Case: Given no data, when processAndSaveCurrentDaySummary is called, then an exception is thrown.
    */
   @Test
   void givenNoData_whenProcessAndSaveCurrentDaySummary_thenThrowsException() {
      String databaseName = "testDB";
      String tableName = "daily_summary";

      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(List.of());

      GarminProcessingException exception = assertThrows(GarminProcessingException.class, () ->
          garminService.processAndSaveCurrentDaySummary(databaseName, tableName));

      assertEquals("No data found in table: daily_summary", exception.getMessage());
   }

   /**
    * ✅ Test Case: Given valid data, when processAndSaveWeeklySummary is called, then data is saved successfully.
    */
   @Test
   void givenValidData_whenProcessAndSaveWeeklySummary_thenSavesSuccessfully() {
      String databaseName = "testDB";
      String tableName = "weekly_summary";

      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(mockSQLiteDataWeek);

      garminService.processAndSaveWeeklySummary(databaseName, tableName);

      // Capture the saved entity
      ArgumentCaptor<WeeklySummary> captor = ArgumentCaptor.forClass(WeeklySummary.class);
      verify(weeklySummaryRepo).save(captor.capture());

      WeeklySummary savedSummary = captor.getValue();
      assertNotNull(savedSummary);
      assertNotNull(savedSummary.firstDay()); // Ensures the correct date field is properly set

      // Ensure validation was called before saving
      verify(validationService).validate(savedSummary);
   }

   /**
    * ❌ Test Case: Given no data, when processAndSaveWeeklySummary is called, then an exception is thrown.
    */
   @Test
   void givenNoData_whenProcessAndSaveWeeklySummary_thenThrowsException() {
      String databaseName = "testDB";
      String tableName = "weekly_summary";

      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(List.of());

      GarminProcessingException exception = assertThrows(GarminProcessingException.class, () ->
          garminService.processAndSaveWeeklySummary(databaseName, tableName));

      assertEquals("No data found in table: weekly_summary", exception.getMessage());
   }

   /**
    * ✅ Test Case: Given valid data, when processAndSaveMonthlySummary is called, then data is saved successfully.
    */
   @Test
   void givenValidData_whenProcessAndSaveMonthlySummary_thenSavesSuccessfully() {
      String databaseName = "testDB";
      String tableName = "monthly_summary";

      // Use the monthly mock data
      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(mockSQLiteDataMonth);

      garminService.processAndSaveMonthlySummary(databaseName, tableName);

      // Capture the saved entity
      ArgumentCaptor<MonthlySummary> captor = ArgumentCaptor.forClass(MonthlySummary.class);
      verify(monthlySummaryRepo).save(captor.capture());

      MonthlySummary savedSummary = captor.getValue();
      assertNotNull(savedSummary);
      assertNotNull(savedSummary.firstDay()); // Ensures the date field is correctly set

      // Ensure validation was called before saving
      verify(validationService).validate(savedSummary);
   }

   /**
    * ❌ Test Case: Given no data, when processAndSaveMonthlySummary is called, then an exception is thrown.
    */
   @Test
   void givenNoData_whenProcessAndSaveMonthlySummary_thenThrowsException() {
      String databaseName = "testDB";
      String tableName = "monthly_summary";

      // Return an empty list to simulate no data
      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(List.of());

      GarminProcessingException exception = assertThrows(GarminProcessingException.class, () ->
          garminService.processAndSaveMonthlySummary(databaseName, tableName));

      assertEquals("No data found in table: monthly_summary", exception.getMessage());
   }

   /**
    * ✅ Test Case: Given valid data, when processAndSaveYearlySummary is called, then data is saved successfully.
    */
   @Test
   void givenValidData_whenProcessAndSaveYearlySummary_thenSavesSuccessfully() {
      String databaseName = "testDB";
      String tableName = "yearly_summary";

      // Use the yearly mock data
      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(mockSQLiteDataYear);

      garminService.processAndSaveYearlySummary(databaseName, tableName);

      // Capture the saved entity
      ArgumentCaptor<YearlySummary> captor = ArgumentCaptor.forClass(YearlySummary.class);
      verify(yearlySummaryRepo).save(captor.capture());

      YearlySummary savedSummary = captor.getValue();
      assertNotNull(savedSummary);
      assertNotNull(savedSummary.firstDay()); // Ensure the date field is correctly set

      // Ensure validation was called before saving
      verify(validationService).validate(savedSummary);
   }

   /**
    * ❌ Test Case: Given no data, when processAndSaveYearlySummary is called, then an exception is thrown.
    */
   @Test
   void givenNoData_whenProcessAndSaveYearlySummary_thenThrowsException() {
      String databaseName = "testDB";
      String tableName = "yearly_summary";

      // Return an empty list to simulate no data
      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(List.of());

      GarminProcessingException exception = assertThrows(GarminProcessingException.class, () ->
          garminService.processAndSaveYearlySummary(databaseName, tableName));

      assertEquals("No data found in table: yearly_summary", exception.getMessage());
   }

   /**
    * ✅ Test Case: Given valid data and a valid reference date, when processAndSaveRecentDailySummaries is called,
    * then data is saved successfully.
    */
   @Test
   void givenValidData_whenProcessAndSaveRecentDailySummaries_thenSavesSuccessfully() throws IOException {
      // Configure ObjectMapper to handle Java 8 date/time types
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

      // Load the expected RecentDailySummaries from the mock file using the configured mapper.
      RecentDailySummaries expectedRecent = mapper.readValue(
          Paths.get("src/test/resources/recent_daily_summaries_mock.json").toFile(),
          RecentDailySummaries.class
                                                            );

      // Use the expected latestDay from the loaded object as the reference date.
      LocalDate refDate = expectedRecent.latestDay();

      // Create a dummy CurrentDaySummary using the reference date.
      CurrentDaySummary dummyDaily = new CurrentDaySummary(
          null,
          refDate,
          DataParsingUtils.mapToBaseSummary(mockSQLiteDataDay.getFirst())
      );
      // Create a list of 7 identical dummy objects (immutable list is fine since service makes a mutable copy).
      List<CurrentDaySummary> dummyList = List.of(
          dummyDaily, dummyDaily, dummyDaily, dummyDaily, dummyDaily, dummyDaily, dummyDaily
                                                 );

      // Use the new repository method with the reference date.
      when(currentDaySummaryRepo.findTop7ByDayLessThanEqualOrderByDayDesc(refDate)).thenReturn(dummyList);

      // Call the service method with the reference date.
      garminService.processAndSaveRecentDailySummaries(String.valueOf(refDate));

      // Capture the RecentDailySummaries object saved to MongoDB.
      ArgumentCaptor<RecentDailySummaries> captor = ArgumentCaptor.forClass(RecentDailySummaries.class);
      verify(recentDailySummariesRepo).save(captor.capture());
      RecentDailySummaries savedRecent = captor.getValue();

      assertNotNull(savedRecent);
      // Verify that the latestDay field matches the expected value from the mock file.
      assertEquals(expectedRecent.latestDay(), savedRecent.latestDay());
      // Optionally, add further assertions to check other fields.

      // Ensure that validation was called.
      verify(validationService).validate(savedRecent);
   }

   /**
    * ❌ Test Case: Given no daily summary data, when processAndSaveRecentDailySummaries is called,
    * then the method logs a warning and skips saving.
    */
   @Test
   void givenNoData_whenProcessAndSaveRecentDailySummaries_thenLogsWarningAndSkipsSaving() {
      LocalDate refDate = LocalDate.of(2025, 1, 15);
      // Simulate no daily summaries found.
      when(currentDaySummaryRepo.findTop7ByDayLessThanEqualOrderByDayDesc(refDate)).thenReturn(List.of());

      garminService.processAndSaveRecentDailySummaries(String.valueOf(refDate));

      // Verify that recentDailySummariesRepo.save was never called.
      verify(recentDailySummariesRepo, never()).save(any());
   }

}
