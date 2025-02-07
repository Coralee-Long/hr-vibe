package com.backend.services;

import com.backend.dtos.CurrentDaySummaryDTO;
import com.backend.dtos.MonthlySummaryDTO;
import com.backend.dtos.RecentDailySummariesDTO;
import com.backend.dtos.WeeklySummaryDTO;
import com.backend.dtos.YearlySummaryDTO;
import com.backend.exceptions.GarminProcessingException;
import com.backend.models.CurrentDaySummary;
import com.backend.models.MonthlySummary;
import com.backend.models.WeeklySummary;
import com.backend.models.YearlySummary;
import com.backend.models.RecentDailySummaries;
import com.backend.repos.MongoDB.CurrentDaySummaryRepo;
import com.backend.repos.MongoDB.MonthlySummaryRepo;
import com.backend.repos.MongoDB.WeeklySummaryRepo;
import com.backend.repos.MongoDB.YearlySummaryRepo;
import com.backend.repos.MongoDB.RecentDailySummariesRepo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link com.backend.services.GarminRetrievalService}.
 *
 * <p><b>Table of Contents:</b>
 * <ul>
 *   <li>1️⃣ <b>getAllDaySummaries(int limit)</b>
 *       - ✅ Returns a valid list of day summary DTOs when data is available.
 *   <li>2️⃣ <b>getDaySummary(LocalDate day)</b>
 *       - ✅ Returns a valid day summary DTO for a given date.
 *       - ❌ Throws {@link GarminProcessingException} when no summary is found.
 *   <li>3️⃣ <b>getRecentDailySummaries(LocalDate referenceDate)</b>
 *       - ✅ Returns a valid RecentDailySummariesDTO (loaded from JSON) when data is available.
 *       - ❌ Throws {@link GarminProcessingException} when no recent daily summaries are found.
 *   <li>4️⃣ <b>getWeekSummary(LocalDate referenceDate)</b>
 *       - ✅ Returns a valid weekly summary DTO (loaded from JSON) when data is available.
 *       - ❌ Throws {@link GarminProcessingException} when no weekly summary is found.
 *   <li>5️⃣ <b>getMonthSummaries(Integer month, Integer year)</b>
 *       - ✅ Returns a valid list of monthly summary DTOs (loaded from JSON) when data is available.
 *   <li>6️⃣ <b>getYearSummaries()</b>
 *       - ✅ Returns a valid list of yearly summary DTOs (loaded from JSON) when data is available.
 * </ul>
 * </p>
 */
class GarminRetrievalServiceTest {

   private CurrentDaySummaryRepo currentDaySummaryRepo;
   private WeeklySummaryRepo weeklySummaryRepo;
   private MonthlySummaryRepo monthlySummaryRepo;
   private YearlySummaryRepo yearlySummaryRepo;
   private RecentDailySummariesRepo recentDailySummariesRepo;

   private GarminRetrievalService service;

   // A common dummy BaseSummary model used in these tests.
   // (The values below represent already-cleaned data.)
   private final com.backend.models.BaseSummary dummyBaseSummaryModel =
       new com.backend.models.BaseSummary(
           60, 120, 80,                // hrMin, hrMax, hrAvg
           60, 80, 70,                 // rhrMin, rhrMax, rhrAvg
           55, 75, 65,                 // inactiveHrMin, inactiveHrMax, inactiveHrAvg
           2000, 2500, 1500, 1800, 500, 300, // caloriesAvg, caloriesGoal, caloriesBmrAvg, caloriesConsumedAvg, caloriesActiveAvg, activitiesCalories
           60.0, 65.0, 62.0,           // weightMin, weightMax, weightAvg (doubles remain as doubles)
           3000, 2800, 2900, 100, 110,  // hydrationGoal, hydrationIntake, hydrationAvg, sweatLoss, sweatLossAvg
           20, 80, 30,                 // bbMin, bbMax, stressAvg
           10, 20, 15, 85, 90,          // rrMin, rrMax, rrWakingAvg, spo2Min, spo2Avg
           "08:00:00", "09:00:00", "08:30:00", // sleepMin, sleepMax, sleepAvg
           "01:00:00", "01:15:00", "01:05:00", // remSleepMin, remSleepMax, remSleepAvg
           10000, 9000, 10, 8,         // stepsGoal, steps, floorsGoal, floors
           2, 5.0,                    // activities, activitiesDistance (activitiesDistance remains as double)
           "00:30:00", "00:45:00", "00:20:00", "00:10:00" // intensityTimeGoal, intensityTime, moderateActivityTime, vigorousActivityTime
       );

