package com.backend.services;

import com.backend.repos.GarminSQLiteRepo;
import org.springframework.stereotype.Service;

@Service
public class GarminSQLiteService {

   private final GarminSQLiteRepo garminSQLiteRepo;

   public GarminSQLiteService(GarminSQLiteRepo garminSQLiteRepo) {
      this.garminSQLiteRepo = garminSQLiteRepo;
   }

   public void printTableData(String tableName) {
      garminSQLiteRepo.printTableData(tableName);
   }
}
