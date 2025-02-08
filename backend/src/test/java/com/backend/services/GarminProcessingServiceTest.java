package com.backend.services;

import com.backend.exceptions.GarminProcessingException;
import com.backend.models.CurrentDaySummary;
import com.backend.models.MonthlySummary;
import com.backend.models.RecentDailySummaries;
import com.backend.models.WeeklySummary;
import com.backend.models.YearlySummary;
import com.backend.repos.MongoDB.CurrentDaySummaryRepo;
import com.backend.repos.MongoDB.MonthlySummaryRepo;
import com.backend.repos.MongoDB.WeeklySummaryRepo;
import com.backend.repos.MongoDB.YearlySummaryRepo;
import com.backend.repos.MongoDB.RecentDailySummariesRepo;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * 📌 GarminProcessingServiceTest - Unit tests for GarminProcessingService.
 *
 * 1️⃣ Tests for `processAndSaveSummary` (Generic Method)
 *    - ✅ `givenValidData_whenProcessAndSaveSummary_thenSavesSuccessfully`
 *    - ❌ `givenNoData_whenProcessAndSaveSummary_thenThrowsException`
 *    - ❌ `givenInvalidData_whenProcessAndSaveSummary_thenThrowsValidationException`
 *    - ❌ `givenDatabaseException_whenProcessAndSaveSummary_thenThrowsProcessingException`
 *
 * 2️⃣ Tests for `processAndSaveCurrentDaySummary`
 *    - ✅ `givenValidData_whenProcessAndSaveCurrentDaySummary_thenSavesSuccessfully`
 *    - ❌ `givenNoData_whenProcessAndSaveCurrentDaySummary_thenThrowsException`
 *
 * 3️⃣ Tests for `processAndSaveWeeklySummary`
 *    - ✅ `givenValidData_whenProcessAndSaveWeeklySummary_thenSavesSuccessfully`
 *    - ❌ `givenNoData_whenProcessAndSaveWeeklySummary_thenThrowsException`
 *
 * 4️⃣ Tests for `processAndSaveMonthlySummary`
 *    - ✅ `givenValidData_whenProcessAndSaveMonthlySummary_thenSavesSuccessfully`
 *    - ✅ `givenDuplicateMonthlyData_whenProcessAndSaveMonthlySummary_thenUpdatesExisting`
 *    - ❌ `givenNoData_whenProcessAndSaveMonthlySummary_thenThrowsException`
 *
 * 5️⃣ Tests for `processAndSaveYearlySummary`
 *    - ✅ `givenValidData_whenProcessAndSaveYearlySummary_thenSavesSuccessfully`
 *    - ❌ `givenNoData_whenProcessAndSaveYearlySummary_thenThrowsException`
 *
 * 6️⃣ Tests for `processAndSaveRecentDailySummaries`
 *    - ✅ `givenValidData_whenProcessAndSaveRecentDailySummaries_thenSavesSuccessfully`
 *    - ❌ `givenNoData_whenProcessAndSaveRecentDailySummaries_thenLogsWarningAndSkipsSaving`
 */
@ExtendWith(MockitoExtension.class)
class GarminProcessingServiceTest {

   @Mock
   private GarminSQLiteRepo garminSQLiteRepo;

   @Mock
   private CurrentDaySummaryRepo currentDaySummaryRepo;

   @Mock
   private RecentDailySummariesRepo recentDailySummariesRepo;

   @Mock
   private WeeklySummaryRepo weeklySummaryRepo;

   @Mock
   private MonthlySummaryRepo monthlySummaryRepo;

   @Mock
   private YearlySummaryRepo yearlySummaryRepo;

   @Mock
   private ValidationService validationService;

   @InjectMocks
   private GarminProcessingService garminProcessingService;

   // Fields to hold the parsed mock data
   private List<Map<String, Object>> mockSQLiteDataDay;
   private List<Map<String, Object>> mockSQLiteDataWeek;
   private List<Map<String, Object>> mockSQLiteDataMonth;
   private List<Map<String, Object>> mockSQLiteDataYear;

   // Dummy models for testing processing methods.
   private CurrentDaySummary dummyDay;
   private WeeklySummary dummyWeek;
   // For monthly summaries, the service computes firstDay based on the SQLite data.
   // In our duplicate test, we want any processed monthly summary to be considered duplicate.
   // Thus, dummyMonth is used only for stubbing.
   private MonthlySummary dummyMonth;
   private YearlySummary dummyYear;
   private RecentDailySummaries dummyRecent;