   private ObjectMapper objectMapper;

   @BeforeEach
   void setUp() {
      MockitoAnnotations.openMocks(this);
      // Initialize mocked repository dependencies.
      currentDaySummaryRepo = Mockito.mock(CurrentDaySummaryRepo.class);
      weeklySummaryRepo = Mockito.mock(WeeklySummaryRepo.class);
      monthlySummaryRepo = Mockito.mock(MonthlySummaryRepo.class);
      yearlySummaryRepo = Mockito.mock(YearlySummaryRepo.class);
      recentDailySummariesRepo = Mockito.mock(RecentDailySummariesRepo.class);
      // Create an instance of the service using the injected mocks.
      service = new GarminRetrievalService(
          currentDaySummaryRepo, weeklySummaryRepo, monthlySummaryRepo, yearlySummaryRepo, recentDailySummariesRepo);

      objectMapper = new ObjectMapper();
      objectMapper.registerModule(new JavaTimeModule());
   }

   // Helper methods to load expected DTO mocks from JSON files (located in resources/mocks/DTOs/)
   private List<CurrentDaySummaryDTO> loadDaysSummaryDTOsFromJson() throws Exception {
      InputStream is = getClass().getResourceAsStream("/mocks/DTOs/mongo_mock_days_summaryDTO.json");
      return objectMapper.readValue(is, new TypeReference<List<CurrentDaySummaryDTO>>() {});
   }

   private List<MonthlySummaryDTO> loadMonthsSummaryDTOsFromJson() throws Exception {
      InputStream is = getClass().getResourceAsStream("/mocks/DTOs/mongo_mock_months_summaryDTO.json");
      return objectMapper.readValue(is, new TypeReference<List<MonthlySummaryDTO>>() {});
   }

   private List<WeeklySummaryDTO> loadWeeksSummaryDTOsFromJson() throws Exception {
      InputStream is = getClass().getResourceAsStream("/mocks/DTOs/mongo_mock_weeks_summaryDTO.json");
      return objectMapper.readValue(is, new TypeReference<List<WeeklySummaryDTO>>() {});
   }

   private List<YearlySummaryDTO> loadYearsSummaryDTOsFromJson() throws Exception {
      InputStream is = getClass().getResourceAsStream("/mocks/DTOs/mongo_mock_years_summaryDTO.json");
      return objectMapper.readValue(is, new TypeReference<List<YearlySummaryDTO>>() {});
   }

   private RecentDailySummariesDTO loadRecentDailySummariesDTOFromJson() throws Exception {
      InputStream is = getClass().getResourceAsStream("/mocks/DTOs/mongo_mock_recent_daily_summariesDTO.json");
      return objectMapper.readValue(is, RecentDailySummariesDTO.class);
   }

