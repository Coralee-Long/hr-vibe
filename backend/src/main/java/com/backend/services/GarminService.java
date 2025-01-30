package com.backend.services;

import static com.backend.utils.DataParsingUtils.*;
import static com.backend.utils.SummaryUtils.*;

import com.backend.models.*;
import com.backend.repos.MongoDB.*;
import com.backend.repos.SQL.GarminSQLiteRepo;

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

   public GarminService(GarminSQLiteRepo garminSQLiteRepo,
                        CurrentDaySummaryRepo currentDaySummaryRepo,
                        WeeklySummaryRepo weeklySummaryRepo,
                        MonthlySummaryRepo monthlySummaryRepo,
                        YearlySummaryRepo yearlySummaryRepo,
                        RecentDailySummariesRepo recentDailySummariesRepo){
      this.garminSQLiteRepo = garminSQLiteRepo;
      this.currentDaySummaryRepo = currentDaySummaryRepo;
      this.weeklySummaryRepo = weeklySummaryRepo;
      this.monthlySummaryRepo = monthlySummaryRepo;
      this.yearlySummaryRepo = yearlySummaryRepo;
      this.recentDailySummariesRepo = recentDailySummariesRepo;
   }

   /**
    * Generic method to process and save a summary.
    */
   private <T> void processAndSaveSummary (String databaseName, String tableName, String dateColumn,
                                          Function<Map<String, Object>, T> mapper, MongoRepository<T, String> repo) {
      logger.info("Fetching data from SQLite: {}, table: {}", databaseName, tableName);

      List<Map<String, Object>> rawData = garminSQLiteRepo.fetchTableData(databaseName, tableName);
      if (rawData.isEmpty()) {
         logger.warn("No data found in table: {}", tableName);
         return;
      }

      Optional<Map<String, Object>> latestData = getLatestData(rawData, dateColumn);
      if (latestData.isEmpty()) {
         logger.warn("No valid data found for table: {}", tableName);
         return;
      }

      T summary = mapper.apply(latestData.get());
      repo.save(summary);
      logger.info("✅ Successfully saved {} summary for {}", summary.getClass().getSimpleName(), dateColumn);
   }

   public void processAndSaveCurrentDaySummary(String db, String table) {
      processAndSaveSummary(db, table, "day", this::mapToCurrentDaySummary, currentDaySummaryRepo);
   }

   private CurrentDaySummary mapToCurrentDaySummary(Map<String, Object> data) {
      return new CurrentDaySummary(null, LocalDate.parse(data.get("day").toString()), mapToBaseSummary(data));
   }
}
