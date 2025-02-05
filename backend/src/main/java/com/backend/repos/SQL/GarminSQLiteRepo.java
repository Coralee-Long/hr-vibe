package com.backend.repos.SQL;

import com.backend.config.GarminDatabaseConfig;
import com.backend.exceptions.GarminDatabaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

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

   private static final Logger logger = LoggerFactory.getLogger(GarminSQLiteRepo.class);
   private final GarminDatabaseConfig garminDbConfig;

   public GarminSQLiteRepo(GarminDatabaseConfig garminDbConfig) {
      this.garminDbConfig = garminDbConfig;
   }

   /**
    * Retrieves all table names from the given SQLite database.
    * Logs a warning if the database is empty.
    */
   public List<String> getAllTableNames(String databaseName) {
      List<String> tables = new ArrayList<>();
      try (Connection connection = garminDbConfig.getConnection(databaseName);
           Statement stmt = connection.createStatement();
           ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name NOT LIKE 'sqlite_%'")) {

         while (rs.next()) {
            String table = rs.getString("name").trim(); // Ensure no spaces
            tables.add(table);
            logger.info("✅ Found table: '{}'", table);
         }
      } catch (SQLException e) {
         throw new GarminDatabaseException("❌ Failed to retrieve table names from database: " + databaseName, e);
      }

      logger.info("📌 Final list of tables: {}", tables);
      return tables;
   }

   /**
    * Fetches all rows from a specified SQLite table.
    * Ensures table name is valid and returns an empty list if no data is found.
    */
   public List<Map<String, Object>> fetchTableData(String databaseName, String tableName) {
      List<Map<String, Object>> result = new ArrayList<>();

      // ✅ Validate the table name before querying
      if (!isValidTableName(tableName)) {
         throw new IllegalArgumentException("❌ Invalid table name: " + tableName);
      }

      String query = "SELECT * FROM " + tableName;

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
         throw new GarminDatabaseException("❌ Error querying table '" + tableName + "' in database '" + databaseName + "': " + e.getMessage(), e);
      }

      if (result.isEmpty()) {
         logger.warn("⚠️ No data found in table '{}' from database '{}'", tableName, databaseName);
      } else {
         logger.info("✅ Retrieved {} rows from table '{}' in database '{}'", result.size(), tableName, databaseName);
      }

      return result;
   }

   /**
    * Ensures the provided table name is valid (only allows alphanumeric and underscores).
    */
   private boolean isValidTableName(String tableName) {
      tableName = tableName.trim(); // 🔹 Ensure no leading/trailing spaces
      logger.info("🔍 Checking trimmed table name: '{}'", tableName);

      boolean isValid = tableName.matches("^[a-zA-Z0-9_]+$");

      if (!isValid) {
         logger.error("❌ Invalid table name attempted: '{}'", tableName);
      } else {
         logger.info("✅ Valid table name: '{}'", tableName);
      }

      return isValid;
   }
}
