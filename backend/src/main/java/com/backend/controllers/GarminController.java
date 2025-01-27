package com.backend.controllers;

import com.backend.services.GarminSQLiteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GarminController {

   private final GarminSQLiteService garminSQLiteService;

   public GarminController(GarminSQLiteService garminSQLiteService) {
      this.garminSQLiteService = garminSQLiteService;
   }

   @GetMapping("/table-data")
   public String getTableData(@RequestParam String tableName) {
      garminSQLiteService.printTableData(tableName);
      return "Data for table '" + tableName + "' printed to console.";
   }
}
