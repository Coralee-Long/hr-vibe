package com.backend.controllers;

import com.backend.services.GarminSQLiteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")  // Base path for all API routes
public class GarminController {

   private final GarminSQLiteService garminSQLiteService;

   public GarminController(GarminSQLiteService garminSQLiteService) {
      this.garminSQLiteService = garminSQLiteService;
   }

   /**
    * ✅ Fetch all table names from the database.
    * URL: GET /api/table-names
    */
   @GetMapping("/table-names")
   public Map<String, Object> getTableNames() {
      List<String> tableNames = garminSQLiteService.getAllTableNames();
      return Map.of("tables", tableNames);
   }

   /**
    * ✅ Save a specific table as a JSON file.
    * URL: GET /api/save-table?tableName={tableName}
    */
   @GetMapping("/save-table")
   public String saveTable(@RequestParam String tableName) {
      garminSQLiteService.saveTableAsJson(tableName);
      return "Table '" + tableName + "' has been saved as JSON.";
   }

   /**
    * ✅ Fetch table data as JSON.
    * URL: GET /api/fetch-table?tableName={tableName}
    */
   @GetMapping("/fetch-table")
   public List<Map<String, Object>> fetchTable(@RequestParam String tableName) {
      return garminSQLiteService.fetchTableData(tableName);
   }

   /**
    * ✅ Save all tables in the database as JSON.
    * URL: GET /api/save-all-tables
    */
   @GetMapping("/save-all-tables")
   public Map<String, Object> saveAllTables() {
      List<String> savedTables = garminSQLiteService.saveAllTablesAsJson();
      return Map.of("message", "All tables saved successfully", "tables", savedTables);
   }
}
