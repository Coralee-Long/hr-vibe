package com.backend.services;

import com.backend.repos.GarminSQLiteRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GarminSQLiteService {

   private final GarminSQLiteRepo garminSQLiteRepo;

   public GarminSQLiteService(GarminSQLiteRepo garminSQLiteRepo) {
      this.garminSQLiteRepo = garminSQLiteRepo;
   }

   public List<Map<String, Object>> fetchTableData (String tableName) {
      return garminSQLiteRepo.fetchTableData(tableName);
   }

   public void saveTableAsJson(String tableName) {
      garminSQLiteRepo.saveTableAsJson(tableName);
   }
}