   /**
    * 1️⃣ Test getAllDaySummaries(int limit)
    * <ul>
    *   <li>✅ Returns a valid list of day summary DTOs when data is available.
    * </ul>
    */
   @Test
   void testGetAllDaySummaries() throws Exception {
      // GIVEN: A dummy CurrentDaySummary model with a fake mongo id.
      CurrentDaySummary dummyModel = new CurrentDaySummary("mongoId1", LocalDate.of(2023, 5, 1), dummyBaseSummaryModel);
      List<CurrentDaySummary> dummyList = Collections.singletonList(dummyModel);
      when(currentDaySummaryRepo.findAllByOrderByDayDesc(any(Pageable.class))).thenReturn(dummyList);

      // Load expected DTO from JSON mock.
      List<CurrentDaySummaryDTO> expectedDTOs = loadDaysSummaryDTOsFromJson();
      CurrentDaySummaryDTO expectedDTO = expectedDTOs.get(0);

      try (MockedStatic<CurrentDaySummaryDTO> mockedConversion = Mockito.mockStatic(CurrentDaySummaryDTO.class)) {
         mockedConversion.when(() -> CurrentDaySummaryDTO.fromModel(dummyModel)).thenReturn(expectedDTO);

         // WHEN: Calling the service method.
         List<CurrentDaySummaryDTO> result = service.getAllDaySummaries(5);

         // THEN: The result should not be null and contain one element equal to expected.
         assertNotNull(result);
         assertEquals(1, result.size());
         assertEquals(expectedDTO, result.get(0));
      }
   }

   /**
    * 2️⃣ Test getDaySummary(LocalDate day)
    * <ul>
    *   <li>✅ Returns a valid day summary DTO for a given date.
    *   <li>❌ Throws {@link GarminProcessingException} when no summary is found.
    * </ul>
    */
   @Test
   void testGetDaySummaryFound() throws Exception {
      // GIVEN: A valid test date and a dummy CurrentDaySummary model with a fake mongo id.
      LocalDate testDate = LocalDate.of(2023, 5, 1);
      CurrentDaySummary dummyModel = new CurrentDaySummary("mongoId2", testDate, dummyBaseSummaryModel);
      when(currentDaySummaryRepo.findByDay(testDate)).thenReturn(Optional.of(dummyModel));

      // Load expected DTO from JSON.
      List<CurrentDaySummaryDTO> expectedDTOs = loadDaysSummaryDTOsFromJson();
      CurrentDaySummaryDTO expectedDTO = expectedDTOs.get(0);

      try (MockedStatic<CurrentDaySummaryDTO> mockedConversion = Mockito.mockStatic(CurrentDaySummaryDTO.class)) {
         mockedConversion.when(() -> CurrentDaySummaryDTO.fromModel(dummyModel)).thenReturn(expectedDTO);

         // WHEN: Calling the service method.
         CurrentDaySummaryDTO result = service.getDaySummary(testDate);

         // THEN: The result should not be null and equal to expected.
         assertNotNull(result);
         assertEquals(expectedDTO, result);
      }
   }

   @Test
   void testGetDaySummaryNotFound() {
      // GIVEN: A date for which the repository returns no model.
      LocalDate testDate = LocalDate.now();
      when(currentDaySummaryRepo.findByDay(testDate)).thenReturn(Optional.empty());

      // WHEN/THEN: The service call should throw a GarminProcessingException.
      GarminProcessingException ex = assertThrows(GarminProcessingException.class, () -> service.getDaySummary(testDate));
      assertTrue(ex.getMessage().contains("No day summary found for"));
   }

