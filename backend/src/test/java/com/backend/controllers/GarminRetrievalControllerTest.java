package com.backend.controllers;

import com.backend.dtos.BaseSummaryDTO;
import com.backend.dtos.CurrentDaySummaryDTO;
import com.backend.dtos.MonthlySummaryDTO;
import com.backend.dtos.RecentDailySummariesDTO;
import com.backend.dtos.WeeklySummaryDTO;
import com.backend.dtos.YearlySummaryDTO;
import com.backend.exceptions.GarminProcessingException;
import com.backend.exceptions.GlobalExceptionHandler;
import com.backend.controllers.GarminRetrievalController;
import com.backend.services.GarminRetrievalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for {@link com.backend.controllers.GarminRetrievalController}.
 *
 * Table of Contents:
 * <ul>
 *   <li>1️⃣ getAllDaySummaries(int limit)
 *      - ✅ Returns a valid list of day summaries when data is available.
 *   <li>2️⃣ getDaySummary(LocalDate day)
 *      - ✅ Returns a valid day summary for a given date.
 *      - ❌ Throws {@link GarminProcessingException} when a summary is not found.
 *   <li>3️⃣ getRecentDailySummaries(LocalDate referenceDate)
 *      - ✅ Returns a valid {@link RecentDailySummariesDTO} loaded from a JSON mock file.
 *   <li>4️⃣ getWeekSummary(LocalDate referenceDate)
 *      - ✅ Returns a valid weekly summary DTO for the reference date.
 *   <li>5️⃣ getAllWeekSummaries(int limit)
 *      - ✅ Returns a valid list of weekly summary DTOs when data is available.
 *   <li>6️⃣ getMonthSummaries(Integer year)
 *      - ✅ Returns all monthly summary DTOs when no year is provided.
 *      - ✅ Returns monthly summary DTOs for the given year when a year is provided.
 *   <li>7️⃣ getYearSummaries()
 *      - ✅ Returns a valid list of yearly summary DTOs when data is available.
 * </ul>
 */
class GarminRetrievalControllerTest {

   private MockMvc mockMvc;

   @InjectMocks
   private GarminRetrievalController controller;

   @Mock
   private GarminRetrievalService retrievalService; // Injected service dependency

   private ObjectMapper objectMapper;

   @BeforeEach
   void setUp() {
      MockitoAnnotations.openMocks(this);
      // Register JavaTimeModule to support LocalDate deserialization.
      objectMapper = new ObjectMapper();
      objectMapper.registerModule(new JavaTimeModule());
      // Set up the standalone controller with our controller and a global exception handler.
      mockMvc = MockMvcBuilders.standaloneSetup(controller)
          .setControllerAdvice(new GlobalExceptionHandler())
          .build();
   }

