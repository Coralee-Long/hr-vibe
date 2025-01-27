package com.backend.repos;

import com.backend.config.GarminDatabaseConfig;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

@Repository
public class GarminSQLiteRepo {

   private final GarminDatabaseConfig garminDbConfig;

   public GarminSQLiteRepo(GarminDatabaseConfig garminDbConfig) {
      this.garminDbConfig = garminDbConfig;
   }

   public void getAllDataFromTable(String tableName) {
      try (Connection connection = garminDbConfig.getConnection();
           Statement stmt = connection.createStatement()) {

         String query = "SELECT * FROM " + tableName + " LIMIT 10";
         ResultSet rs = stmt.executeQuery(query);

         while (rs.next()) {
            System.out.println(rs.getString(1)); // Replace with actual column handling
         }

      } catch (Exception e) {
         e.printStackTrace();
      }
   }
}
