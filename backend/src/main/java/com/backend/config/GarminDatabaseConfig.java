package com.backend.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.context.annotation.Configuration;


@Configuration
public class GarminDatabaseConfig {

   private final String databaseUrl = "/Users/coralee/Projects/GarminDB/GarminData/DBs/garmin_summary.db";

   public Connection getConnection() {
      try {
         Connection connection = DriverManager.getConnection("jdbc:sqlite:" + databaseUrl);

         // Debug query to test connection
         Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table';");

         System.out.println("Tables in the database:");
         while (rs.next()) {
            System.out.println(rs.getString("name"));
         }

         rs.close();
         stmt.close();
         return connection;
      } catch (SQLException e) {
         throw new RuntimeException("Failed to connect to the Garmin SQLite database: " + e.getMessage(), e);
      }
   }
}
