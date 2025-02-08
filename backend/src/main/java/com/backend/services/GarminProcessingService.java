package com.backend.services;

import com.backend.exceptions.GarminProcessingException;
import com.backend.models.CurrentDaySummary;
import com.backend.models.MonthlySummary;
import com.backend.models.RecentDailySummaries;
import com.backend.models.WeeklySummary;
import com.backend.models.YearlySummary;
import com.backend.repos.MongoDB.CurrentDaySummaryRepo;
import com.backend.repos.MongoDB.MonthlySummaryRepo;
import com.backend.repos.MongoDB.WeeklySummaryRepo;
import com.backend.repos.MongoDB.YearlySummaryRepo;
import com.backend.repos.MongoDB.RecentDailySummariesRepo;
import com.backend.repos.SQL.GarminSQLiteRepo;
import com.backend.utils.DataParsingUtils;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;

@Service
public class GarminProcessingService {

   private static final Logger logger = LoggerFactory.getLogger(GarminProcessingService.class);

   private final GarminSQLiteRepo garminSQLiteRepo;
   private final CurrentDaySummaryRepo currentDaySummaryRepo;
   private final WeeklySummaryRepo weeklySummaryRepo;
   private final MonthlySummaryRepo monthlySummaryRepo;
   private final YearlySummaryRepo yearlySummaryRepo;
   private final RecentDailySummariesRepo recentDailySummariesRepo;
   private final ValidationService validationService;

   public GarminProcessingService(GarminSQLiteRepo garminSQLiteRepo,
                                  CurrentDaySummaryRepo currentDaySummaryRepo,
                                  WeeklySummaryRepo weeklySummaryRepo,
                                  MonthlySummaryRepo monthlySummaryRepo,
                                  YearlySummaryRepo yearlySummaryRepo,
                                  RecentDailySummariesRepo recentDailySummariesRepo,
                                  ValidationService validationService) {
      this.garminSQLiteRepo = garminSQLiteRepo;
      this.currentDaySummaryRepo = currentDaySummaryRepo;
      this.weeklySummaryRepo = weeklySummaryRepo;
      this.monthlySummaryRepo = monthlySummaryRepo;
      this.yearlySummaryRepo = yearlySummaryRepo;
      this.recentDailySummariesRepo = recentDailySummariesRepo;
      this.validationService = validationService;
   }

   /**
    * Processes and saves current day summaries.
    * Duplicate check: uses the 'day' field.
    */
   public void processAndSaveCurrentDaySummary(String databaseName, String tableName) {
      List<Map<String, Object>> rawData;
      try {
         rawData = garminSQLiteRepo.fetchTableData(databaseName, tableName);
      } catch (RuntimeException e) {
         // Wrap any runtime exception in a GarminProcessingException
         throw new GarminProcessingException("Failed to process summary for " + tableName, e);
      }

      if (rawData.isEmpty()) {
         throw new GarminProcessingException("No data found in table: " + tableName);
      }

      List<CurrentDaySummary> summaries = rawData.stream()
          .map(DataParsingUtils::mapToCurrentDaySummary)
          .toList();

      for (CurrentDaySummary summary : summaries) {
         validationService.validate(summary);
         Optional<CurrentDaySummary> existing = currentDaySummaryRepo.findByDay(summary.day());
         if (existing.isPresent()) {
            CurrentDaySummary updated = mergeCurrentDay(existing.get(), summary);
            currentDaySummaryRepo.save(updated);
         } else {
            currentDaySummaryRepo.insert(summary);
         }
      }
      logger.info("✅ Successfully processed and saved CurrentDaySummaries.");
   }

   /**
    * Processes and saves weekly summaries.
    * Duplicate check: uses the 'firstDay' field.
    */
   public void processAndSaveWeeklySummary(String databaseName, String tableName) {
      List<Map<String, Object>> rawData = garminSQLiteRepo.fetchTableData(databaseName, tableName);
      if (rawData.isEmpty()) {
         throw new GarminProcessingException("No data found in table: " + tableName);
      }
      List<WeeklySummary> summaries = rawData.stream()
          .map(DataParsingUtils::mapToWeeklySummary)
          .toList();
      for (WeeklySummary summary : summaries) {
         validationService.validate(summary);
         Optional<WeeklySummary> existing = weeklySummaryRepo.findByFirstDay(summary.firstDay());
         if (existing.isPresent()) {
            WeeklySummary updated = mergeWeekly(existing.get(), summary);
            weeklySummaryRepo.save(updated);
         } else {
            weeklySummaryRepo.insert(summary);
         }
      }
      logger.info("✅ Successfully processed and saved WeeklySummaries.");
   }

   /**
    * Processes and saves monthly summaries.
    * Duplicate check: uses the 'firstDay' field.
    */
   public void processAndSaveMonthlySummary(String databaseName, String tableName) {
      List<MonthlySummary> summaries = parseMonthlyData(databaseName, tableName);
      for (MonthlySummary summary : summaries) {
         validationService.validate(summary);
         Optional<MonthlySummary> existing = monthlySummaryRepo.findByFirstDay(summary.firstDay());
         if (existing.isPresent()) {
            MonthlySummary updated = mergeMonthly(existing.get(), summary);
            monthlySummaryRepo.save(updated);
         } else {
            monthlySummaryRepo.insert(summary);
         }
      }
      logger.info("✅ Successfully processed and saved MonthlySummaries.");
   }