   // A common sample date for testing.
   private LocalDate sampleDate;

   @BeforeEach
   void setUp() throws IOException {
      // Load daily summary mock data
      String jsonDayData = new String(Files.readAllBytes(Paths.get("src/test/resources/mocks/models/sqlite_mock_days_summary.json")));
      mockSQLiteDataDay = DataParsingUtils.JsonUtils.parseJsonToList(jsonDayData);

      // Load weekly summary mock data
      String jsonWeekData = new String(Files.readAllBytes(Paths.get("src/test/resources/mocks/models/sqlite_mock_weeks_summary.json")));
      mockSQLiteDataWeek = DataParsingUtils.JsonUtils.parseJsonToList(jsonWeekData);

      // Load monthly summary mock data
      String jsonMonthData = new String(Files.readAllBytes(Paths.get("src/test/resources/mocks/models/sqlite_mock_months_summary.json")));
      mockSQLiteDataMonth = DataParsingUtils.JsonUtils.parseJsonToList(jsonMonthData);

      // Load yearly summary mock data
      String jsonYearData = new String(Files.readAllBytes(Paths.get("src/test/resources/mocks/models/sqlite_mock_years_summary.json")));
      mockSQLiteDataYear = DataParsingUtils.JsonUtils.parseJsonToList(jsonYearData);

      sampleDate = LocalDate.of(2025, 2, 8);
      // Create dummy models. In a complete test, these would have valid BaseSummary values.
      dummyDay = new CurrentDaySummary("dayId", sampleDate, null);
      dummyWeek = new WeeklySummary("weekId", sampleDate, null);
      // For duplicate monthly scenario, dummyMonth is used solely for stubbing.
      // Its firstDay value here is less important since we stub using any(LocalDate.class).
      dummyMonth = new MonthlySummary("monthId", LocalDate.of(2024, 11, 1), null);
      dummyYear = new YearlySummary("yearId", sampleDate.withDayOfMonth(1), null);
      // For recent daily summaries, we must supply exactly 49 arguments:
      // id, latestDay, followed by 47 empty lists for summary fields.
      dummyRecent = new RecentDailySummaries(
          "recentId",
          sampleDate,
          Collections.emptyList(), // hrMin
          Collections.emptyList(), // hrMax
          Collections.emptyList(), // hrAvg
          Collections.emptyList(), // rhrMin
          Collections.emptyList(), // rhrMax
          Collections.emptyList(), // rhrAvg
          Collections.emptyList(), // inactiveHrMin
          Collections.emptyList(), // inactiveHrMax
          Collections.emptyList(), // inactiveHrAvg
          Collections.emptyList(), // caloriesAvg
          Collections.emptyList(), // caloriesGoal
          Collections.emptyList(), // caloriesBmrAvg
          Collections.emptyList(), // caloriesConsumedAvg
          Collections.emptyList(), // caloriesActiveAvg
          Collections.emptyList(), // activitiesCalories
          Collections.emptyList(), // weightMin
          Collections.emptyList(), // weightMax
          Collections.emptyList(), // weightAvg
          Collections.emptyList(), // hydrationGoal
          Collections.emptyList(), // hydrationIntake
          Collections.emptyList(), // hydrationAvg
          Collections.emptyList(), // sweatLoss
          Collections.emptyList(), // sweatLossAvg
          Collections.emptyList(), // bbMin
          Collections.emptyList(), // bbMax
          Collections.emptyList(), // stressAvg
          Collections.emptyList(), // rrMin
          Collections.emptyList(), // rrMax
          Collections.emptyList(), // rrWakingAvg
          Collections.emptyList(), // spo2Min
          Collections.emptyList(), // spo2Avg
          Collections.emptyList(), // sleepMin
          Collections.emptyList(), // sleepMax
          Collections.emptyList(), // sleepAvg
          Collections.emptyList(), // remSleepMin
          Collections.emptyList(), // remSleepMax
          Collections.emptyList(), // remSleepAvg
          Collections.emptyList(), // stepsGoal
          Collections.emptyList(), // steps
          Collections.emptyList(), // floorsGoal
          Collections.emptyList(), // floors
          Collections.emptyList(), // activities
          Collections.emptyList(), // activitiesDistance
          Collections.emptyList(), // intensityTimeGoal
          Collections.emptyList(), // intensityTime
          Collections.emptyList(), // moderateActivityTime
          Collections.emptyList()  // vigorousActivityTime
      );
   }

