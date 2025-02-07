package com.backend.controllers;

import com.backend.exceptions.GarminProcessingException;
import com.backend.services.GarminProcessingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * GarminProcessingController is responsible for processing SQLite data and storing the results
 * into different MongoDB collections. Each endpoint triggers the processing of an array (i.e. a collection)
 * of summary objects corresponding to a specific period (days, weeks, months, years).

 * Endpoints:
 * 1. POST /garmin/process/days
 *    - Processes and saves an array of current day summary objects from SQLite into the MongoDB collection.

 * 2. POST /garmin/process/weeks
 *    - Processes and saves an array of weekly summary objects from SQLite into the MongoDB collection.

 * 3. POST /garmin/process/months
 *    - Processes and saves an array of monthly summary objects from SQLite into the MongoDB collection.

 * 4. POST /garmin/process/years
 *    - Processes and saves an array of yearly summary objects from SQLite into the MongoDB collection.

 * 5. POST /garmin/process/recent
 *    - Processes and saves recent daily summaries from SQLite into MongoDB.
 *      The endpoint accepts a reference date as a parameter, retrieves the last 7 days of day summaries
 *      relative to that date, reformats them into a single RecentDailySummaries object (each numeric field is an array of 7 values),
 *      and saves that object.

 * Each endpoint returns a JSON response indicating success or an error message with details.
 * Custom exceptions thrown from the service layer are caught and returned with appropriate HTTP status codes.
 */
@RestController
@RequestMapping("/garmin")
public class GarminProcessingController {

   private static final Logger logger = LoggerFactory.getLogger(GarminProcessingController.class);
   private final GarminProcessingService garminProcessingService;

   public GarminProcessingController (GarminProcessingService garminProcessingService) {
      this.garminProcessingService = garminProcessingService;
   }

   /**
    * Processes and saves an array of current day summary objects from SQLite to the MongoDB collection.
    *
    * @param databaseName the SQLite database name.
    * @param tableName    the table name containing current day summary data.
    * @return ResponseEntity with a JSON success message or error details.
    */
   @PostMapping("/process/days")
   public ResponseEntity<Map<String, String>> processCurrentDaySummary(
       @RequestParam String databaseName,
       @RequestParam String tableName) {

      logger.info("Received request: databaseName={}, tableName={}", databaseName, tableName);

      try {
         garminProcessingService.processAndSaveCurrentDaySummary(databaseName, tableName);
         logger.info("Successfully processed CurrentDaySummaries.");
         return ResponseEntity.ok(Map.of("message", "Processed and saved an array of CurrentDaySummary objects."));
      } catch (GarminProcessingException e) {
         logger.error("Error processing CurrentDaySummaries: {}", e.getMessage(), e);
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
             .body(Map.of("error", "Failed to process CurrentDaySummaries.", "details", e.getMessage()));
      } catch (Exception e) {
         logger.error("Unexpected error processing CurrentDaySummaries", e);
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
             .body(Map.of("error", "Unexpected error processing CurrentDaySummaries.", "details", e.getMessage()));
      }
   }

   /**
    * Processes and saves an array of weekly summary objects from SQLite to the MongoDB collection.
    *
    * @param databaseName the SQLite database name.
    * @param tableName    the table name containing weekly summary data.
    * @return ResponseEntity with a JSON success message or error details.
    */
   @PostMapping("/process/weeks")
   public ResponseEntity<Map<String, String>> processWeeklySummary(
       @RequestParam String databaseName,
       @RequestParam String tableName) {
      logger.info("Starting processing for WeeklySummaries. DB='{}', Table='{}'", databaseName, tableName);
      try {
         garminProcessingService.processAndSaveWeeklySummary(databaseName, tableName);
         logger.info("Successfully processed WeeklySummaries.");
         return ResponseEntity.ok(Map.of("message", "Processed and saved an array of WeeklySummary objects."));
      } catch (GarminProcessingException e) {
         logger.error("Error processing WeeklySummaries: {}", e.getMessage(), e);
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
             .body(Map.of("error", "Failed to process WeeklySummaries.", "details", e.getMessage()));
      } catch (Exception e) {
         logger.error("Unexpected error processing WeeklySummaries: {}", e.getMessage(), e);
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
             .body(Map.of("error", "Unexpected error processing WeeklySummaries.", "details", e.getMessage()));
      }
   }

