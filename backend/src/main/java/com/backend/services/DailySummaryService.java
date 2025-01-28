package com.backend.services;

import com.backend.models.DailySummary;
import com.backend.repos.DailySummaryRepo;
import com.backend.dtos.CreateDailySummariesDTO;
import com.backend.dtos.DailySummaryDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DailySummaryService {
   private final DailySummaryRepo dailySummaryRepo;

   public DailySummaryService(DailySummaryRepo dailySummaryRepo) {
      this.dailySummaryRepo = dailySummaryRepo;
   }

   // ✅ Save all daily summaries (Upsert: update if exists, insert if new)
   public List<DailySummary> saveAllDailySummaries(CreateDailySummariesDTO request) {
      List<DailySummary> summariesToSave = request.summaries().stream().map(summary -> {
         Optional<DailySummary> existing = dailySummaryRepo.findByDay(summary.day());
         return existing.map(dailySummary ->
                                 new DailySummary(
                                     dailySummary.id(), // Keep existing ID
                                     summary.step_goal(),
                                     summary.hr_max(),
                                     summary.distance(),
                                     summary.hydration_intake(),
                                     summary.intensity_time_goal(),
                                     summary.calories_total(),
                                     summary.description(),
                                     summary.calories_goal(),
                                     summary.sweat_loss(),
                                     summary.floors_up(),
                                     summary.hydration_goal(),
                                     summary.day(),
                                     summary.stress_avg(),
                                     summary.bb_charged(),
                                     summary.rr_max(),
                                     summary.calories_bmr(),
                                     summary.bb_min(),
                                     summary.rr_min(),
                                     summary.bb_max(),
                                     summary.spo2_avg(),
                                     summary.spo2_min(),
                                     summary.steps(),
                                     summary.floors_down(),
                                     summary.calories_consumed(),
                                     summary.rr_waking_avg(),
                                     summary.calories_active(),
                                     summary.floors_goal(),
                                     summary.hr_min(),
                                     summary.vigorous_activity_time(),
                                     summary.moderate_activity_time(),
                                     summary.rhr()
                                 )
                            ).orElse(new DailySummary(
             null,
             summary.step_goal(),
             summary.hr_max(),
             summary.distance(),
             summary.hydration_intake(),
             summary.intensity_time_goal(),
             summary.calories_total(),
             summary.description(),
             summary.calories_goal(),
             summary.sweat_loss(),
             summary.floors_up(),
             summary.hydration_goal(),
             summary.day(),
             summary.stress_avg(),
             summary.bb_charged(),
             summary.rr_max(),
             summary.calories_bmr(),
             summary.bb_min(),
             summary.rr_min(),
             summary.bb_max(),
             summary.spo2_avg(),
             summary.spo2_min(),
             summary.steps(),
             summary.floors_down(),
             summary.calories_consumed(),
             summary.rr_waking_avg(),
             summary.calories_active(),
             summary.floors_goal(),
             summary.hr_min(),
             summary.vigorous_activity_time(),
             summary.moderate_activity_time(),
             summary.rhr()
         ));
      }).collect(Collectors.toList());

      return dailySummaryRepo.saveAll(summariesToSave);
   }



   // ✅ Get one summary by date
   public Optional<DailySummary> getDailySummaryByDate(String date) {
      return dailySummaryRepo.findByDay(date);
   }
}
