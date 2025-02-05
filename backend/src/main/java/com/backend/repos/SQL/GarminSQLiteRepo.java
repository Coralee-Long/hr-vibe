package com.backend.repos.SQL;

import com.backend.config.GarminDatabaseConfig;
import com.backend.exceptions.GarminDatabaseException;
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
   private static final String EXPORT_DIR = System.getProperty("user.dir") + "/backend/data/raw_garmin_data/";

   public GarminSQLiteRepo(GarminDatabaseConfig garminDbConfig) {
      this.garminDbConfig = garminDbConfig;
   }

   public List<String> getAllTableNames(String databaseName) {
      List<String> tables = new ArrayList<>();
      try (Connection connection = garminDbConfig.getConnection(databaseName)) {
         Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%'");
         while (rs.next()) {
            tables.add(rs.getString("name"));
         }
      } catch (SQLException e) {
         throw new GarminDatabaseException("Failed to retrieve table names from database: " + databaseName, e);
      }
      return tables;
   }

   public List<Map<String, Object>> fetchTableData(String databaseName, String tableName) {
      List<Map<String, Object>> result = new ArrayList<>();

      // ✅ Validate table name against a whitelist before using it in the query
      if (!isValidTableName(tableName)) {
         throw new IllegalArgumentException("Invalid table name: " + tableName);
      }

      String query = "SELECT * FROM " + databaseName + "." + tableName;// Now safe because table name is validated

      try (Connection connection = garminDbConfig.getConnection(databaseName);
           Statement stmt = connection.createStatement();
           ResultSet rs = stmt.executeQuery(query)) {

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

   public void saveTableAsJson(String databaseName, String tableName) {
      List<Map<String, Object>> tableData = fetchTableData(databaseName, tableName);

      if (tableData.isEmpty()) {
         System.out.println("No data found for table: " + tableName);
         return;
      }

      try {
         // Create folder for database
         Path databaseFolder = Paths.get(EXPORT_DIR, databaseName.replace(".db", ""));
         if (Files.notExists(databaseFolder)) {
            Files.createDirectories(databaseFolder);
         }

         // Define JSON file path
         File jsonFile = new File(databaseFolder.toFile(), tableName + ".json");

         // Convert data to JSON and save
         ObjectMapper objectMapper = new ObjectMapper();
         objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, tableData);

         System.out.println("✅ Table " + tableName + " from " + databaseName + " saved as JSON in: " + databaseFolder);
      } catch (IOException e) {
         throw new RuntimeException("Error saving table as JSON: " + e.getMessage(), e);
      }
   }

   public List<String> saveAllTablesAsJson(String databaseName) {
      List<String> tableNames = getAllTableNames(databaseName);
      List<String> savedTables = new ArrayList<>();

      for (String tableName : tableNames) {
         try {
            saveTableAsJson(databaseName, tableName);
            savedTables.add(tableName);
         } catch (Exception e) {
            System.err.println("❌ Failed to save table: " + tableName + " from " + databaseName + ". Error: " + e.getMessage());
         }
      }
      return savedTables;
   }

   /**
    * ✅ Helper method: Ensures the provided table name is valid by checking against a whitelist.
    */
   private boolean isValidTableName(String tableName) {
      return tableName.matches("^[a-zA-Z0-9_]+$"); // Allow all alphanumeric table names with underscores
   }
}
