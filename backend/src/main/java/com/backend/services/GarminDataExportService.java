package com.backend.services;

import com.backend.enums.ValidTable;
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

/**
 * GarminDataExportService is responsible for exporting SQLite table data to JSON files.
 * <p>
 * It retrieves table data from a GarminSQLiteRepo, converts the table name using the ValidTable enum
 * to ensure only valid table names are used (helping prevent SQL injection), and then writes the data
 * to a JSON file in the export directory.
 * <p>
 * The export directory is defined by the EXPORT_DIR constant. In this updated version, the directory
 * is set to: {user.dir}/backend/data/raw_garmin_data/ so that it matches your test expectations.
 */
@Service
public class GarminDataExportService {

   private static final Logger logger = LoggerFactory.getLogger(GarminDataExportService.class);

   // Updated EXPORT_DIR to match the folder structure expected in tests.
   private static final String EXPORT_DIR = System.getProperty("user.dir") + "/backend/data/raw_garmin_data/";

   private final GarminSQLiteRepo garminSQLiteRepo;
   private final ObjectMapper objectMapper; // Injected instead of creating a new one

   public GarminDataExportService(GarminSQLiteRepo garminSQLiteRepo, ObjectMapper objectMapper) {
      this.garminSQLiteRepo = garminSQLiteRepo;
      this.objectMapper = objectMapper; // uses the injected instance (or a mock in tests)
   }

   /**
    * Retrieves all table names from SQLite.
    * Throws a {@code GarminDatabaseException} if the database query fails.
    *
    * @param databaseName the name of the SQLite database
    * @return a list of table names
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
    * Saves a specific table as a JSON file.
    * <p>
    * The method first converts the provided table name to a {@code ValidTable} using the
    * {@code fromString} method. It then fetches the table data, and if data exists,
    * writes it to a JSON file. The file is named using the enum’s internal table name (lowercase)
    * to ensure consistency.
    * <p>
    * Throws a {@code GarminExportException} if file writing fails.
    *
    * @param databaseName the name of the SQLite database
    * @param tableName    the table name (as a string) to export
    */
   public void saveTableAsJson(String databaseName, String tableName) {
      logger.info("🔄 Exporting table '{}' from database '{}' to JSON...", tableName, databaseName);
      List<Map<String, Object>> tableData;

      try {
         // Convert the table name string to a ValidTable enum using the custom converter method.
         tableData = garminSQLiteRepo.fetchTableData(databaseName, ValidTable.fromString(tableName));
      } catch (Exception e) {
         logger.error("❌ Failed to fetch data from table '{}': {}", tableName, e.getMessage());
         throw new GarminDatabaseException("Failed to retrieve data for table: " + tableName, e);
      }

      if (tableData.isEmpty()) {
         logger.warn("⚠️ No data found for table '{}'. Skipping export.", tableName);
         return;
      }

      try {
         // Convert the provided tableName string to its corresponding enum.
         ValidTable vt = ValidTable.fromString(tableName);
         // Build the target directory path (e.g., {EXPORT_DIR}/testDB/)
         Path databaseFolder = Paths.get(EXPORT_DIR, databaseName.replace(".db", ""));
         if (Files.notExists(databaseFolder)) {
            Files.createDirectories(databaseFolder);
         }
         // Use the enum's tableName (e.g., "days_summary") for the file name.
         String fileName = vt.getTableName() + ".json";
         File jsonFile = new File(databaseFolder.toFile(), fileName);

         objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, tableData);

         logger.info("✅ Successfully exported table '{}' to JSON.", tableName);
      } catch (IOException e) {
         logger.error("❌ Error saving table '{}' as JSON: {}", tableName, e.getMessage());
         throw new GarminExportException("Error saving table as JSON: " + tableName, e);
      }
   }

   /**
    * Saves all tables from SQLite as JSON files.
    * <p>
    * The method retrieves all table names from the database and then attempts to export each table.
    * If a particular table export fails, it logs the error and continues with the next table.
    *
    * @param databaseName the name of the SQLite database
    * @return a list of table names that were attempted to be exported
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

      // For each table, attempt to export its data to JSON.
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
