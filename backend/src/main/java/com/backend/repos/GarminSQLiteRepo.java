package com.backend.repos;

import com.backend.config.GarminDatabaseConfig;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class GarminSQLiteRepo {

   private final GarminDatabaseConfig garminDbConfig;

   public GarminSQLiteRepo(GarminDatabaseConfig garminDbConfig) {
      this.garminDbConfig = garminDbConfig;
   }

   public void printTableData(String tableName) {
      try (Connection connection = garminDbConfig.getConnection()) {
         Statement stmt = connection.createStatement();
         String query = "SELECT * FROM " + tableName + " LIMIT 10"; // Limiting to 10 rows for clarity
         ResultSet rs = stmt.executeQuery(query);

         System.out.println("Data from table: " + tableName);
         int columnCount = rs.getMetaData().getColumnCount();

         // Print column headers
         for (int i = 1; i <= columnCount; i++) {
            System.out.print(rs.getMetaData().getColumnName(i) + "\t");
         }
         System.out.println();

         // Print rows
         while (rs.next()) {
            for (int i = 1; i <= columnCount; i++) {
               System.out.print(rs.getString(i) + "\t");
            }
            System.out.println();
         }

      } catch (SQLException e) {
         System.err.println("Error querying table: " + e.getMessage());
      }
   }
}