   /**
    * 3️⃣ Test getRecentDailySummaries(LocalDate referenceDate)
    * <ul>
    *   <li>✅ Returns a valid RecentDailySummariesDTO (loaded from JSON) when data is available.
    *   <li>❌ Throws {@link GarminProcessingException} when no recent daily summaries are found.
    * </ul>
    */
   @Test
   void testGetRecentDailySummariesFound() throws Exception {
      // GIVEN: A reference date matching the "latestDay" in the JSON mock file.
      LocalDate refDate = LocalDate.of(2025, 1, 17);
      RecentDailySummaries dummyModel = new RecentDailySummaries(
          "1", // id
          refDate, // latestDay
          Arrays.asList(60, 61, 53, 56, 56, 51, 55),                     // hrMin
          Arrays.asList(134, 152, 112, 151, 146, 153, 152),                // hrMax
          Arrays.asList(79, 84, 71, 75, 75, 72, 73),                       // hrAvg
          Arrays.asList(64, 66, 57, 59, 59, 57, 60),                       // rhrMin
          Arrays.asList(64, 66, 57, 59, 59, 57, 60),                       // rhrMax
          Arrays.asList(64, 66, 57, 59, 59, 57, 60),                       // rhrAvg
          Arrays.asList(61, 63, 59, 58, 58, 55, 57),                       // inactiveHrMin
          Arrays.asList(100, 138, 86, 91, 89, 95, 85),                     // inactiveHrMax
          Arrays.asList(76, 80, 73, 70, 71, 72, 68),                       // inactiveHrAvg
          Arrays.asList(2478, 2753, 2056, 2478, 2481, 2483, 2440),         // caloriesAvg
          Arrays.asList(1240, 1240, 1240, 1240, 1240, 1240, 1240),         // caloriesGoal
          Arrays.asList(2030, 2030, 2030, 2030, 2030, 2030, 2030),         // caloriesBmrAvg
          Arrays.asList(null, null, null, null, null, null, null),          // caloriesConsumedAvg
          Arrays.asList(448, 723, 26, 448, 451, 453, 410),                 // caloriesActiveAvg
          Arrays.asList(291, 487, null, 442, 443, 445, 409),               // activitiesCalories
          Arrays.asList(null, null, null, null, null, null, null),          // weightMin
          Arrays.asList(null, null, null, null, null, null, null),          // weightMax
          Arrays.asList(null, null, null, null, null, null, null),          // weightAvg
          Arrays.asList(3000, 3000, 3000, 3000, 3000, 3000, 3000),         // hydrationGoal
          Arrays.asList(0, 0, null, 0, 0, 0, 0),                           // hydrationIntake
          Arrays.asList(0, 0, null, 0, 0, 0, 0),                           // hydrationAvg
          Arrays.asList(295, 471, null, 411, 448, 370, 417),               // sweatLoss
          Arrays.asList(295, 471, null, 411, 448, 370, 417),               // sweatLossAvg
          Arrays.asList(26, 13, 24, 37, 42, 43, 32),                       // bbMin
          Arrays.asList(54, 63, 67, 89, 71, 74, 78),                       // bbMax
          Arrays.asList(30, 45, 24, 24, 27, 23, 24),                       // stressAvg
          Arrays.asList(10, 9, 9, 10, 10, 10, 10),                         // rrMin
          Arrays.asList(23, 25, 23, 23, 23, 23, 23),                       // rrMax
          Arrays.asList(15, 15, 14, 14, 14, 14, 14),                       // rrWakingAvg
          Arrays.asList(82, 85, 85, 78, 83, 82, 80),                       // spo2Min
          Arrays.asList(94, 95, 94, 96, 96, 94, 94),                       // spo2Avg
          Arrays.asList("08:27:00", "08:08:00", "10:52:00", "07:46:00", "08:20:00", "09:27:00", "08:20:00"), // sleepMin
          Arrays.asList("08:27:00", "08:08:00", "10:52:00", "07:46:00", "08:20:00", "09:27:00", "08:20:00"), // sleepMax
          Arrays.asList("08:27:00", "08:08:00", "10:52:00", "07:46:00", "08:20:00", "09:27:00", "08:20:00"), // sleepAvg
          Arrays.asList("01:29:00", "01:10:00", "02:04:00", "01:13:00", "01:18:00", "01:34:00", "01:14:00"), // remSleepMin
          Arrays.asList("01:29:00", "01:10:00", "02:04:00", "01:13:00", "01:18:00", "01:34:00", "01:14:00"), // remSleepMax
          Arrays.asList("01:29:00", "01:10:00", "02:04:00", "01:13:00", "01:18:00", "01:34:00", "01:14:00"), // remSleepAvg
          Arrays.asList(10000, 10000, 10000, 10000, 10000, 10000, 10000), // stepsGoal
          Arrays.asList(9543, 8946, 468, 8342, 8897, 7733, 8402),         // steps
          Arrays.asList(10, 10, 10, 10, 10, 10, 10),                        // floorsGoal
          Arrays.asList(6, 5, 0, 7, 6, 6, 6),                               // floors
          Arrays.asList(1, 2, null, 1, 1, 1, 1),                           // activities
          Arrays.asList(3.57549, 5.40078, null, 5.29658, 5.41812, 5.01491, 5.00717), // activitiesDistance
          Arrays.asList("00:21:25", "00:21:25", "00:21:25", "00:21:25", "00:21:25", "00:21:25", "00:21:25"), // intensityTimeGoal
          Arrays.asList("00:52:00", "01:47:00", "00:00:00", "01:27:00", "01:39:00", "01:39:00", "01:26:00"), // intensityTime
          Arrays.asList("00:16:00", "00:13:00", "00:00:00", "00:11:00", "00:09:00", "00:15:00", "00:14:00"), // moderateActivityTime
          Arrays.asList("00:18:00", "00:47:00", "00:00:00", "00:38:00", "00:45:00", "00:42:00", "00:36:00")  // vigorousActivityTime
      );

      when(recentDailySummariesRepo.findByLatestDay(refDate)).thenReturn(Optional.of(dummyModel));

      // Load the expected DTO from the JSON mock file.
      RecentDailySummariesDTO expectedDTO = loadRecentDailySummariesDTOFromJson();

      try (MockedStatic<RecentDailySummariesDTO> mockedConversion = Mockito.mockStatic(RecentDailySummariesDTO.class)) {
         // Stub the static conversion method so it returns the expected DTO.
         mockedConversion.when(() -> RecentDailySummariesDTO.fromModel(dummyModel))
             .thenReturn(expectedDTO);

         // WHEN: Call the service method.
         RecentDailySummariesDTO result = service.getRecentDailySummaries(refDate);

         // THEN: Assert the result is as expected.
         assertNotNull(result);
         assertEquals(expectedDTO, result);
      }
   }

