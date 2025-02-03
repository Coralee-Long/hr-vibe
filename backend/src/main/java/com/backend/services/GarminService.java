package com.backend.services;

import static com.backend.utils.DataParsingUtils.*;
import static com.backend.utils.SummaryUtils.*;

import com.backend.models.*;
import com.backend.repos.MongoDB.*;
import com.backend.repos.SQL.GarminSQLiteRepo;
import com.backend.services.ValidationService;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Service
public class GarminService {

   private static final Logger logger = LoggerFactory.getLogger(GarminService.class);

   private final GarminSQLiteRepo garminSQLiteRepo;
   private final CurrentDaySummaryRepo currentDaySummaryRepo;
   private final WeeklySummaryRepo weeklySummaryRepo;
   private final MonthlySummaryRepo monthlySummaryRepo;
   private final YearlySummaryRepo yearlySummaryRepo;
   private final RecentDailySummariesRepo recentDailySummariesRepo;
   private final ValidationService validationService;

   public GarminService(GarminSQLiteRepo garminSQLiteRepo,
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
    * Generic method to process, validate, and save a summary.
    */
   protected <T> void processAndSaveSummary(
       String databaseName,
       String tableName,
       Function<Map<String, Object>, T> mapper,
       MongoRepository<T, String> repo) {
      logger.info("Fetching data from SQLite: {}, table: {}", databaseName, tableName);

      List<Map<String, Object>> rawData = garminSQLiteRepo.fetchTableData(databaseName, tableName);
      if (rawData.isEmpty()) {
         logger.warn("No data found in table: {}", tableName);
         return;
      }

      Optional<Map<String, Object>> latestData = getLatestData(rawData, "day");
      if (latestData.isEmpty()) {
         logger.warn("No valid data found for table: {}", tableName);
         return;
      }

      T summary = mapper.apply(latestData.get());

      // ✅ Validate before saving
      validationService.validate(summary);

      repo.save(summary);
      logger.info("✅ Successfully saved {} summary for {}", summary.getClass().getSimpleName(), "day");
   }

   public void processAndSaveCurrentDaySummary(String db, String table) {
      processAndSaveSummary(db, table, this::mapToCurrentDaySummary, currentDaySummaryRepo);
   }

   private CurrentDaySummary mapToCurrentDaySummary(Map<String, Object> data) {
      return new CurrentDaySummary(null, LocalDate.parse(data.get("day").toString()), mapToBaseSummary(data));
   }

   public void processAndSaveWeeklySummary(String db, String table) {
      processAndSaveSummary(db, table, this::mapToWeeklySummary, weeklySummaryRepo);
   }

   private WeeklySummary mapToWeeklySummary(Map<String, Object> data) {
      return new WeeklySummary(null, LocalDate.parse(data.get("week_start").toString()), mapToBaseSummary(data));
   }

   public void processAndSaveMonthlySummary(String db, String table) {
      processAndSaveSummary(db, table, this::mapToMonthlySummary, monthlySummaryRepo);
   }

   private MonthlySummary mapToMonthlySummary(Map<String, Object> data) {
      return new MonthlySummary(null, LocalDate.parse(data.get("month_start").toString()), mapToBaseSummary(data));
   }

   public void processAndSaveYearlySummary(String db, String table) {
      processAndSaveSummary(db, table, this::mapToYearlySummary, yearlySummaryRepo);
   }

   private YearlySummary mapToYearlySummary(Map<String, Object> data) {
      return new YearlySummary(null, LocalDate.parse(data.get("year_start").toString()), mapToBaseSummary(data));
   }

   public void processAndSaveRecentDailySummaries() {
      logger.info("Fetching last 7 days of daily summaries...");

      List<CurrentDaySummary> last7Days = currentDaySummaryRepo.findTop7ByOrderByDayDesc();

      if (last7Days.isEmpty()) {
         logger.warn("No daily summary data found. Skipping RecentDailySummaries update.");
         return;
      }

      RecentDailySummaries recentSummary = mapToRecentDailySummaries(last7Days);

      // ✅ Validate before saving
      validationService.validate(recentSummary);

      recentDailySummariesRepo.save(recentSummary);
      logger.info("✅ Successfully saved RecentDailySummaries for latest day {}", recentSummary.latestDay());
   }
}
