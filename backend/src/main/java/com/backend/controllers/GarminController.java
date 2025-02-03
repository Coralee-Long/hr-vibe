package com.backend.controllers;

import com.backend.repos.SQL.GarminSQLiteRepo;
import com.backend.services.GarminService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/garmin")
public class GarminController {

   private final GarminService garminService;
   private final GarminSQLiteRepo garminSQLiteRepo;

   public GarminController (GarminService garminService, GarminSQLiteRepo garminSQLiteRepo) {
      this.garminService = garminService;
      this.garminSQLiteRepo = garminSQLiteRepo;
   }

   /**
    * Retrieves all table names from the specified SQLite database.
    * Useful for debugging and verifying available tables.
    */
   @GetMapping("/table-names")
   public Map<String, Object> getTableNames(@RequestParam String databaseName) {
      List<String> tableNames = garminSQLiteRepo.getAllTableNames(databaseName);
      return Map.of("tables", tableNames);
   }

   /**
    * Saves a specified table from SQLite as a JSON file for debugging purposes.
    */
   @GetMapping("/save-table")
   public String saveTable(@RequestParam String databaseName, @RequestParam String tableName) {
      garminSQLiteRepo.saveTableAsJson(databaseName, tableName);
      return "Table '" + tableName + "' from '" + databaseName + "' has been saved as JSON.";
   }

   /**
    * Saves all tables from SQLite as JSON files for debugging.
    */
   @GetMapping("/save-all-tables")
   public Map<String, Object> saveAllTables(@RequestParam String databaseName) {
      List<String> savedTables = garminSQLiteRepo.saveAllTablesAsJson(databaseName);
      return Map.of("message", "All tables saved successfully", "tables", savedTables);
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
   public String processRecentDailySummaries() {
      garminService.processAndSaveRecentDailySummaries();
      return "Processed and saved RecentDailySummaries.";
   }
}
