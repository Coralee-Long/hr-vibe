package com.backend.enums;

public enum ValidTable {
   ATTRIBUTES("_attributes"),
   SUMMARY(    "summary"),
   YEARS_SUMMARY(    "years_summary"),
   MONTHS_SUMMARY(    "months_summary"),
   WEEKS_SUMMARY(    "weeks_summary"),
   DAYS_SUMMARY(    "days_summary"),
   INTENSITY_HR(    "intensity_hr");

   private final String tableName;

   ValidTable(String tableName) {
      this.tableName = tableName;
   }

   public String getTableName() {
      return tableName;
   }
}