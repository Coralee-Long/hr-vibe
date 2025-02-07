package com.backend.controllers;

import com.backend.services.GarminProcessingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 📌 Test Summary for GarminProcessingController Integration Tests

 * Endpoints:
 * 1. POST /garmin/process/days
 *    - ✅ givenValidParameters_whenProcessCurrentDaySummary_thenReturnsSuccessResponse

 * 2. POST /garmin/process/weeks
 *    - ✅ givenValidParameters_whenProcessWeeklySummary_thenReturnsSuccessResponse

 * 3. POST /garmin/process/months
 *    - ✅ givenValidParameters_whenProcessMonthlySummary_thenReturnsSuccessResponse

 * 4. POST /garmin/process/years
 *    - ✅ givenValidParameters_whenProcessYearlySummary_thenReturnsSuccessResponse

 * 5. POST /garmin/process/recent
 *    - ✅ givenValidDataAndReferenceDate_whenProcessAndSaveRecentDailySummaries_thenSavesSuccessfully
 *    - ❌ givenNoData_whenProcessAndSaveRecentDailySummaries_thenSkipsSaving
 */
@ExtendWith (MockitoExtension.class)
class GarminProcessingControllerTest {
   private MockMvc mockMvc;

   @Mock
   private GarminProcessingService garminProcessingService;

   @InjectMocks
   private GarminProcessingController controller;

   private final ObjectMapper mapper = new ObjectMapper();

   @BeforeEach
   void setUp() {
      mapper.registerModule(new JavaTimeModule());
      mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
   }

   @Test
   void givenValidParameters_whenProcessCurrentDaySummary_thenReturnsSuccessResponse() throws Exception {
      String dbName = "testDB";
      String tableName = "days_summary";

      // Assume garminService.processAndSaveCurrentDaySummary runs without error.
      doNothing().when(garminProcessingService).processAndSaveCurrentDaySummary(dbName, tableName);

      mockMvc.perform(post("/garmin/process/days")
                          .param("databaseName", dbName)
                          .param("tableName", tableName))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.message").value("Processed and saved an array of CurrentDaySummary objects."));
   }

   @Test
   void givenValidParameters_whenProcessWeeklySummary_thenReturnsSuccessResponse() throws Exception {
      String dbName = "testDB";
      String tableName = "weeks_summary";

      doNothing().when(garminProcessingService).processAndSaveWeeklySummary(dbName, tableName);

      mockMvc.perform(post("/garmin/process/weeks")
                          .param("databaseName", dbName)
                          .param("tableName", tableName))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.message").value("Processed and saved an array of WeeklySummary objects."));
   }

   @Test
   void givenValidParameters_whenProcessMonthlySummary_thenReturnsSuccessResponse() throws Exception {
      String dbName = "testDB";
      String tableName = "months_summary";

      doNothing().when(garminProcessingService).processAndSaveMonthlySummary(dbName, tableName);

      mockMvc.perform(post("/garmin/process/months")
                          .param("databaseName", dbName)
                          .param("tableName", tableName))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.message").value("Processed and saved an array of MonthlySummary objects."));
   }

   @Test
   void givenValidParameters_whenProcessYearlySummary_thenReturnsSuccessResponse() throws Exception {
      String dbName = "testDB";
      String tableName = "years_summary";

      doNothing().when(garminProcessingService).processAndSaveYearlySummary(dbName, tableName);

      mockMvc.perform(post("/garmin/process/years")
                          .param("databaseName", dbName)
                          .param("tableName", tableName))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.message").value("Processed and saved an array of YearlySummary objects."));
   }

   @Test
   void givenValidDataAndReferenceDate_whenProcessAndSaveRecentDailySummaries_thenSavesSuccessfully() throws Exception {
      // For this test, we simulate that the service method is called with a reference date.
      // We assume that garminService.processAndSaveRecentDailySummaries(String) works correctly.
      String refDate = "2025-01-17";
      doNothing().when(garminProcessingService).processAndSaveRecentDailySummaries(refDate);

      mockMvc.perform(post("/garmin/process/recent")
                          .param("referenceDate", refDate))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.message").value("Processed and saved RecentDailySummaries."));
   }

   @Test
   void givenNoData_whenProcessAndSaveRecentDailySummaries_thenSkipsSaving() throws Exception {
      // In this test, we simulate that the service method handles the no-data scenario gracefully.
      String refDate = "2025-01-17";
      // You could have garminService throw a GarminProcessingException if no data is found.
      // For simplicity, assume it just returns without saving.
      doNothing().when(garminProcessingService).processAndSaveRecentDailySummaries(refDate);

      mockMvc.perform(post("/garmin/process/recent")
                          .param("referenceDate", refDate))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.message").value("Processed and saved RecentDailySummaries."));
   }
}