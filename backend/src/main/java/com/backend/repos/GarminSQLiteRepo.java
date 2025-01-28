package com.backend.repos;

import com.backend.config.GarminDatabaseConfig;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GarminSQLiteRepo {

   private final GarminDatabaseConfig garminDbConfig;

   // 🔥 Define the directory where JSON files will be stored
   private static final String EXPORT_DIR = System.getProperty("user.dir") + "/backend/data/raw_garmin_data/";

   public GarminSQLiteRepo(GarminDatabaseConfig garminDbConfig) {
      this.garminDbConfig = garminDbConfig;
   }

   /**
    * ✅ Fetches all table names from the SQLite database.
    * Ignores internal SQLite tables.
    */
   public List<String> getAllTableNames() {
      List<String> tables = new ArrayList<>();
      try (Connection connection = garminDbConfig.getConnection()) {
         Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%'");

         while (rs.next()) {
            tables.add(rs.getString("name"));
         }
      } catch (SQLException e) {
         throw new RuntimeException("Error retrieving table names: " + e.getMessage(), e);
      }
      return tables;
   }

   /**
    * ✅ Saves all tables as JSON files in the export directory.
    */
   public List<String> saveAllTablesAsJson() {
      List<String> tableNames = getAllTableNames();
      List<String> savedTables = new ArrayList<>();

      for (String tableName : tableNames) {
         try {
            saveTableAsJson(tableName);
            savedTables.add(tableName);
         } catch (Exception e) {
            System.err.println("Failed to save table: " + tableName + ". Error: " + e.getMessage());
         }
      }
      return savedTables;
   }

   /**
    * ✅ Fetches all data from a specified table.
    */
   public List<Map<String, Object>> fetchTableData(String tableName) {
      List<Map<String, Object>> result = new ArrayList<>();
      try (Connection connection = garminDbConfig.getConnection()) {
         Statement stmt = connection.createStatement();
         String query = "SELECT * FROM " + tableName;
         ResultSet rs = stmt.executeQuery(query);

         int columnCount = rs.getMetaData().getColumnCount();
         while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
               row.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
            }
            result.add(row);
         }
      } catch (SQLException e) {
         throw new RuntimeException("Error querying table: " + e.getMessage(), e);
      }
      return result;
   }

   /**
    * ✅ Saves a specific table's data as a JSON file in the backend.
    */
   public void saveTableAsJson(String tableName) {
      List<Map<String, Object>> tableData = fetchTableData(tableName);

      if (tableData.isEmpty()) {
         System.out.println("No data found for table: " + tableName);
         return;
      }

      try {
         // Ensure the export directory exists
         Path directoryPath = Paths.get(EXPORT_DIR);
         if (Files.notExists(directoryPath)) {
            Files.createDirectories(directoryPath);
         }

         // Define the file path inside backend/data/raw_garmin_data/
         File jsonFile = new File(EXPORT_DIR + tableName + ".json");

         // Convert data to JSON and save
         ObjectMapper objectMapper = new ObjectMapper();
         objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, tableData);

         System.out.println("Table " + tableName + " saved as JSON to " + jsonFile.getAbsolutePath());
      } catch (IOException e) {
         throw new RuntimeException("Error saving table as JSON: " + e.getMessage(), e);
      }
   }
}
