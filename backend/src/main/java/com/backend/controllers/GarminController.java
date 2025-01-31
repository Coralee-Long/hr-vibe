package com.backend.controllers;

import com.backend.services.GarminService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class GarminController {

//   private final GarminService garminService;
//
//   public GarminController (GarminService garminService) {
//      this.garminService = garminService;
//   }
//
//   @GetMapping("/table-names")
//   public Map<String, Object> getTableNames(@RequestParam String databaseName) {
//      List<String> tableNames = garminService.getAllTableNames(databaseName);
//      return Map.of("tables", tableNames);
//   }
//
//   @GetMapping("/save-table")
//   public String saveTable(@RequestParam String databaseName, @RequestParam String tableName) {
//      garminService.saveTableAsJson(databaseName, tableName);
//      return "Table '" + tableName + "' from '" + databaseName + "' has been saved as JSON.";
//   }
//
//   @GetMapping("/save-all-tables")
//   public Map<String, Object> saveAllTables(@RequestParam String databaseName) {
//      List<String> savedTables = garminService.saveAllTablesAsJson(databaseName);
//      return Map.of("message", "All tables saved successfully", "tables", savedTables);
//   }
}
