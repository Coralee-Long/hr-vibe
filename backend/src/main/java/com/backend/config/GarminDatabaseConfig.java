package com.backend.config;

import org.springframework.context.annotation.Configuration;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
public class GarminDatabaseConfig {

   private static final String BASE_PATH = "/Users/coralee/Projects/GarminDB/GarminData/DBs/";

   public Connection getConnection(String databaseName) {
      try {
         String databaseUrl = BASE_PATH + databaseName;
         Connection connection = DriverManager.getConnection("jdbc:sqlite:" + databaseUrl);

         // Debug log: List tables in DB
         Statement stmt = connection.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table';");

         System.out.println("Tables in database " + databaseName + ":");
         while (rs.next()) {
            System.out.println(rs.getString("name"));
         }

         rs.close();
         stmt.close();
         return connection;
      } catch (SQLException e) {
         throw new RuntimeException("Failed to connect to SQLite DB: " + e.getMessage(), e);
      }
   }
}
