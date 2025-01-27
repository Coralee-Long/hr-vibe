package com.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class GarminDatabaseConfig {

   @Value("${sqlite.summary.db.path}")
   private String databaseUrl;

   public Connection getConnection() {
      try {
         return DriverManager.getConnection("jdbc:sqlite:" + databaseUrl);
      } catch (SQLException e) {
         throw new RuntimeException("Failed to connect to the Garmin SQLite database: " + e.getMessage(), e);
      }
   }
}
