package com.backend.controllers;

import com.backend.dtos.*;
import com.backend.services.GarminRetrievalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * GarminRetrievalController provides GET endpoints for retrieving processed summary data from MongoDB.

 * Endpoints:
 * 1. GET /garmin/days
 *    - Retrieves an array of day summary DTOs. Supports an optional "limit" parameter to restrict the number of days.

 * 2. GET /garmin/days/{day}
 *    - Retrieves a single day summary DTO for the specified date.

 * 3. GET /garmin/recent/{referenceDate}
 *    - Retrieves the recent daily summaries (last 7 days) based on a reference date.

 * 4. GET /garmin/weeks
 *    - Retrieves an array of weekly summary DTOs. Supports an optional "limit" parameter to restrict the number of weeks.

 * 5. GET /garmin/weeks/{referenceDate}
 *    - Retrieves a single weekly summary DTO based on a reference date.

 * 6. GET /garmin/months
 *    - Retrieves an array of monthly summary DTOs. Supports optional "month" and "year" parameters.

 * 7. GET /garmin/years
 *    - Retrieves an array of yearly summary DTOs.

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
    * @param month optional parameter for the month (1-12). If provided, filters summaries for that month.
    * @param year optional parameter for the year. If provided, filters summaries for that year.
    * @return ResponseEntity containing the list of MonthlySummaryDTOs.
    */
   @GetMapping("/months")
   public ResponseEntity<List<MonthlySummaryDTO>> getMonthSummaries(
       @RequestParam(required = false) Integer month,
       @RequestParam(required = false) Integer year) {
      logger.info("Fetching month summaries with month={} and year={}", month, year);
      return ResponseEntity.ok(retrievalService.getMonthSummaries(month, year));
   }

   /**
    * Retrieves an array of yearly summary DTOs.
    *
    * @return ResponseEntity containing the list of YearlySummaryDTOs.
    */
   @GetMapping("/years")
   public ResponseEntity<List<YearlySummaryDTO>> getYearSummaries() {
      logger.info("Fetching year summaries...");
      return ResponseEntity.ok(retrievalService.getYearSummaries());
   }
}