   @Test
   void testGetRecentDailySummariesNotFound() {
      // GIVEN: A reference date for which the repository returns empty.
      LocalDate refDate = LocalDate.now();
      when(recentDailySummariesRepo.findByLatestDay(refDate)).thenReturn(Optional.empty());

      // WHEN/THEN: The service call should throw a GarminProcessingException.
      GarminProcessingException ex = assertThrows(GarminProcessingException.class, () -> service.getRecentDailySummaries(refDate));
      assertTrue(ex.getMessage().contains("No recent daily summaries found for"));
   }

   /**
    * 4️⃣ Test getWeekSummary(LocalDate referenceDate)
    * <ul>
    *   <li>✅ Returns a valid weekly summary DTO (loaded from JSON) when data is available.
    *   <li>❌ Throws GarminProcessingException when no weekly summary is found.
    * </ul>
    */
   @Test
   void testGetWeekSummaryFound() throws Exception {
      // GIVEN: A reference date and a dummy WeeklySummary model with a fake id.
      LocalDate refDate = LocalDate.of(2023, 5, 3);
      WeeklySummary dummyModel = new WeeklySummary("mongoId2", refDate, dummyBaseSummaryModel);
      when(weeklySummaryRepo.findByFirstDay(refDate)).thenReturn(Optional.of(dummyModel));

      // Load expected DTO from JSON.
      List<WeeklySummaryDTO> expectedDTOs = loadWeeksSummaryDTOsFromJson();
      WeeklySummaryDTO expectedDTO = expectedDTOs.get(0);

      try (MockedStatic<WeeklySummaryDTO> mockedConversion = Mockito.mockStatic(WeeklySummaryDTO.class)) {
         mockedConversion.when(() -> WeeklySummaryDTO.fromModel(dummyModel)).thenReturn(expectedDTO);

         // WHEN: Calling the service method.
         WeeklySummaryDTO result = service.getWeekSummary(refDate);

         // THEN: The result should not be null and equal to expected.
         assertNotNull(result);
         assertEquals(expectedDTO, result);
      }
   }