   /**
    * 1️⃣ Test Case: Given valid data, when processAndSaveSummary is called, then data is saved successfully.
    *
    * Note: The service uses the repository method saveAll() (instead of save()) to persist a list of summaries.
    */
   @Test
   void givenValidData_whenProcessAndSaveSummary_thenSavesSuccessfully() {
      String databaseName = "testDB";
      String tableName = "daily_summary";

      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(mockSQLiteDataDay);

      garminProcessingService.processAndSaveCurrentDaySummary(databaseName, tableName);

      // Capture the list of saved entities using saveAll().
      ArgumentCaptor<List<CurrentDaySummary>> captor = ArgumentCaptor.forClass(List.class);
      verify(currentDaySummaryRepo).saveAll(captor.capture());

      List<CurrentDaySummary> savedSummaries = captor.getValue();
      assertNotNull(savedSummaries, "The saved summaries list should not be null");
      assertFalse(savedSummaries.isEmpty(), "The saved summaries list should not be empty");

      // Ensure that validation was called on each saved summary before saving.
      savedSummaries.forEach(summary -> verify(validationService).validate(summary));
   }

   /**
    * 1️⃣ Test Case: Given no data, when processAndSaveSummary is called, then an exception is thrown.
    */
   @Test
   void givenNoData_whenProcessAndSaveSummary_thenThrowsException() {
      String databaseName = "testDB";
      String tableName = "daily_summary";

      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(List.of());

      GarminProcessingException exception = assertThrows(GarminProcessingException.class, () ->
          garminProcessingService.processAndSaveCurrentDaySummary(databaseName, tableName));

      assertEquals("No data found in table: daily_summary", exception.getMessage());
   }

   /**
    * 1️⃣ Test Case: Given invalid data, when processAndSaveSummary is called, then a validation exception is thrown.
    */
   @Test
   void givenInvalidData_whenProcessAndSaveSummary_thenThrowsValidationException() {
      String databaseName = "testDB";
      String tableName = "daily_summary";

      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(mockSQLiteDataDay);
      doThrow(new RuntimeException("Validation failed"))
          .when(validationService).validate(any(CurrentDaySummary.class));

      assertThrows(RuntimeException.class, () ->
          garminProcessingService.processAndSaveCurrentDaySummary(databaseName, tableName));
   }

   /**
    * 1️⃣ Test Case: Given a database exception, when processAndSaveSummary is called, then GarminProcessingException is thrown.
    */
   @Test
   void givenDatabaseException_whenProcessAndSaveSummary_thenThrowsProcessingException() {
      String databaseName = "testDB";
      String tableName = "daily_summary";

      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenThrow(new RuntimeException("Database error"));

      assertThrows(GarminProcessingException.class, () ->
          garminProcessingService.processAndSaveCurrentDaySummary(databaseName, tableName));
   }

   /**
    * 2️⃣ Test Case: Given valid data, when processAndSaveCurrentDaySummary is called, then data is saved successfully.
    */
   @Test
   void givenValidData_whenProcessAndSaveCurrentDaySummary_thenSavesSuccessfully() {
      String databaseName = "testDB";
      String tableName = "daily_summary";

      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(mockSQLiteDataDay);

      garminProcessingService.processAndSaveCurrentDaySummary(databaseName, tableName);

      // Capture the saved list using saveAll().
      ArgumentCaptor<List<CurrentDaySummary>> captor = ArgumentCaptor.forClass(List.class);
      verify(currentDaySummaryRepo).saveAll(captor.capture());

      List<CurrentDaySummary> savedSummaries = captor.getValue();
      assertNotNull(savedSummaries, "The saved summaries list should not be null");
      assertFalse(savedSummaries.isEmpty(), "The saved summaries list should not be empty");

      // Ensure validation was called on each saved summary.
      savedSummaries.forEach(summary -> verify(validationService).validate(summary));
   }

   /**
    * 2️⃣ Test Case: Given no data, when processAndSaveCurrentDaySummary is called, then an exception is thrown.
    */
   @Test
   void givenNoData_whenProcessAndSaveCurrentDaySummary_thenThrowsException() {
      String databaseName = "testDB";
      String tableName = "daily_summary";

      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(List.of());

      GarminProcessingException exception = assertThrows(GarminProcessingException.class, () ->
          garminProcessingService.processAndSaveCurrentDaySummary(databaseName, tableName));

      assertEquals("No data found in table: daily_summary", exception.getMessage());
   }

