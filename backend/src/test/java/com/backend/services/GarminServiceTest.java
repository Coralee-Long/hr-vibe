package com.backend.services;

import com.backend.exceptions.GarminProcessingException;
import com.backend.models.*;
import com.backend.repos.MongoDB.*;
import com.backend.repos.SQL.GarminSQLiteRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GarminServiceTest {

   @Mock
   private GarminSQLiteRepo garminSQLiteRepo;

   @Mock
   private CurrentDaySummaryRepo currentDaySummaryRepo;

   @Mock
   private WeeklySummaryRepo weeklySummaryRepo;

   @Mock
   private MonthlySummaryRepo monthlySummaryRepo;

   @Mock
   private YearlySummaryRepo yearlySummaryRepo;

   @Mock
   private RecentDailySummariesRepo recentDailySummariesRepo;

   @Mock
   private ValidationService validationService;

   @InjectMocks
   private GarminService garminService;

   private List<Map<String, Object>> mockSQLiteData;
   private List<CurrentDaySummary> last7Days;

   @BeforeEach
   void setUp() {
      reset(garminSQLiteRepo, currentDaySummaryRepo, validationService);

      // Mock SQLite Data
      mockSQLiteData = List.of(
          Map.of("day", "2025-01-30", "metric", 100),
          Map.of("day", "2025-01-31", "metric", 110)
                              );

      // Mock Last 7 Days Data
      last7Days = List.of(
          createMockCurrentDaySummary("2025-01-31", 100, 120, 110),
          createMockCurrentDaySummary("2025-01-30", 90, 110, 100)
                         );
   }

   /**
    * 🔹 Helper method to create mock `CurrentDaySummary` instances.
    */
   private CurrentDaySummary createMockCurrentDaySummary(String date, int hrMin, int hrMax, int hrAvg) {
      return new CurrentDaySummary(
          null, LocalDate.parse(date),
          new BaseSummary(
              hrMin, hrMax, hrAvg,
              60, 65, 62,      // rhrMin, rhrMax, rhrAvg
              50, 55, 53,      // inactiveHrMin, inactiveHrMax, inactiveHrAvg
              2000, 2200, 2100, // caloriesAvg, caloriesGoal, caloriesBmrAvg
              1800, 1900, 1700, // caloriesConsumedAvg, caloriesActiveAvg, activitiesCalories
              60.0, 65.0, 63.0, // weightMin, weightMax, weightAvg
              3000, 3500, 3200, // hydrationGoal, hydrationIntake, hydrationAvg
              50, 55,           // sweatLoss, sweatLossAvg
              90, 100,          // bbMin, bbMax
              30,               // stressAvg
              10, 20, 15,       // rrMin, rrMax, rrWakingAvg
              95, 97,           // spo2Min, spo2Avg
              "06:30:00", "08:00:00", "07:15:00",  // sleepMin, sleepMax, sleepAvg
              "01:30:00", "02:00:00", "01:45:00",  // remSleepMin, remSleepMax, remSleepAvg
              10000, 9000, 10, 9, // stepsGoal, steps, floorsGoal, floors
              5, 12.5,            // activities, activitiesDistance
              "01:00:00", "00:45:00", "00:30:00", "00:15:00" // intensity times
          )
      );
   }

   @Test
   void givenValidData_whenProcessAndSaveCurrentDaySummary_thenSavesSuccessfully() {
      String databaseName = "testDB";
      String tableName = "daily_summary";

      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(mockSQLiteData);

      garminService.processAndSaveCurrentDaySummary(databaseName, tableName);

      // Capture the saved entity
      ArgumentCaptor<CurrentDaySummary> captor = ArgumentCaptor.forClass(CurrentDaySummary.class);
      verify(currentDaySummaryRepo).save(captor.capture());

      CurrentDaySummary savedSummary = captor.getValue();

      assertNotNull(savedSummary);
      assertEquals(LocalDate.parse("2025-01-31"), savedSummary.day());

      // Ensure validation was called before saving
      verify(validationService).validate(savedSummary);

      verify(garminSQLiteRepo).fetchTableData(databaseName, tableName);
      verify(currentDaySummaryRepo).save(any(CurrentDaySummary.class));
   }

   @Test
   void givenNoData_whenProcessAndSaveCurrentDaySummary_thenThrowsException() {
      String databaseName = "testDB";
      String tableName = "daily_summary";

      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(List.of());

      GarminProcessingException exception = assertThrows(
          GarminProcessingException.class,
          () -> garminService.processAndSaveCurrentDaySummary(databaseName, tableName));

      assertEquals("No data found in table: daily_summary", exception.getMessage());

      verify(garminSQLiteRepo).fetchTableData(databaseName, tableName);
      verifyNoInteractions(validationService, currentDaySummaryRepo);
   }

   @Test
   void givenValidData_whenProcessAndSaveRecentDailySummaries_thenSavesSuccessfully() {
      when(currentDaySummaryRepo.findTop7ByOrderByDayDesc()).thenReturn(last7Days);

      garminService.processAndSaveRecentDailySummaries();

      // Capture the saved entity
      ArgumentCaptor<RecentDailySummaries> captor = ArgumentCaptor.forClass(RecentDailySummaries.class);
      verify(recentDailySummariesRepo).save(captor.capture());

      RecentDailySummaries savedSummary = captor.getValue();
      assertNotNull(savedSummary);
      assertEquals(LocalDate.parse("2025-01-31"), savedSummary.latestDay());

      // Ensure validation was called before saving
      verify(validationService).validate(savedSummary);

      verify(currentDaySummaryRepo).findTop7ByOrderByDayDesc();
      verify(recentDailySummariesRepo).save(any(RecentDailySummaries.class));
   }

   @Test
   void givenNoData_whenProcessAndSaveRecentDailySummaries_thenLogsWarningAndSkipsSaving() {
      when(currentDaySummaryRepo.findTop7ByOrderByDayDesc()).thenReturn(List.of());

      garminService.processAndSaveRecentDailySummaries();

      verify(currentDaySummaryRepo).findTop7ByOrderByDayDesc();
      verifyNoInteractions(validationService, recentDailySummariesRepo);
   }
}