   @Test
   void testGetWeekSummaryNotFound() {
      // GIVEN: A reference date for which the repository returns empty.
      LocalDate refDate = LocalDate.now();
      when(weeklySummaryRepo.findByFirstDay(refDate)).thenReturn(Optional.empty());

      // WHEN/THEN: Calling getWeekSummary should throw a GarminProcessingException.
      GarminProcessingException ex = assertThrows(GarminProcessingException.class, () -> service.getWeekSummary(refDate));
      assertTrue(ex.getMessage().contains("No weekly summary found for"));
   }

   /**
    * 5️⃣ Test getMonthSummaries(Integer month, Integer year)
    * <ul>
    *   <li>✅ Returns a valid list of monthly summary DTOs (loaded from JSON) when data is available.
    * </ul>
    */
   @Test
   void testGetMonthSummaries() throws Exception {
      // GIVEN: A dummy MonthlySummary model with a fake id.
      MonthlySummary dummyModel = new MonthlySummary("mongoId3", LocalDate.of(2023, 5, 1), dummyBaseSummaryModel);
      when(monthlySummaryRepo.findByMonthAndYear(5, 2023)).thenReturn(Arrays.asList(dummyModel));

      // Load expected DTO from JSON.
      List<MonthlySummaryDTO> expectedDTOs = loadMonthsSummaryDTOsFromJson();
      MonthlySummaryDTO expectedDTO = expectedDTOs.get(0);

      try (MockedStatic<MonthlySummaryDTO> mockedConversion = Mockito.mockStatic(MonthlySummaryDTO.class)) {
         mockedConversion.when(() -> MonthlySummaryDTO.fromModel(dummyModel)).thenReturn(expectedDTO);

         // WHEN: Calling the service method.
         List<MonthlySummaryDTO> result = service.getMonthSummaries(5, 2023);

         // THEN: The result should not be null and contain one element equal to expected.
         assertNotNull(result);
         assertEquals(1, result.size());
         assertEquals(expectedDTO, result.get(0));
      }
   }

   /**
    * 6️⃣ Test getYearSummaries()
    * <ul>
    *   <li>✅ Returns a valid list of yearly summary DTOs (loaded from JSON) when data is available.
    * </ul>
    */
   @Test
   void testGetYearSummaries() throws Exception {
      // GIVEN: A dummy YearlySummary model with a fake id.
      YearlySummary dummyModel = new YearlySummary("mongoId4", LocalDate.of(2023, 1, 1), dummyBaseSummaryModel);
      when(yearlySummaryRepo.findAll()).thenReturn(Collections.singletonList(dummyModel));

      // Load expected DTO from JSON.
      List<YearlySummaryDTO> expectedDTOs = loadYearsSummaryDTOsFromJson();
      YearlySummaryDTO expectedDTO = expectedDTOs.get(0);

      try (MockedStatic<YearlySummaryDTO> mockedConversion = Mockito.mockStatic(YearlySummaryDTO.class)) {
         mockedConversion.when(() -> YearlySummaryDTO.fromModel(dummyModel)).thenReturn(expectedDTO);

         // WHEN: Calling the service method.
         List<YearlySummaryDTO> result = service.getYearSummaries();

         // THEN: The result should not be null and contain one element equal to expected.
         assertNotNull(result);
         assertEquals(1, result.size());
         assertEquals(expectedDTO, result.get(0));
      }
   }
}
