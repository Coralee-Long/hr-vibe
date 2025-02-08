package com.backend.services;

import static com.backend.utils.DataParsingUtils.*;
import static com.backend.utils.SummaryUtils.*;

import com.backend.exceptions.GarminProcessingException;
import com.backend.models.CurrentDaySummary;
import com.backend.models.MonthlySummary;
import com.backend.models.RecentDailySummaries;
import com.backend.models.WeeklySummary;
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
import java.util.function.Function;
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
    * Generic method to process, validate, and save summaries into MongoDB.
    *
    * @param databaseName the SQLite database name.
    * @param tableName the table name containing summary data.
    * @param dateKey a key used for logging purposes.
    * @param mapper a function that maps raw data into a domain model.
    * @param repo the MongoRepository to use for saving.
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

      // Convert raw data into summary objects.
      List<T> summaries = rawData.stream()
          .map(mapper)
          .peek(validationService::validate)
          .toList();

      if (summaries.isEmpty()) {
         throw new GarminProcessingException("No valid summaries could be mapped for table: " + tableName);
      }

      repo.saveAll(summaries); // Save all summaries at once

      logger.info("✅ Successfully saved {} summaries for {}", summaries.size(), dateKey);
   }

   // Processing methods using the generic method
   public void processAndSaveCurrentDaySummary(String db, String table) {
      processAndSaveSummary(db, table, "day", DataParsingUtils::mapToCurrentDaySummary, currentDaySummaryRepo);
   }

   public void processAndSaveWeeklySummary(String db, String table) {
      processAndSaveSummary(db, table, "first_day", DataParsingUtils::mapToWeeklySummary, weeklySummaryRepo);
   }

   /**
    * Processes and saves monthly summaries.
    *
    * <p>This method retrieves raw data from SQLite, maps it to MonthlySummary objects,
    * checks for duplicates by the firstDay field, and either updates an existing record or inserts a new one.
    *
    * @param databaseName the SQLite database name.
    * @param tableName the table name containing monthly summary data.
    */
   public void processAndSaveMonthlySummary(String databaseName, String tableName) {
      // Retrieve and parse raw data from SQLite.
      List<MonthlySummary> summaries = parseData(databaseName, tableName);

      for (MonthlySummary summary : summaries) {
         validationService.validate(summary);
         // Check if a record with the same firstDay already exists.
         Optional<MonthlySummary> existing = monthlySummaryRepo.findByFirstDay(summary.firstDay());

         if (existing.isPresent()) {
            // Update the existing document using a merge of the existing and new data.
            MonthlySummary updated = merge(existing.get(), summary);
            monthlySummaryRepo.save(updated);
         } else {
            // Insert a new document.
            monthlySummaryRepo.insert(summary);
         }
      }
   }

   public void processAndSaveYearlySummary(String db, String table) {
      processAndSaveSummary(db, table, "first_day", DataParsingUtils::mapToYearlySummary, yearlySummaryRepo);
   }

   /**
    * Process and save recent daily summaries (last 7 days) based on a reference date.
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

      RecentDailySummaries recentSummary = mapToRecentDailySummaries(last7Days);
      validationService.validate(recentSummary);
      recentDailySummariesRepo.save(recentSummary);

      logger.info("✅ Successfully saved RecentDailySummaries for latest day {}", recentSummary.latestDay());
   }

   /**
    * Parses raw data from SQLite into a list of MonthlySummary objects.
    *
    * @param databaseName the SQLite database name.
    * @param tableName the table name containing monthly summary data.
    * @return a List of MonthlySummary objects.
    * @throws GarminProcessingException if no data is found.
    */
   private List<MonthlySummary> parseData(String databaseName, String tableName) {
      List<Map<String, Object>> rawData = garminSQLiteRepo.fetchTableData(databaseName, tableName);
      if (rawData.isEmpty()) {
         throw new GarminProcessingException("No data found in table: " + tableName);
      }
      return rawData.stream()
          .map(DataParsingUtils::mapToMonthlySummary)
          .toList();
   }

   /**
    * Merges an existing MonthlySummary with a new incoming MonthlySummary.
    *
    * <p>This example merge simply keeps the existing record's id and firstDay, and updates the summary field.
    * Adjust this logic as needed if you require more complex merging.
    *
    * @param existing the existing MonthlySummary from the DB.
    * @param incoming the new MonthlySummary generated from raw data.
    * @return a merged MonthlySummary object.
    */
   private MonthlySummary merge(MonthlySummary existing, MonthlySummary incoming) {
      // In this simple merge, we replace the summary with the new one while preserving the id and firstDay.
      return new MonthlySummary(existing.id(), existing.firstDay(), incoming.summary());
   }
}
