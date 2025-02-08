package com.backend.controllers;

import com.backend.dtos.BaseSummaryDTO;
import com.backend.dtos.CurrentDaySummaryDTO;
import com.backend.dtos.MonthlySummaryDTO;
import com.backend.dtos.RecentDailySummariesDTO;
import com.backend.dtos.WeeklySummaryDTO;
import com.backend.dtos.YearlySummaryDTO;
import com.backend.exceptions.GarminProcessingException;
import com.backend.services.GarminRetrievalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * GarminRetrievalController provides GET endpoints for retrieving processed summary data from MongoDB.
 *
 * Endpoints:
 * 1. GET /garmin/days
 *    - Retrieves an array of day summary DTOs. Supports an optional "limit" parameter.
 *
 * 2. GET /garmin/days/{day}
 *    - Retrieves a single day summary DTO for the specified date.
 *
 * 3. GET /garmin/recent/{referenceDate}
 *    - Retrieves the recent daily summaries (last 7 days) based on a reference date.
 *
 * 4. GET /garmin/weeks
 *    - Retrieves an array of weekly summary DTOs. Supports an optional "limit" parameter.
 *
 * 5. GET /garmin/weeks/{referenceDate}
 *    - Retrieves a single weekly summary DTO based on a reference date.
 *
 * 6. GET /garmin/months
 *    - Retrieves an array of monthly summary DTOs.
 *      If no query parameter is provided, returns all monthly summaries.
 *      If the "year" parameter is provided, returns only the monthly summaries for that year.
 *
 * 7. GET /garmin/years
 *    - Retrieves an array of yearly summary DTOs.
 *
 * The controller uses DTOs to ensure that only the required fields are exposed to the frontend,
 * and validation annotations ensure the data conforms to expected formats.
 */
@RestController
@RequestMapping("/garmin")
public class GarminRetrievalController {

   private static final Logger logger = LoggerFactory.getLogger(GarminRetrievalController.class);
   private final GarminRetrievalService retrievalService;

   public GarminRetrievalController(GarminRetrievalService retrievalService) {
      this.retrievalService = retrievalService;
   }

   /**
    * Retrieves an array of day summary DTOs.
    *
    * @param limit optional parameter to limit the number of day summaries returned.
    * @return ResponseEntity containing the list of CurrentDaySummaryDTO objects.
    */
   @GetMapping("/days")
   public ResponseEntity<List<CurrentDaySummaryDTO>> getAllDaySummaries(@RequestParam(defaultValue = "30") int limit) {
      logger.info("Fetching up to {} day summaries...", limit);
      return ResponseEntity.ok(retrievalService.getAllDaySummaries(limit));
   }

   /**
    * Retrieves a single day summary DTO for the specified date.
    *
    * @param day the day (in ISO format) to retrieve the summary for.
    * @return ResponseEntity containing the CurrentDaySummaryDTO.
    */
   @GetMapping("/days/{day}")
   public ResponseEntity<CurrentDaySummaryDTO> getDaySummary(@PathVariable String day) {
      logger.info("Fetching day summary for date {}...", day);
      return ResponseEntity.ok(retrievalService.getDaySummary(LocalDate.parse(day)));
   }

   /**
    * Retrieves the recent daily summaries (last 7 days) based on a reference date.
    *
    * @param referenceDate the reference date (in ISO format) to fetch the recent daily summaries.
    * @return ResponseEntity containing the RecentDailySummariesDTO.
    */
   @GetMapping("/recent/{referenceDate}")
   public ResponseEntity<RecentDailySummariesDTO> getRecentDailySummaries(@PathVariable String referenceDate) {
      logger.info("Fetching recent daily summaries for reference date {}...", referenceDate);
      return ResponseEntity.ok(retrievalService.getRecentDailySummaries(LocalDate.parse(referenceDate)));
   }

   /**
    * Retrieves an array of weekly summary DTOs.
    *
    * @param limit optional parameter to limit the number of weekly summaries returned.
    * @return ResponseEntity containing the list of WeeklySummaryDTO objects.
    */
   @GetMapping("/weeks")
   public ResponseEntity<List<WeeklySummaryDTO>> getAllWeekSummaries(@RequestParam(defaultValue = "30") int limit) {
      logger.info("Fetching up to {} weekly summaries...", limit);
      return ResponseEntity.ok(retrievalService.getAllWeekSummaries(limit));
   }

   /**
    * Retrieves a weekly summary DTO based on the reference date.
    *
    * @param referenceDate the reference date to determine the week.
    * @return ResponseEntity containing the WeeklySummaryDTO.
    */
   @GetMapping("/weeks/{referenceDate}")
   public ResponseEntity<WeeklySummaryDTO> getWeekSummary(@PathVariable String referenceDate) {
      logger.info("Fetching weekly summary for reference date {}...", referenceDate);
      return ResponseEntity.ok(retrievalService.getWeekSummary(LocalDate.parse(referenceDate)));
   }

   /**
    * Retrieves an array of monthly summary DTOs.
    *
    * <p>If no query parameters are provided, returns all monthly summaries.
    * If the "year" parameter is provided, returns only the monthly summaries for that year.
    *
    * @param year optional; filter by year.
    * @return ResponseEntity containing the list of MonthlySummaryDTO objects.
    */
   @GetMapping("/months")
   public ResponseEntity<List<MonthlySummaryDTO>> getMonthSummaries(
       @RequestParam(required = false) Integer year) {
      logger.info("Fetching monthly summaries with year={}", year);
      try {
         List<MonthlySummaryDTO> dtos;
         if (year == null) {
            // Retrieve all monthly summaries.
            dtos = retrievalService.getMonthSummaries(null);
         } else {
            // Retrieve only monthly summaries for the specified year.
            dtos = retrievalService.getMonthSummaries(year);
         }
         return ResponseEntity.ok(dtos);
      } catch (GarminProcessingException e) {
         logger.error("Error retrieving monthly summaries: {}", e.getMessage(), e);
         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
      } catch (Exception e) {
         logger.error("Unexpected error retrieving monthly summaries: {}", e.getMessage(), e);
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
      }
   }

   /**
    * Retrieves an array of yearly summary DTOs.
    *
    * @return ResponseEntity containing the list of YearlySummaryDTO objects.
    */
   @GetMapping("/years")
   public ResponseEntity<List<YearlySummaryDTO>> getYearSummaries() {
      logger.info("Fetching year summaries...");
      return ResponseEntity.ok(retrievalService.getYearSummaries());
   }
}
