package com.backend.services;

import com.backend.exceptions.GarminDatabaseException;
import com.backend.exceptions.GarminExportException;
import com.backend.repos.SQL.GarminSQLiteRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Service
public class GarminDataExportService {

   private static final Logger logger = LoggerFactory.getLogger(GarminDataExportService.class);
   private final GarminSQLiteRepo garminSQLiteRepo;
   private static final String EXPORT_DIR = System.getProperty("user.dir") + "/backend/data/raw_garmin_data/";

   private final ObjectMapper objectMapper; // Injected instead of creating a new one

   public GarminDataExportService(GarminSQLiteRepo garminSQLiteRepo, ObjectMapper objectMapper) {
      this.garminSQLiteRepo = garminSQLiteRepo;
      this.objectMapper = objectMapper; // uses the injected mock
   }

   /**
    * ✅ Retrieves all table names from SQLite.
    * Throws a `GarminDatabaseException` if the database query fails.
    */
   public List<String> getAllTableNames(String databaseName) {
      try {
         return garminSQLiteRepo.getAllTableNames(databaseName);
      } catch (Exception e) {
         logger.error("❌ Error retrieving table names for database {}: {}", databaseName, e.getMessage());
         throw new GarminDatabaseException("Failed to fetch table names for " + databaseName, e);
      }
   }

   /**
    * ✅ Saves a specific table as a JSON file.
    * Throws a `GarminExportException` if the process fails.
    */
   public void saveTableAsJson(String databaseName, String tableName) {
      logger.info("🔄 Exporting table '{}' from database '{}' to JSON...", tableName, databaseName);
      List<Map<String, Object>> tableData;

      try {
         tableData = garminSQLiteRepo.fetchTableData(databaseName, tableName);
      } catch (Exception e) {
         logger.error("❌ Failed to fetch data from table '{}': {}", tableName, e.getMessage());
         throw new GarminDatabaseException("Failed to retrieve data for table: " + tableName, e);
      }

      if (tableData.isEmpty()) {
         logger.warn("⚠️ No data found for table '{}'. Skipping export.", tableName);
         return;
      }

      try {
         Path databaseFolder = Paths.get(EXPORT_DIR, databaseName.replace(".db", ""));
         if (Files.notExists(databaseFolder)) {
            Files.createDirectories(databaseFolder);
         }

         File jsonFile = new File(databaseFolder.toFile(), tableName + ".json");

         objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, tableData);

         logger.info("✅ Successfully exported table '{}' to JSON.", tableName);
      } catch (IOException e) {
         logger.error("❌ Error saving table '{}' as JSON: {}", tableName, e.getMessage());
         throw new GarminExportException("Error saving table as JSON: " + tableName, e);
      }
   }

   /**
    * ✅ Saves all tables from SQLite as JSON files.
    * Logs each step and throws a `GarminExportException` if exporting fails.
    */
   public List<String> saveAllTablesAsJson(String databaseName) {
      logger.info("🔄 Exporting all tables from database '{}' to JSON...", databaseName);
      List<String> tableNames;

      try {
         tableNames = garminSQLiteRepo.getAllTableNames(databaseName);
      } catch (Exception e) {
         logger.error("❌ Error fetching table names for '{}': {}", databaseName, e.getMessage());
         throw new GarminDatabaseException("Failed to retrieve table names for " + databaseName, e);
      }

      if (tableNames.isEmpty()) {
         logger.warn("⚠️ No tables found in database '{}'. Nothing to export.", databaseName);
         return List.of();
      }

      for (String tableName : tableNames) {
         try {
            saveTableAsJson(databaseName, tableName);
         } catch (GarminExportException e) {
            logger.error("❌ Skipping export for table '{}': {}", tableName, e.getMessage());
         }
      }

      return tableNames;
   }
}
