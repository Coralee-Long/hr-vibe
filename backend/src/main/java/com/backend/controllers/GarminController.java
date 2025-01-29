package com.backend.controllers;

import com.backend.services.GarminSQLiteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class GarminController {

   private final GarminSQLiteService garminSQLiteService;

   public GarminController(GarminSQLiteService garminSQLiteService) {
      this.garminSQLiteService = garminSQLiteService;
   }

   @GetMapping("/table-names")
   public Map<String, Object> getTableNames(@RequestParam String databaseName) {
      List<String> tableNames = garminSQLiteService.getAllTableNames(databaseName);
      return Map.of("tables", tableNames);
   }

   @GetMapping("/save-table")
   public String saveTable(@RequestParam String databaseName, @RequestParam String tableName) {
      garminSQLiteService.saveTableAsJson(databaseName, tableName);
      return "Table '" + tableName + "' from '" + databaseName + "' has been saved as JSON.";
   }

   @GetMapping("/save-all-tables")
   public Map<String, Object> saveAllTables(@RequestParam String databaseName) {
      List<String> savedTables = garminSQLiteService.saveAllTablesAsJson(databaseName);
      return Map.of("message", "All tables saved successfully", "tables", savedTables);
   }
}