   /**
    * Processes and saves yearly summaries.
    * Duplicate check: uses the 'firstDay' field.
    */
   public void processAndSaveYearlySummary(String databaseName, String tableName) {
      List<Map<String, Object>> rawData = garminSQLiteRepo.fetchTableData(databaseName, tableName);
      if (rawData.isEmpty()) {
         throw new GarminProcessingException("No data found in table: " + tableName);
      }
      List<YearlySummary> summaries = rawData.stream()
          .map(DataParsingUtils::mapToYearlySummary)
          .toList();
      for (YearlySummary summary : summaries) {
         validationService.validate(summary);
         Optional<YearlySummary> existing = yearlySummaryRepo.findByFirstDay(summary.firstDay());
         if (existing.isPresent()) {
            YearlySummary updated = mergeYearly(existing.get(), summary);
            yearlySummaryRepo.save(updated);
         } else {
            yearlySummaryRepo.insert(summary);
         }
      }
      logger.info("✅ Successfully processed and saved YearlySummaries.");
   }

   /**
    * Processes and saves recent daily summaries (last 7 days) based on a reference date.
    *
    * Duplicate check: uses the 'latestDay' field.
    *
    * @param referenceDate the reference date (in ISO format, e.g. "2025-01-17") as a String.
    */
   public void processAndSaveRecentDailySummaries(String referenceDate) {
      logger.info("Fetching daily summaries for the 7-day period ending at reference date {}...", referenceDate);
      LocalDate refDate = LocalDate.parse(referenceDate);
      List<CurrentDaySummary> last7Days = new ArrayList<>(currentDaySummaryRepo.findTop7ByDayLessThanEqualOrderByDayDesc(refDate));

      if (last7Days.isEmpty()) {
         logger.warn("No daily summary data found for reference date {}. Skipping RecentDailySummaries update.", referenceDate);
         return;
      }

      // Ensure the list is sorted descending.
      last7Days.sort(Comparator.comparing(CurrentDaySummary::day).reversed());

      RecentDailySummaries recentSummary = DataParsingUtils.mapToRecentDailySummaries(last7Days);
      validationService.validate(recentSummary);

      Optional<RecentDailySummaries> existing = recentDailySummariesRepo.findByLatestDay(recentSummary.latestDay());
      if (existing.isPresent()) {
         RecentDailySummaries updated = mergeRecent(existing.get(), recentSummary);
         recentDailySummariesRepo.save(updated);
      } else {
         // Disambiguate the call by casting to RecentDailySummaries so that the single-entity overload is chosen.
         recentDailySummariesRepo.insert((RecentDailySummaries) recentSummary);
      }

      logger.info("✅ Successfully processed and saved RecentDailySummaries for latest day {}", recentSummary.latestDay());
   }


   // Helper to parse monthly data.
   private List<MonthlySummary> parseMonthlyData(String databaseName, String tableName) {
      List<Map<String, Object>> rawData = garminSQLiteRepo.fetchTableData(databaseName, tableName);
      if (rawData.isEmpty()) {
         throw new GarminProcessingException("No data found in table: " + tableName);
      }
      return rawData.stream()
          .map(DataParsingUtils::mapToMonthlySummary)
          .toList();
   }

   // Merge methods: In these simple merges, we preserve the existing record's id and key,
   // and update the summary data from the incoming record.

   private CurrentDaySummary mergeCurrentDay(CurrentDaySummary existing, CurrentDaySummary incoming) {
      return new CurrentDaySummary(existing.id(), existing.day(), incoming.summary());
   }

   private WeeklySummary mergeWeekly(WeeklySummary existing, WeeklySummary incoming) {
      return new WeeklySummary(existing.id(), existing.firstDay(), incoming.summary());
   }

   private MonthlySummary mergeMonthly(MonthlySummary existing, MonthlySummary incoming) {
      return new MonthlySummary(existing.id(), existing.firstDay(), incoming.summary());
   }

   private YearlySummary mergeYearly(YearlySummary existing, YearlySummary incoming) {
      return new YearlySummary(existing.id(), existing.firstDay(), incoming.summary());
   }

   private RecentDailySummaries mergeRecent(RecentDailySummaries existing, RecentDailySummaries incoming) {
      return new RecentDailySummaries(
          existing.id(),
          existing.latestDay(),
          incoming.hrMin(),
          incoming.hrMax(),
          incoming.hrAvg(),
          incoming.rhrMin(),
          incoming.rhrMax(),
          incoming.rhrAvg(),
          incoming.inactiveHrMin(),
          incoming.inactiveHrMax(),
          incoming.inactiveHrAvg(),
          incoming.caloriesAvg(),
          incoming.caloriesGoal(),
          incoming.caloriesBmrAvg(),
          incoming.caloriesConsumedAvg(),
          incoming.caloriesActiveAvg(),
          incoming.activitiesCalories(),
          incoming.weightMin(),
          incoming.weightMax(),
          incoming.weightAvg(),
          incoming.hydrationGoal(),
          incoming.hydrationIntake(),
          incoming.hydrationAvg(),
          incoming.sweatLoss(),
          incoming.sweatLossAvg(),
          incoming.bbMin(),
          incoming.bbMax(),
          incoming.stressAvg(),
          incoming.rrMin(),
          incoming.rrMax(),
          incoming.rrWakingAvg(),
          incoming.spo2Min(),
          incoming.spo2Avg(),
          incoming.sleepMin(),
          incoming.sleepMax(),
          incoming.sleepAvg(),
          incoming.remSleepMin(),
          incoming.remSleepMax(),
          incoming.remSleepAvg(),
          incoming.stepsGoal(),
          incoming.steps(),
          incoming.floorsGoal(),
          incoming.floors(),
          incoming.activities(),
          incoming.activitiesDistance(),
          incoming.intensityTimeGoal(),
          incoming.intensityTime(),
          incoming.moderateActivityTime(),
          incoming.vigorousActivityTime()
      );
   }
}
