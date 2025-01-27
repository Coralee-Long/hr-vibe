package com.backend.repos;

import com.backend.config.GarminDatabaseConfig;
import org.springframework.stereotype.Repository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

   public GarminSQLiteRepo(GarminDatabaseConfig garminDbConfig) {
      this.garminDbConfig = garminDbConfig;
   }

   // Fetch data from a specific table
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

   // Save table as JSON
   public void saveTableAsJson(String tableName) {
      List<Map<String, Object>> tableData = fetchTableData(tableName);

      // Convert to JSON
      ObjectMapper objectMapper = new ObjectMapper();
      try {
         String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(tableData);

         // Save to a file
         File file = new File(tableName + ".json");
         try (FileWriter writer = new FileWriter(file)) {
            writer.write(json);
            System.out.println("Table " + tableName + " saved as JSON to " + file.getAbsolutePath());
         }
      } catch (IOException e) {
         throw new RuntimeException("Error saving table as JSON: " + e.getMessage(), e);
      }
   }
}
