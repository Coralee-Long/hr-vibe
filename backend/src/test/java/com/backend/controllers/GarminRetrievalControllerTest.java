package com.backend.controllers;

import com.backend.dtos.BaseSummaryDTO;
import com.backend.dtos.CurrentDaySummaryDTO;
import com.backend.dtos.MonthlySummaryDTO;
import com.backend.dtos.RecentDailySummariesDTO;
import com.backend.dtos.WeeklySummaryDTO;
import com.backend.dtos.YearlySummaryDTO;
import com.backend.exceptions.GarminProcessingException;
import com.backend.exceptions.GlobalExceptionHandler;
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
 * <p><b>Table of Contents:</b>
 * <ul>
 *   <li>1️⃣ <b>getAllDaySummaries(int limit)</b>
 *       - ✅ Returns a valid list of day summaries when data is available.
 *   <li>2️⃣ <b>getDaySummary(LocalDate day)</b>
 *       - ✅ Returns a valid day summary for a given date.
 *       - ❌ Throws {@link GarminProcessingException} when summary is not found.
 *   <li>3️⃣ <b>getRecentDailySummaries(LocalDate referenceDate)</b>
 *       - ✅ Returns a valid {@link RecentDailySummariesDTO} loaded from a JSON mock file.
 *   <li>4️⃣ <b>getWeekSummary(LocalDate referenceDate)</b>
 *       - ✅ Returns a valid weekly summary DTO for the reference date.
 *   <li>5️⃣ <b>getMonthSummaries(Integer month, Integer year)</b>
 *       - ✅ Returns a valid list of monthly summary DTOs when data is available.
 *   <li>6️⃣ <b>getYearSummaries()</b>
 *       - ✅ Returns a valid list of yearly summary DTOs when data is available.
 * </ul>
 * </p>
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
      // Setup the standalone controller with our controller and a global exception handler.
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
      CurrentDaySummaryDTO dummyDTO = new CurrentDaySummaryDTO("1", LocalDate.now(), baseSummary);
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
      CurrentDaySummaryDTO dummyDTO = new CurrentDaySummaryDTO("1", testDate, baseSummary);
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

      // WHEN: Perform GET /garmin/recents/{referenceDate}
      // THEN: Verify the response contains expected fields.
      // Instead of asserting latestDay as a single string, we now assert its components,
      // because the LocalDate is serialized as an array [year, month, day].
      mockMvc.perform(get("/garmin/recents/{referenceDate}", refDate.toString())
                          .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.latestDay[0]").value(2025))
          .andExpect(jsonPath("$.latestDay[1]").value(1))
          .andExpect(jsonPath("$.latestDay[2]").value(17))
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
      WeeklySummaryDTO dummyDTO = new WeeklySummaryDTO("1", refDate, baseSummary);
      when(retrievalService.getWeekSummary(eq(refDate))).thenReturn(dummyDTO);

      // WHEN: Perform GET /garmin/weeks/{referenceDate}.
      mockMvc.perform(get("/garmin/weeks/{referenceDate}", refDate.toString())
                          .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk());
   }

   /**
    * ✅ Test getMonthSummaries returns a valid list of monthly summary DTOs for the given month and year.
    */
   @Test
   void testGetMonthSummaries() throws Exception {
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
      MonthlySummaryDTO dummyDTO = new MonthlySummaryDTO("1", LocalDate.of(2023, 5, 1), baseSummary);
      when(retrievalService.getMonthSummaries(5, 2023)).thenReturn(java.util.Arrays.asList(dummyDTO));

      // WHEN: Perform GET /garmin/months with month and year parameters.
      // THEN: Expect a list of size 1.
      mockMvc.perform(get("/garmin/months")
                          .param("month", "5")
                          .param("year", "2023")
                          .accept(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(1)));
   }

   /**
    * ✅ Test getYearSummaries returns a valid list of yearly summary DTOs.
    */
   @Test
   void testGetYearSummaries() throws Exception {
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
      YearlySummaryDTO dummyDTO = new YearlySummaryDTO("1", LocalDate.of(2023, 1, 1), baseSummary);
      when(retrievalService.getYearSummaries()).thenReturn(Collections.singletonList(dummyDTO));

      // WHEN: Perform GET /garmin/years.
      // THEN: Expect a list of size 1.
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