   /**
    * ✅ Test getAllDaySummaries returns a valid list of day summaries when data is available.
    */
   @Test
   void testGetAllDaySummaries() throws Exception {
      // GIVEN: A dummy BaseSummaryDTO and a corresponding CurrentDaySummaryDTO.
      BaseSummaryDTO baseSummary = new BaseSummaryDTO(
          60, 120, 80, 60, 80, 70, 55, 75, 65,
          2000, 2500, 1500, 1800, 500, 300,
          60.0, 65.0, 62.0,
          3000, 2800, 2900, 100, 110,
          20, 80, 30,
          10, 20, 15, 85, 90,
          "08:00:00", "09:00:00", "08:30:00",
          "01:00:00", "01:15:00", "01:05:00",
          10000, 9000, 10, 8,
          2, 5.0, "00:30:00", "00:45:00", "00:20:00", "00:10:00"
      );
      CurrentDaySummaryDTO dummyDTO = new CurrentDaySummaryDTO("1", LocalDate.now().toString(), baseSummary);
      // WHEN: The retrievalService instance returns the dummy list.
      when(retrievalService.getAllDaySummaries(anyInt())).thenReturn(Collections.singletonList(dummyDTO));

      // THEN: Perform GET /garmin/days?limit=30 and expect one element in the returned JSON array.
      mockMvc.perform(get("/garmin/days")
                          .param("limit", "30")
                          .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(1)));
   }

   /**
    * ✅ Test getDaySummary returns the expected day summary DTO for a given date.
    */
   @Test
   void testGetDaySummary() throws Exception {
      LocalDate testDate = LocalDate.of(2023, 5, 1);
      BaseSummaryDTO baseSummary = new BaseSummaryDTO(
          60, 120, 80, 60, 80, 70, 55, 75, 65,
          2000, 2500, 1500, 1800, 500, 300,
          60.0, 65.0, 62.0,
          3000, 2800, 2900, 100, 110,
          20, 80, 30,
          10, 20, 15, 85, 90,
          "08:00:00", "09:00:00", "08:30:00",
          "01:00:00", "01:15:00", "01:05:00",
          10000, 9000, 10, 8,
          2, 5.0, "00:30:00", "00:45:00", "00:20:00", "00:10:00"
      );
      CurrentDaySummaryDTO dummyDTO = new CurrentDaySummaryDTO("1", testDate.toString(), baseSummary);
      when(retrievalService.getDaySummary(eq(testDate))).thenReturn(dummyDTO);

      // WHEN: Perform GET /garmin/days/{day} with a valid date.
      mockMvc.perform(get("/garmin/days/{day}", testDate.toString())
                          .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());
   }

   /**
    * ❌ Test getDaySummary throws GarminProcessingException when no summary is found for the given date.
    */
   @Test
   void testGetDaySummaryNotFound() throws Exception {
      LocalDate testDate = LocalDate.of(2023, 5, 1);
      when(retrievalService.getDaySummary(eq(testDate)))
          .thenThrow(new GarminProcessingException("No day summary found for " + testDate));

      // WHEN: Perform GET /garmin/days/{day} with a date that has no data.
      // THEN: Expect a Bad Request status and an error message in the response.
      mockMvc.perform(get("/garmin/days/{day}", testDate.toString())
                          .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.error").value("Garmin Processing Error"));
   }

   /**
    * ✅ Test getRecentDailySummaries returns a valid RecentDailySummariesDTO loaded from the JSON mock file.
    */
   @Test
   void testGetRecentDailySummaries() throws Exception {
      LocalDate refDate = LocalDate.of(2023, 5, 2);
      // Load dummy DTO from JSON file (expected to have cleaned data)
      RecentDailySummariesDTO dummyDTO = loadRecentDailySummariesFromJson();
      when(retrievalService.getRecentDailySummaries(eq(refDate))).thenReturn(dummyDTO);

      // WHEN: Perform GET /garmin/recent/{referenceDate}
      // THEN: Verify the response contains expected fields.
      // Assert that latestDay is now serialized as a single string.
      mockMvc.perform(get("/garmin/recent/{referenceDate}", refDate.toString())
                          .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.latestDay").value("2025-01-17"))
          .andExpect(jsonPath("$.hrMin", hasSize(7)));
   }

   /**
    * ✅ Test getWeekSummary returns a valid weekly summary DTO for the given reference date.
    */
   @Test
   void testGetWeekSummary() throws Exception {
      LocalDate refDate = LocalDate.of(2023, 5, 3);
      BaseSummaryDTO baseSummary = new BaseSummaryDTO(
          60, 120, 80, 60, 80, 70, 55, 75, 65,
          2000, 2500, 1500, 1800, 500, 300,
          60.0, 65.0, 62.0,
          3000, 2800, 2900, 100, 110,
          20, 80, 30,
          10, 20, 15, 85, 90,
          "08:00:00", "09:00:00", "08:30:00",
          "01:00:00", "01:15:00", "01:05:00",
          10000, 9000, 10, 8,
          2, 5.0, "00:30:00", "00:45:00", "00:20:00", "00:10:00"
      );
      WeeklySummaryDTO dummyDTO = new WeeklySummaryDTO("1", refDate.toString(), baseSummary);
      when(retrievalService.getWeekSummary(eq(refDate))).thenReturn(dummyDTO);

      // WHEN: Perform GET /garmin/weeks/{referenceDate}
      mockMvc.perform(get("/garmin/weeks/{referenceDate}", refDate.toString())
                          .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          // Verify that the field is now "firstDay" with the correct value.
          .andExpect(jsonPath("$.firstDay").value("2023-05-03"))
          // Verify a summary field is correctly nested.
          .andExpect(jsonPath("$.summary.hrMin").value(60));
   }

   /**
    * ✅ Test getMonthSummaries returns a valid list of monthly summary DTOs when no year is provided.
    */
   @Test
   void testGetMonthSummaries_All() throws Exception {
      MonthlySummaryDTO dummyDTO = new MonthlySummaryDTO("1", LocalDate.of(2023, 5, 1).toString(),
                                                         new BaseSummaryDTO(60, 120, 80, 60, 80, 70, 55, 75, 65,
                                                                            2000, 2500, 1500, 1800, 500, 300,
                                                                            60.0, 65.0, 62.0,
                                                                            3000, 2800, 2900, 100, 110,
                                                                            20, 80, 30,
                                                                            10, 20, 15, 85, 90,
                                                                            "08:00:00", "09:00:00", "08:30:00",
                                                                            "01:00:00", "01:15:00", "01:05:00",
                                                                            10000, 9000, 10, 8,
                                                                            2, 5.0, "00:30:00", "00:45:00", "00:20:00", "00:10:00"));
      when(retrievalService.getMonthSummaries(null)).thenReturn(Collections.singletonList(dummyDTO));

      // WHEN: Perform GET /garmin/months with no query parameters.
      mockMvc.perform(get("/garmin/months")
                          .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(1)));
   }

   /**
    * ✅ Test getMonthSummaries returns a valid list of monthly summary DTOs when a year is provided.
    */
   @Test
   void testGetMonthSummaries_ByYear() throws Exception {
      int year = 2023;
      MonthlySummaryDTO dummyDTO = new MonthlySummaryDTO("1", LocalDate.of(2023, 5, 1).toString(),
                                                         new BaseSummaryDTO(60, 120, 80, 60, 80, 70, 55, 75, 65,
                                                                            2000, 2500, 1500, 1800, 500, 300,
                                                                            60.0, 65.0, 62.0,
                                                                            3000, 2800, 2900, 100, 110,
                                                                            20, 80, 30,
                                                                            10, 20, 15, 85, 90,
                                                                            "08:00:00", "09:00:00", "08:30:00",
                                                                            "01:00:00", "01:15:00", "01:05:00",
                                                                            10000, 9000, 10, 8,
                                                                            2, 5.0, "00:30:00", "00:45:00", "00:20:00", "00:10:00"));
      when(retrievalService.getMonthSummaries(eq(year))).thenReturn(Collections.singletonList(dummyDTO));

      // WHEN: Perform GET /garmin/months with the year parameter.
      mockMvc.perform(get("/garmin/months")
                          .param("year", String.valueOf(year))
                          .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(1)));
   }

   /**
    * ✅ Test getYearSummaries returns a valid list of yearly summary DTOs.
    */
   @Test
   void testGetYearSummaries() throws Exception {
      YearlySummaryDTO dummyDTO = new YearlySummaryDTO("1", LocalDate.of(2023, 1, 1).toString(),
                                                       new BaseSummaryDTO(60, 120, 80, 60, 80, 70, 55, 75, 65,
                                                                          2000, 2500, 1500, 1800, 500, 300,
                                                                          60.0, 65.0, 62.0,
                                                                          3000, 2800, 2900, 100, 110,
                                                                          20, 80, 30,
                                                                          10, 20, 15, 85, 90,
                                                                          "08:00:00", "09:00:00", "08:30:00",
                                                                          "01:00:00", "01:15:00", "01:05:00",
                                                                          10000, 9000, 10, 8,
                                                                          2, 5.0, "00:30:00", "00:45:00", "00:20:00", "00:10:00"));
      when(retrievalService.getYearSummaries()).thenReturn(Collections.singletonList(dummyDTO));

      // WHEN: Perform GET /garmin/years.
      mockMvc.perform(get("/garmin/years")
                          .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(1)));
   }

   /**
    * Helper method that loads a RecentDailySummariesDTO from the JSON file
    * located at /resources/mocks/DTOs/mongo_mock_recent_daily_summariesDTO.json.
    *
    * @return the RecentDailySummariesDTO parsed from the JSON file.
    * @throws Exception if there is an error reading or parsing the file.
    */
   private RecentDailySummariesDTO loadRecentDailySummariesFromJson() throws Exception {
      InputStream is = getClass().getResourceAsStream("/mocks/DTOs/mongo_mock_recent_daily_summariesDTO.json");
      return objectMapper.readValue(is, RecentDailySummariesDTO.class);
   }
}
