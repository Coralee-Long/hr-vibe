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
import static org.mockito.ArgumentMatchers.*;
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
   // For monthly summaries, dummyMonth is used solely for stubbing duplicate scenarios.
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
      dummyMonth = new MonthlySummary("monthId", LocalDate.of(2024, 11, 1), null);
      dummyYear = new YearlySummary("yearId", sampleDate.withDayOfMonth(1), null);
      // For recent daily summaries, we supply the required arguments.
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

   // --- CURRENT DAY SUMMARY TESTS ---

   /**
    * 1️⃣ Test Case: Given valid data, when processAndSaveSummary (for current day) is called,
    * then data is saved successfully.
    *
    * Updated to verify that each raw record leads to an insert call.
    */
   @Test
   void givenValidData_whenProcessAndSaveSummary_thenSavesSuccessfully() {
      String databaseName = "testDB";
      String tableName = "daily_summary";

      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(mockSQLiteDataDay);
      // Simulate no duplicate exists by returning empty for any lookup.
      when(currentDaySummaryRepo.findByDay(any())).thenReturn(Optional.empty());

      garminProcessingService.processAndSaveCurrentDaySummary(databaseName, tableName);

      // Verify that insert() is called for each processed record.
      verify(currentDaySummaryRepo, atLeastOnce()).insert(any(CurrentDaySummary.class));
      // Verify that validation is called at least once.
      verify(validationService, atLeastOnce()).validate(any(CurrentDaySummary.class));
   }

   /**
    * 1️⃣ Test Case: Given no data, when processAndSaveSummary (for current day) is called, then an exception is thrown.
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
    * 1️⃣ Test Case: Given invalid data, when processAndSaveSummary (for current day) is called,
    * then a validation exception is thrown.
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
    * 1️⃣ Test Case: Given a database exception, when processAndSaveSummary (for current day) is called,
    * then GarminProcessingException is thrown.
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
    * 2️⃣ Test Case: Given valid data, when processAndSaveCurrentDaySummary is called,
    * then data is saved successfully.
    */
   @Test
   void givenValidData_whenProcessAndSaveCurrentDaySummary_thenSavesSuccessfully() {
      String databaseName = "testDB";
      String tableName = "daily_summary";

      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(mockSQLiteDataDay);
      when(currentDaySummaryRepo.findByDay(any())).thenReturn(Optional.empty());

      garminProcessingService.processAndSaveCurrentDaySummary(databaseName, tableName);

      // Capture individual insert calls.
      ArgumentCaptor<CurrentDaySummary> captor = ArgumentCaptor.forClass(CurrentDaySummary.class);
      verify(currentDaySummaryRepo, atLeastOnce()).insert(captor.capture());
      List<CurrentDaySummary> insertedSummaries = captor.getAllValues();
      assertNotNull(insertedSummaries, "Inserted summaries should not be null");
      assertFalse(insertedSummaries.isEmpty(), "Inserted summaries should not be empty");
      verify(validationService, atLeast(insertedSummaries.size())).validate(any(CurrentDaySummary.class));
   }

   /**
    * 2️⃣ Test Case: Given no data, when processAndSaveCurrentDaySummary is called,
    * then an exception is thrown.
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

   // --- WEEKLY SUMMARY TESTS ---

   /**
    * 3️⃣ Test Case: Given valid data, when processAndSaveWeeklySummary is called,
    * then data is saved successfully.
    */
   @Test
   void givenValidData_whenProcessAndSaveWeeklySummary_thenSavesSuccessfully() {
      String databaseName = "testDB";
      String tableName = "weekly_summary";

      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(mockSQLiteDataWeek);
      when(weeklySummaryRepo.findByFirstDay(any())).thenReturn(Optional.empty());

      garminProcessingService.processAndSaveWeeklySummary(databaseName, tableName);

      ArgumentCaptor<WeeklySummary> captor = ArgumentCaptor.forClass(WeeklySummary.class);
      verify(weeklySummaryRepo, atLeastOnce()).insert(captor.capture());
      List<WeeklySummary> insertedSummaries = captor.getAllValues();
      assertNotNull(insertedSummaries, "Inserted weekly summaries should not be null");
      assertFalse(insertedSummaries.isEmpty(), "Inserted weekly summaries should not be empty");
      insertedSummaries.forEach(summary -> {
         assertNotNull(summary.firstDay(), "Weekly summary must have a firstDay set");
         verify(validationService).validate(summary);
      });
   }

   /**
    * 3️⃣ Test Case: Given no data, when processAndSaveWeeklySummary is called,
    * then an exception is thrown.
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

   // --- MONTHLY SUMMARY TESTS ---

   /**
    * 4️⃣ Test Case: Given valid data, when processAndSaveMonthlySummary is called,
    * then data is saved successfully (inserts new records).
    */
   @Test
   void givenValidData_whenProcessAndSaveMonthlySummary_thenSavesSuccessfully() {
      String databaseName = "testDB";
      String tableName = "monthly_summary";

      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(mockSQLiteDataMonth);
      // Simulate no duplicate exists.
      when(monthlySummaryRepo.findByFirstDay(any())).thenReturn(Optional.empty());

      garminProcessingService.processAndSaveMonthlySummary(databaseName, tableName);

      verify(monthlySummaryRepo, atLeastOnce()).insert(any(MonthlySummary.class));
      verify(validationService, atLeastOnce()).validate(any(MonthlySummary.class));
   }

   /**
    * 4️⃣ Test Case: Given duplicate monthly data, when processAndSaveMonthlySummary is called,
    * then the existing record is updated.
    */
   @Test
   void givenDuplicateMonthlyData_whenProcessAndSaveMonthlySummary_thenUpdatesExisting() {
      String databaseName = "testDB";
      String tableName = "monthly_summary";

      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(mockSQLiteDataMonth);
      // Simulate duplicate: for any lookup, return an existing record.
      when(monthlySummaryRepo.findByFirstDay(any())).thenReturn(Optional.of(dummyMonth));

      garminProcessingService.processAndSaveMonthlySummary(databaseName, tableName);

      verify(monthlySummaryRepo, atLeastOnce()).save(any(MonthlySummary.class));
      verify(monthlySummaryRepo, never()).insert(any(MonthlySummary.class));
   }

   /**
    * 4️⃣ Test Case: Given no data, when processAndSaveMonthlySummary is called,
    * then an exception is thrown.
    */
   @Test
   void givenNoData_whenProcessAndSaveMonthlySummary_thenThrowsException() {
      String databaseName = "testDB";
      String tableName = "monthly_summary";

      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(List.of());

      GarminProcessingException exception = assertThrows(GarminProcessingException.class, () ->
          garminProcessingService.processAndSaveMonthlySummary(databaseName, tableName));

      assertEquals("No data found in table: monthly_summary", exception.getMessage());
   }

   // --- YEARLY SUMMARY TESTS ---

   /**
    * 5️⃣ Test Case: Given valid data, when processAndSaveYearlySummary is called,
    * then data is saved successfully (inserts new records).
    */
   @Test
   void givenValidData_whenProcessAndSaveYearlySummary_thenSavesSuccessfully() {
      String databaseName = "testDB";
      String tableName = "yearly_summary";

      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(mockSQLiteDataYear);
      when(yearlySummaryRepo.findByFirstDay(any())).thenReturn(Optional.empty());

      garminProcessingService.processAndSaveYearlySummary(databaseName, tableName);

      ArgumentCaptor<YearlySummary> captor = ArgumentCaptor.forClass(YearlySummary.class);
      verify(yearlySummaryRepo, atLeastOnce()).insert(captor.capture());
      List<YearlySummary> insertedSummaries = captor.getAllValues();
      assertNotNull(insertedSummaries, "Inserted yearly summaries should not be null");
      assertFalse(insertedSummaries.isEmpty(), "Inserted yearly summaries should not be empty");
      insertedSummaries.forEach(summary -> {
         assertNotNull(summary.firstDay(), "Yearly summary must have a firstDay set");
         verify(validationService).validate(summary);
      });
   }

   /**
    * 5️⃣ Test Case: Given no data, when processAndSaveYearlySummary is called,
    * then an exception is thrown.
    */
   @Test
   void givenNoData_whenProcessAndSaveYearlySummary_thenThrowsException() {
      String databaseName = "testDB";
      String tableName = "yearly_summary";

      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(List.of());

      GarminProcessingException exception = assertThrows(GarminProcessingException.class, () ->
          garminProcessingService.processAndSaveYearlySummary(databaseName, tableName));

      assertEquals("No data found in table: yearly_summary", exception.getMessage());
   }

   // --- RECENT DAILY SUMMARIES TESTS ---

   /**
    * 6️⃣ Test Case: Given valid data and a valid reference date, when processAndSaveRecentDailySummaries is called,
    * then data is saved successfully (inserts new record).
    *
    * Note: The models stored in MongoDB use a LocalDate for the date fields.
    * When converting to DTOs for output, these dates are transformed to ISO string format.
    */
   @Test
   void givenValidData_whenProcessAndSaveRecentDailySummaries_thenSavesSuccessfully() throws IOException {
      // Configure ObjectMapper for Java 8 date/time types.
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

      RecentDailySummaries expectedRecent = mapper.readValue(
          Paths.get("src/test/resources/mocks/models/recent_daily_summaries_mock.json").toFile(),
          RecentDailySummaries.class
                                                            );

      LocalDate refDate = expectedRecent.latestDay();
      CurrentDaySummary dummyDaily = new CurrentDaySummary(
          null,
          refDate,
          DataParsingUtils.mapToBaseSummary(mockSQLiteDataDay.get(0))
      );
      List<CurrentDaySummary> dummyList = List.of(dummyDaily, dummyDaily, dummyDaily, dummyDaily, dummyDaily, dummyDaily, dummyDaily);

      when(currentDaySummaryRepo.findTop7ByDayLessThanEqualOrderByDayDesc(refDate)).thenReturn(dummyList);
      // Simulate no duplicate exists for recent daily summaries.
      when(recentDailySummariesRepo.findByLatestDay(any())).thenReturn(Optional.empty());

      garminProcessingService.processAndSaveRecentDailySummaries(refDate.toString());

      ArgumentCaptor<RecentDailySummaries> captor = ArgumentCaptor.forClass(RecentDailySummaries.class);
      verify(recentDailySummariesRepo, atLeastOnce()).insert(captor.capture());
      RecentDailySummaries savedRecent = captor.getValue();
      assertNotNull(savedRecent, "The saved RecentDailySummaries object should not be null");
      assertEquals(refDate.toString(), savedRecent.latestDay().toString());
      verify(validationService).validate(savedRecent);
   }

   /**
    * 6️⃣ Test Case: Given no daily summary data, when processAndSaveRecentDailySummaries is called,
    * then the method logs a warning and skips saving.
    */
   @Test
   void givenNoData_whenProcessAndSaveRecentDailySummaries_thenLogsWarningAndSkipsSaving() {
      LocalDate refDate = LocalDate.of(2025, 1, 15);
      when(currentDaySummaryRepo.findTop7ByDayLessThanEqualOrderByDayDesc(refDate)).thenReturn(List.of());

      garminProcessingService.processAndSaveRecentDailySummaries(refDate.toString());

      verify(recentDailySummariesRepo, never()).insert(any(RecentDailySummaries.class));
      verify(recentDailySummariesRepo, never()).save(any());
   }
}