   /**
    * 3️⃣ Test Case: Given valid data, when processAndSaveWeeklySummary is called, then data is saved successfully.
    */
   @Test
   void givenValidData_whenProcessAndSaveWeeklySummary_thenSavesSuccessfully() {
      String databaseName = "testDB";
      String tableName = "weekly_summary";

      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(mockSQLiteDataWeek);

      garminProcessingService.processAndSaveWeeklySummary(databaseName, tableName);

      // Capture the saved list using saveAll().
      ArgumentCaptor<List<WeeklySummary>> captor = ArgumentCaptor.forClass(List.class);
      verify(weeklySummaryRepo).saveAll(captor.capture());

      List<WeeklySummary> savedSummaries = captor.getValue();
      assertNotNull(savedSummaries, "The saved weekly summaries list should not be null");
      assertFalse(savedSummaries.isEmpty(), "The saved weekly summaries list should not be empty");
      savedSummaries.forEach(summary -> {
         assertNotNull(summary.firstDay(), "Weekly summary must have a firstDay set");
         verify(validationService).validate(summary);
      });
   }

   /**
    * 3️⃣ Test Case: Given no data, when processAndSaveWeeklySummary is called, then an exception is thrown.
    */
   @Test
   void givenNoData_whenProcessAndSaveWeeklySummary_thenThrowsException() {
      String databaseName = "testDB";
      String tableName = "weekly_summary";

      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(List.of());

      GarminProcessingException exception = assertThrows(GarminProcessingException.class, () ->
          garminProcessingService.processAndSaveWeeklySummary(databaseName, tableName));

      assertEquals("No data found in table: weekly_summary", exception.getMessage());
   }

   /**
    * 4️⃣ Test Case: Given valid data, when processAndSaveMonthlySummary is called, then data is saved successfully.
    * This scenario simulates no duplicate existing, so the record is inserted.
    */
   @Test
   void givenValidData_whenProcessAndSaveMonthlySummary_thenSavesSuccessfully() {
      String databaseName = "testDB";
      String tableName = "monthly_summary";

      // Use the monthly mock data.
      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(mockSQLiteDataMonth);
      // Simulate no duplicate exists.
      when(monthlySummaryRepo.findByFirstDay(dummyMonth.firstDay())).thenReturn(Optional.empty());

      garminProcessingService.processAndSaveMonthlySummary(databaseName, tableName);

      // Expect that insert() is called (since no duplicate exists)
      verify(monthlySummaryRepo, atLeastOnce()).insert(any(MonthlySummary.class));
      // Also, verify that validation was called on each MonthlySummary.
      verify(validationService, atLeastOnce()).validate(any(MonthlySummary.class));
   }

   /**
    * 4️⃣ Test Case: Given duplicate monthly data, when processAndSaveMonthlySummary is called, then the existing record is updated.
    */
   @Test
   void givenDuplicateMonthlyData_whenProcessAndSaveMonthlySummary_thenUpdatesExisting() {
      String databaseName = "testDB";
      String tableName = "monthly_summary";

      // Use the monthly mock data.
      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(mockSQLiteDataMonth);
      // Simulate that a record with the same firstDay already exists for any given date.
      when(monthlySummaryRepo.findByFirstDay(any(LocalDate.class))).thenReturn(Optional.of(dummyMonth));

      garminProcessingService.processAndSaveMonthlySummary(databaseName, tableName);

      // Expect that save() is called (for update) rather than insert()
      verify(monthlySummaryRepo, atLeastOnce()).save(any(MonthlySummary.class));
      verify(monthlySummaryRepo, never()).insert(any(MonthlySummary.class));
   }

   /**
    * 4️⃣ Test Case: Given no data, when processAndSaveMonthlySummary is called, then an exception is thrown.
    */
   @Test
   void givenNoData_whenProcessAndSaveMonthlySummary_thenThrowsException() {
      String databaseName = "testDB";
      String tableName = "monthly_summary";

      // Return an empty list to simulate no data.
      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(List.of());

      GarminProcessingException exception = assertThrows(GarminProcessingException.class, () ->
          garminProcessingService.processAndSaveMonthlySummary(databaseName, tableName));

      assertEquals("No data found in table: monthly_summary", exception.getMessage());
   }

