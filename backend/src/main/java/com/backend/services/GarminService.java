package com.backend.services;

import static com.backend.utils.DataParsingUtils.*;
import static com.backend.utils.SummaryUtils.*;

import com.backend.exceptions.GarminProcessingException;
import com.backend.models.*;
import com.backend.repos.MongoDB.*;
import com.backend.repos.SQL.GarminSQLiteRepo;
import com.backend.utils.DataParsingUtils;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.*;
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
    * ✅ Generic method to process, validate, and save summaries into MongoDB.
    */
   private <T> void processAndSaveSummary(
       String databaseName,
       String tableName,
       String dateKey,
       Function<Map<String, Object>, T> mapper,
       MongoRepository<T, String> repo) {
      logger.info("Fetching data from SQLite: {}, table: {}", databaseName, tableName);

      List<Map<String, Object>> rawData;
      try {
         rawData = garminSQLiteRepo.fetchTableData(databaseName, tableName);
      } catch (RuntimeException e) {
         throw new GarminProcessingException("Failed to process summary for " + tableName, e);
      }

      if (rawData.isEmpty()) {
         throw new GarminProcessingException("No data found in table: " + tableName);
      }

      Optional<Map<String, Object>> latestData = getLatestData(rawData, dateKey);
      if (latestData.isEmpty()) {
         throw new GarminProcessingException("No valid data found for table: " + tableName);
      }

      T summary = mapper.apply(latestData.get());
      validationService.validate(summary);
      repo.save(summary);

      logger.info("✅ Successfully saved {} summary for {}", summary.getClass().getSimpleName(), dateKey);
   }

   // 🔹 Processing Methods - Now using the `DataParsingUtils` mapping methods
   public void processAndSaveCurrentDaySummary(String db, String table) {
      processAndSaveSummary(db, table, "day", DataParsingUtils::mapToCurrentDaySummary, currentDaySummaryRepo);
   }

   public void processAndSaveWeeklySummary(String db, String table) {
      processAndSaveSummary(db, table, "first_day", DataParsingUtils::mapToWeeklySummary, weeklySummaryRepo);
   }

   public void processAndSaveMonthlySummary(String db, String table) {
      processAndSaveSummary(db, table, "first_day", DataParsingUtils::mapToMonthlySummary, monthlySummaryRepo);
   }

   public void processAndSaveYearlySummary(String db, String table) {
      processAndSaveSummary(db, table, "first_day", DataParsingUtils::mapToYearlySummary, yearlySummaryRepo);
   }

   /**
    * ✅ Process and save recent daily summaries (last 7 days) based on a reference date.
    *
    * This method uses the provided reference date (in ISO format) to determine the period over which to gather
    * the last 7 days of daily summaries, reformats them into a RecentDailySummaries object (each field becomes an array of 7 values),
    * validates the result, and saves it to MongoDB.
    *
    * @param referenceDate the reference date (in ISO format, e.g. "2025-01-17") as a String.
    */
   public void processAndSaveRecentDailySummaries(String referenceDate) {
      logger.info("Fetching daily summaries for the 7-day period ending at reference date {}...", referenceDate);
      LocalDate refDate = LocalDate.parse(referenceDate);
      // Create a mutable copy of the list returned by the repository
      List<CurrentDaySummary> last7Days = new ArrayList<>(currentDaySummaryRepo.findTop7ByDayLessThanEqualOrderByDayDesc(refDate));

      if (last7Days.isEmpty()) {
         logger.warn("No daily summary data found for reference date {}. Skipping RecentDailySummaries update.", referenceDate);
         return;
      }

      // Ensure the list is sorted descending.
      last7Days.sort(Comparator.comparing(CurrentDaySummary::day).reversed());

      RecentDailySummaries recentSummary = mapToRecentDailySummaries(last7Days);
      validationService.validate(recentSummary);
      recentDailySummariesRepo.save(recentSummary);

      logger.info("✅ Successfully saved RecentDailySummaries for latest day {}", recentSummary.latestDay());
   }
}