   /**
    * Processes and saves an array of monthly summary objects from SQLite to the MongoDB collection.
    *
    * @param databaseName the SQLite database name.
    * @param tableName    the table name containing monthly summary data.
    * @return ResponseEntity with a JSON success message or error details.
    */
   @PostMapping("/process/months")
   public ResponseEntity<Map<String, String>> processMonthlySummary(
       @RequestParam String databaseName,
       @RequestParam String tableName) {
      logger.info("Starting processing for MonthlySummaries. DB='{}', Table='{}'", databaseName, tableName);
      try {
         garminProcessingService.processAndSaveMonthlySummary(databaseName, tableName);
         logger.info("Successfully processed MonthlySummaries.");
         return ResponseEntity.ok(Map.of("message", "Processed and saved an array of MonthlySummary objects."));
      } catch (GarminProcessingException e) {
         logger.error("Error processing MonthlySummaries: {}", e.getMessage(), e);
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
             .body(Map.of("error", "Failed to process MonthlySummaries.", "details", e.getMessage()));
      } catch (Exception e) {
         logger.error("Unexpected error processing MonthlySummaries: {}", e.getMessage(), e);
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
             .body(Map.of("error", "Unexpected error processing MonthlySummaries.", "details", e.getMessage()));
      }
   }

   /**
    * Processes and saves an array of yearly summary objects from SQLite to the MongoDB collection.
    *
    * @param databaseName the SQLite database name.
    * @param tableName    the table name containing yearly summary data.
    * @return ResponseEntity with a JSON success message or error details.
    */
   @PostMapping("/process/years")
   public ResponseEntity<Map<String, String>> processYearlySummary(
       @RequestParam String databaseName,
       @RequestParam String tableName) {
      logger.info("Starting processing for YearlySummaries. DB='{}', Table='{}'", databaseName, tableName);
      try {
         garminProcessingService.processAndSaveYearlySummary(databaseName, tableName);
         logger.info("Successfully processed YearlySummaries.");
         return ResponseEntity.ok(Map.of("message", "Processed and saved an array of YearlySummary objects."));
      } catch (GarminProcessingException e) {
         logger.error("Error processing YearlySummaries: {}", e.getMessage(), e);
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
             .body(Map.of("error", "Failed to process YearlySummaries.", "details", e.getMessage()));
      } catch (Exception e) {
         logger.error("Unexpected error processing YearlySummaries: {}", e.getMessage(), e);
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
             .body(Map.of("error", "Unexpected error processing YearlySummaries.", "details", e.getMessage()));
      }
   }

   /**
    * Processes and saves the recent daily summaries (last 7 days) from SQLite to MongoDB.
    * The endpoint accepts a reference date (in ISO format) as a request parameter.
    *
    * @param referenceDate the reference date to determine the 7-day period.
    * @return ResponseEntity containing a JSON success message or error details.
    */
   @PostMapping("/process/recent")
   public ResponseEntity<Map<String, String>> processAndSaveRecentDailySummaries(
       @RequestParam String referenceDate) {
      logger.info("Starting processing for RecentDailySummaries with reference date '{}'.", referenceDate);
      try {
         garminProcessingService.processAndSaveRecentDailySummaries(referenceDate);
         logger.info("Successfully processed RecentDailySummaries.");
         return ResponseEntity.ok(Map.of("message", "Processed and saved RecentDailySummaries."));
      } catch (GarminProcessingException e) {
         logger.error("Error processing RecentDailySummaries: {}", e.getMessage(), e);
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
             .body(Map.of("error", "Failed to process RecentDailySummaries.", "details", e.getMessage()));
      } catch (Exception e) {
         logger.error("Unexpected error processing RecentDailySummaries: {}", e.getMessage(), e);
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
             .body(Map.of("error", "Unexpected error processing RecentDailySummaries.", "details", e.getMessage()));
      }
   }
}