   /**
    * 5️⃣ Test Case: Given valid data, when processAndSaveYearlySummary is called, then data is saved successfully.
    */
   @Test
   void givenValidData_whenProcessAndSaveYearlySummary_thenSavesSuccessfully() {
      String databaseName = "testDB";
      String tableName = "yearly_summary";

      // Use the yearly mock data.
      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(mockSQLiteDataYear);

      garminProcessingService.processAndSaveYearlySummary(databaseName, tableName);

      // Capture the saved list using saveAll().
      ArgumentCaptor<List<YearlySummary>> captor = ArgumentCaptor.forClass(List.class);
      verify(yearlySummaryRepo).saveAll(captor.capture());

      List<YearlySummary> savedSummaries = captor.getValue();
      assertNotNull(savedSummaries, "The saved yearly summaries list should not be null");
      assertFalse(savedSummaries.isEmpty(), "The saved yearly summaries list should not be empty");
      savedSummaries.forEach(summary -> {
         assertNotNull(summary.firstDay(), "Yearly summary must have a firstDay set");
         verify(validationService).validate(summary);
      });
   }

   /**
    * 5️⃣ Test Case: Given no data, when processAndSaveYearlySummary is called, then an exception is thrown.
    */
   @Test
   void givenNoData_whenProcessAndSaveYearlySummary_thenThrowsException() {
      String databaseName = "testDB";
      String tableName = "yearly_summary";

      // Return an empty list to simulate no data.
      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(List.of());

      GarminProcessingException exception = assertThrows(GarminProcessingException.class, () ->
          garminProcessingService.processAndSaveYearlySummary(databaseName, tableName));

      assertEquals("No data found in table: yearly_summary", exception.getMessage());
   }

   /**
    * 6️⃣ Test Case: Given valid data and a valid reference date, when processAndSaveRecentDailySummaries is called,
    * then data is saved successfully.
    *
    * Note: The models stored in MongoDB use a LocalDate for the date fields.
    * When converting to DTOs for output, these dates are transformed to ISO string format.
    */
   @Test
   void givenValidData_whenProcessAndSaveRecentDailySummaries_thenSavesSuccessfully() throws IOException {
      // Configure ObjectMapper to handle Java 8 date/time types.
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

      // Load the expected RecentDailySummaries from the mock file using the configured mapper.
      RecentDailySummaries expectedRecent = mapper.readValue(
          Paths.get("src/test/resources/mocks/models/recent_daily_summaries_mock.json").toFile(),
          RecentDailySummaries.class
                                                            );

      // Use the expected latestDay (as a LocalDate) from the loaded object as the reference date.
      LocalDate refDate = expectedRecent.latestDay();

      // Create a dummy CurrentDaySummary using the reference date.
      CurrentDaySummary dummyDaily = new CurrentDaySummary(
          null,
          refDate,
          DataParsingUtils.mapToBaseSummary(mockSQLiteDataDay.get(0))
      );
      // Create a list of 7 identical dummy objects.
      List<CurrentDaySummary> dummyList = List.of(
          dummyDaily, dummyDaily, dummyDaily, dummyDaily, dummyDaily, dummyDaily, dummyDaily
                                                 );

      // Simulate repository call with the reference date.
      when(currentDaySummaryRepo.findTop7ByDayLessThanEqualOrderByDayDesc(refDate)).thenReturn(dummyList);

      // Call the service method with the reference date as a string.
      garminProcessingService.processAndSaveRecentDailySummaries(refDate.toString());

      // Capture the RecentDailySummaries object saved to MongoDB.
      ArgumentCaptor<RecentDailySummaries> captor = ArgumentCaptor.forClass(RecentDailySummaries.class);
      verify(recentDailySummariesRepo).save(captor.capture());
      RecentDailySummaries savedRecent = captor.getValue();

      assertNotNull(savedRecent, "The saved RecentDailySummaries object should not be null");
      // Verify that the latestDay field matches the reference date.
      assertEquals(refDate.toString(), savedRecent.latestDay().toString());
      // Ensure that validation was called.
      verify(validationService).validate(savedRecent);
   }

   /**
    * 6️⃣ Test Case: Given no daily summary data, when processAndSaveRecentDailySummaries is called,
    * then the method logs a warning and skips saving.
    */
   @Test
   void givenNoData_whenProcessAndSaveRecentDailySummaries_thenLogsWarningAndSkipsSaving() {
      LocalDate refDate = LocalDate.of(2025, 1, 15);
      // Simulate no daily summaries found.
      when(currentDaySummaryRepo.findTop7ByDayLessThanEqualOrderByDayDesc(refDate)).thenReturn(List.of());

      garminProcessingService.processAndSaveRecentDailySummaries(refDate.toString());

      // Verify that recentDailySummariesRepo.save was never called.
      verify(recentDailySummariesRepo, never()).save(any());
   }
}
