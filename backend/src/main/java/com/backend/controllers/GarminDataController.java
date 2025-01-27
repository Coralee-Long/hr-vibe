package com.backend.controllers;

import com.backend.services.GarminDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GarminDataController {

   private final GarminDataService garminDataService;

   @Autowired
   public GarminDataController(GarminDataService garminDataService) {
      this.garminDataService = garminDataService;
   }

   @GetMapping("/garmin/data")
   public String getGarminData(@RequestParam String tableName) {
      garminDataService.fetchAndPrintData(tableName);
      return "Data fetched successfully! Check the console output.";
   }
}
