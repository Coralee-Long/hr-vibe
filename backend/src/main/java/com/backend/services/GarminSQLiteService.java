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

   /**
    * ✅ Fetches data from a specific table.
    */
   public List<Map<String, Object>> fetchTableData(String tableName) {
      return garminSQLiteRepo.fetchTableData(tableName);
   }

   /**
    * ✅ Saves a specific table as a JSON file.
    */
   public void saveTableAsJson(String tableName) {
      garminSQLiteRepo.saveTableAsJson(tableName);
   }

   /**
    * ✅ Fetches a list of all table names.
    */
   public List<String> getAllTableNames() {
      return garminSQLiteRepo.getAllTableNames();
   }

   /**
    * ✅ Saves all tables as JSON files.
    */
   public List<String> saveAllTablesAsJson() {
      return garminSQLiteRepo.saveAllTablesAsJson();
   }
}
