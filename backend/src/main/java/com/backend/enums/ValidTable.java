package com.backend.enums;

public enum ValidTable {
   // Existing constants
   ATTRIBUTES("_attributes"),
   SUMMARY("summary"),
   YEARS_SUMMARY("years_summary"),
   MONTHS_SUMMARY("months_summary"),
   WEEKS_SUMMARY("weeks_summary"),
   DAYS_SUMMARY("days_summary"),
   INTENSITY_HR("intensity_hr"),

   // Extra table names from the second JSON
   ATTRIBUTES_RAW("attributes"),
   DEVICES("devices"),
   WEIGHT("weight"),
   STRESS("stress"),
   SLEEP("sleep"),
   SLEEP_EVENTS("sleep_events"),
   RESTING_HR("resting_hr"),
   DAILY_SUMMARY("daily_summary"),
   FILES("files"),
   DEVICE_INFO("device_info"),

   // Extra table names from the third JSON
   MONITORING_INFO("monitoring_info"),
   MONITORING_HR("monitoring_hr"),
   MONITORING_INTENSITY("monitoring_intensity"),
   MONITORING_CLIMB("monitoring_climb"),
   MONITORING("monitoring"),
   MONITORING_RR("monitoring_rr"),
   MONITORING_PULSE_OX("monitoring_pulse_ox");

   private final String tableName;

   ValidTable(String tableName) {
      this.tableName = tableName;
   }

   public String getTableName() {
      return tableName;
   }

   public static ValidTable fromString(String tableName) {
      for (ValidTable vt : ValidTable.values()) {
         if (vt.getTableName().equalsIgnoreCase(tableName)) {
            return vt;
         }
      }
      throw new IllegalArgumentException("No enum constant for table name: " + tableName);
   }
}
