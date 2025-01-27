package com.backend.services;

import com.backend.repos.GarminSQLiteRepo;
import org.springframework.stereotype.Service;

@Service
public class GarminDataService {

   private final GarminSQLiteRepo garminSQLiteRepo;

   public GarminDataService(GarminSQLiteRepo garminSQLiteRepo) {
      this.garminSQLiteRepo = garminSQLiteRepo;
   }

   public void fetchAndPrintData(String tableName) {
      System.out.println("Fetching data from table: " + tableName);
      garminSQLiteRepo.getAllDataFromTable(tableName);
   }
}
