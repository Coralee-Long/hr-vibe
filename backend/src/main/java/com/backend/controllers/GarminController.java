package com.backend.controllers;

import com.backend.services.GarminSQLiteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class GarminController {

   private final GarminSQLiteService garminSQLiteService;

   public GarminController(GarminSQLiteService garminSQLiteService) {
      this.garminSQLiteService = garminSQLiteService;
   }

   // Endpoint to save daily_summary table as JSON
   @GetMapping("/save-table")
   public String saveTable(@RequestParam String tableName) {
      garminSQLiteService.saveTableAsJson(tableName);
      return "Table '" + tableName + "' has been saved as JSON.";
   }

   // Endpoint to fetch daily_summary table data as JSON
   @GetMapping("/fetch-table")
   public List<Map<String, Object>> fetchTable(@RequestParam String tableName) {
      return garminSQLiteService.fetchTableData(tableName);
   }
}
