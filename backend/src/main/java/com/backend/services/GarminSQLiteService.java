package com.backend.services;

import com.backend.models.CurrentDaySummary;
import com.backend.repos.MongoDB.CurrentDaySummaryRepo;
import com.backend.repos.SQL.GarminSQLiteRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class GarminSQLiteService {

   private final GarminSQLiteRepo garminSQLiteRepo;
   private final CurrentDaySummaryRepo currentDaySummaryRepo;

   public GarminSQLiteService (GarminSQLiteRepo garminSQLiteRepo, CurrentDaySummaryRepo currentDaySummaryRepo) {
      this.garminSQLiteRepo = garminSQLiteRepo;
      this.currentDaySummaryRepo = currentDaySummaryRepo;
   }

   public List<Map<String, Object>> fetchTableData (String databaseName, String tableName) {
      return garminSQLiteRepo.fetchTableData(databaseName, tableName);
   }

   public void saveTableAsJson (String databaseName, String tableName) {
      garminSQLiteRepo.saveTableAsJson(databaseName, tableName);
   }

   public List<String> getAllTableNames (String databaseName) {
      return garminSQLiteRepo.getAllTableNames(databaseName);
   }

   public List<String> saveAllTablesAsJson (String databaseName) {
      return garminSQLiteRepo.saveAllTablesAsJson(databaseName);
   }
}
