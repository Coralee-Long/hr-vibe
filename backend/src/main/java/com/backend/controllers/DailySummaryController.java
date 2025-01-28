package com.backend.controllers;

import com.backend.dtos.CreateDailySummariesDTO;
import com.backend.models.DailySummary;
import com.backend.services.DailySummaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/daily-summary")
public class DailySummaryController {
   private final DailySummaryService dailySummaryService;

   public DailySummaryController(DailySummaryService dailySummaryService) {
      this.dailySummaryService = dailySummaryService;
   }

   @PostMapping("/all")
   public ResponseEntity<List<DailySummary>> saveAllDailySummaries(@RequestBody CreateDailySummariesDTO request) {
      return ResponseEntity.ok(dailySummaryService.saveAllDailySummaries(request));
   }

   @GetMapping("/{date}")
   public ResponseEntity<DailySummary> getDailySummaryByDate(@PathVariable String date) {
      return dailySummaryService.getDailySummaryByDate(date)
          .map(ResponseEntity::ok)
          .orElse(ResponseEntity.notFound().build());
   }
}
