package com.backend.controllers;

import com.backend.services.GarminService;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/garmin")
public class GarminController {

   private final GarminService garminService;

   public GarminController (GarminService garminService) {
      this.garminService = garminService;
   }

   /**
    * Processes and saves the current day's summary from SQLite to MongoDB.
    */
   @PostMapping("/process/current-day-summary")
   public String processCurrentDaySummary(@RequestParam String databaseName, @RequestParam String tableName) {
      garminService.processAndSaveCurrentDaySummary(databaseName, tableName);
      return "Processed and saved CurrentDaySummary.";
   }

   /**
    * Processes and saves the weekly summary from SQLite to MongoDB.
    */
   @PostMapping("/process/weekly-summary")
   public String processWeeklySummary(@RequestParam String databaseName, @RequestParam String tableName) {
      garminService.processAndSaveWeeklySummary(databaseName, tableName);
      return "Processed and saved WeeklySummary.";
   }

   /**
    * Processes and saves the monthly summary from SQLite to MongoDB.
    */
   @PostMapping("/process/monthly-summary")
   public String processMonthlySummary(@RequestParam String databaseName, @RequestParam String tableName) {
      garminService.processAndSaveMonthlySummary(databaseName, tableName);
      return "Processed and saved MonthlySummary.";
   }

   /**
    * Processes and saves the yearly summary from SQLite to MongoDB.
    */
   @PostMapping("/process/yearly-summary")
   public String processYearlySummary(@RequestParam String databaseName, @RequestParam String tableName) {
      garminService.processAndSaveYearlySummary(databaseName, tableName);
      return "Processed and saved YearlySummary.";
   }

   /**
    * Processes and saves the recent daily summaries (last 7 days) from MongoDB.
    */
   @PostMapping("/process/recent-daily-summaries")
   public String processAndSaveRecentDailySummaries() {
      garminService.processAndSaveRecentDailySummaries();
      return "Processed and saved RecentDailySummaries.";
   }
}
