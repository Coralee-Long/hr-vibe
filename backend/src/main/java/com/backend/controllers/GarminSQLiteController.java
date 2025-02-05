package com.backend.controllers;

import com.backend.services.GarminDataExportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping ("/garmin-sqlite")
public class GarminSQLiteController {

   private static final Logger logger = LoggerFactory.getLogger(GarminSQLiteController.class);
   private final GarminDataExportService garminDataExportService;

   public GarminSQLiteController(GarminDataExportService garminDataExportService) {
      this.garminDataExportService = garminDataExportService;
   }

   /**
    * Retrieves all table names from SQLite (for debugging).
    */
   @GetMapping ("/table-names")
   public Map<String, Object> getTableNames (@RequestParam String databaseName) {
      List<String> tableNames = garminDataExportService.getAllTableNames(databaseName);
      return Map.of("tables", tableNames);
   }

   /**
    * Exports a specific table as a JSON file.
    */
   @PostMapping("/export-table-as-json")
   public ResponseEntity<String> exportTableAsJson(@RequestBody Map<String, String> request) {
      String databaseName = request.get("databaseName");
      String tableName = request.get("tableName");

      logger.info("📌 Received request for DB: '{}' Table: '{}'", databaseName, tableName);

      garminDataExportService.saveTableAsJson(databaseName, tableName);
      return ResponseEntity.ok("Table '" + tableName + "' has been exported as JSON.");
   }

   /**
    * Exports all SQLite tables as JSON files.
    */
   @PostMapping("/export-all-tables-as-json")
   public Map<String, Object> exportAllTablesAsJson(@RequestParam String databaseName) {
      List<String> savedTables = garminDataExportService.saveAllTablesAsJson(databaseName);
      return Map.of("message", "All tables exported successfully", "tables", savedTables);
   }
}
