package com.backend.services;

import com.backend.repos.GarminSQLiteRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GarminSQLiteService {

   private final GarminSQLiteRepo garminSQLiteRepo;

   public GarminSQLiteService(GarminSQLiteRepo garminSQLiteRepo) {
      this.garminSQLiteRepo = garminSQLiteRepo;
   }

   public List<Map<String, Object>> fetchTableData(String databaseName, String tableName) {
      return garminSQLiteRepo.fetchTableData(databaseName, tableName);
   }

   public void saveTableAsJson(String databaseName, String tableName) {
      garminSQLiteRepo.saveTableAsJson(databaseName, tableName);
   }

   public List<String> getAllTableNames(String databaseName) {
      return garminSQLiteRepo.getAllTableNames(databaseName);
   }

   public List<String> saveAllTablesAsJson(String databaseName) {
      return garminSQLiteRepo.saveAllTablesAsJson(databaseName);
   }
}
