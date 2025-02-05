package com.backend.services;

import com.backend.exceptions.GarminDatabaseException;
import com.backend.exceptions.GarminExportException;
import com.backend.repos.SQL.GarminSQLiteRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * ✅ GarminDataExportServiceTest - Unit tests for GarminDataExportService.

 * **Table of Contents:**

 * 1️⃣ **getAllTableNames(String databaseName)**
 *    - ✅ Returns a valid list of table names when the database contains tables.
 *    - ❌ Throws `GarminDatabaseException` when database query fails.

 * 2️⃣ **saveTableAsJson(String databaseName, String tableName)**
 *    - ✅ Successfully writes table data to a JSON file.
 *    - ❌ Logs a warning and skips file creation when table is empty.
 *    - ❌ Throws `GarminDatabaseException` if fetching data fails.
 *    - ❌ Throws `GarminExportException` if file writing fails.

 * 3️⃣ **saveAllTablesAsJson(String databaseName)**
 *    - ✅ Successfully exports multiple tables.
 *    - ❌ Logs a warning and returns an empty list when there are no tables.
 *    - ❌ Skips problematic tables and logs errors without interrupting execution.
 */

class GarminDataExportServiceTest {

   @Mock
   private GarminSQLiteRepo garminSQLiteRepo;

   @InjectMocks
   private GarminDataExportService garminDataExportService;

   @TempDir
   Path tempDir;

   @Mock
   private ObjectMapper objectMapper;

   @BeforeEach
   void setUp() {
      MockitoAnnotations.openMocks(this);

      // Ensure ObjectMapper mock returns a non-null writer
      when(objectMapper.writerWithDefaultPrettyPrinter()).thenReturn(new ObjectMapper().writerWithDefaultPrettyPrinter());
   }

   /**
    * ✅ Test `getAllTableNames()` returns a valid list of table names when database contains tables.
    */
   @Test
   void givenDatabaseWithTables_whenGetAllTableNames_thenReturnsList() {
      // GIVEN mock table names
      String databaseName = "mockDB";
      List<String> mockTables = List.of("daily_summary", "weekly_summary", "monthly_summary");

      when(garminSQLiteRepo.getAllTableNames(databaseName)).thenReturn(mockTables);

      // WHEN retrieving table names
      List<String> result = garminDataExportService.getAllTableNames(databaseName);

      // THEN verify the correct tables are returned
      assertNotNull(result);
      assertEquals(3, result.size());
      assertEquals("daily_summary", result.get(0));

      verify(garminSQLiteRepo).getAllTableNames(databaseName);
   }

   /**
    * ❌ Test `getAllTableNames()` when database query fails.
    */
   @Test
   void givenDatabaseError_whenGetAllTableNames_thenThrowsGarminDatabaseException() {
      String databaseName = "errorDB";

      when(garminSQLiteRepo.getAllTableNames(databaseName)).thenThrow(new RuntimeException("Mock database failure"));

      Exception exception = assertThrows(GarminDatabaseException.class,
                                         () -> garminDataExportService.getAllTableNames(databaseName));

      assertTrue(exception.getMessage().contains("Failed to fetch table names"));
   }

   /**
    * ✅ Test `saveTableAsJson()` successfully writes data to a JSON file.
    */
   @Test
   void givenValidTableData_whenSaveTableAsJson_thenCreatesJsonFile() throws IOException {
      // GIVEN mock SQLite data
      String databaseName = "testDB";
      String tableName = "daily_summary";
      List<Map<String, Object>> mockData = List.of(
          Map.of("day", "2025-01-30", "calories", 2500),
          Map.of("day", "2025-01-31", "calories", 2600)
                                                  );

      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(mockData);

      // WHEN exporting data to JSON
      garminDataExportService.saveTableAsJson(databaseName, tableName);

      // THEN verify the file was created correctly
      File jsonFile = new File(System.getProperty("user.dir") + "/backend/data/raw_garmin_data/testDB/daily_summary.json");

      // Ensure file exists before reading
      assertTrue(jsonFile.exists(), "JSON file was not created");

      // Do NOT use mocked objectMapper for reading → Use a real ObjectMapper
      ObjectMapper realObjectMapper = new ObjectMapper(); // Use a real instance
      List savedData = realObjectMapper.readValue(jsonFile, List.class);

      assertNotNull(savedData, "JSON file was read but returned null");
      assertFalse(savedData.isEmpty(), "JSON file was created but contains no data");
      assertEquals(2, savedData.size());

      verify(garminSQLiteRepo).fetchTableData(databaseName, tableName);
   }


   /**
    * ❌ Test `saveTableAsJson()` when table is empty.
    */
   @Test
   void givenEmptyTable_whenSaveTableAsJson_thenLogsWarningAndSkipsFileCreation() {
      // GIVEN empty table data
      String databaseName = "testDB";
      String tableName = "empty_table";
      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(List.of());

      // WHEN exporting data
      garminDataExportService.saveTableAsJson(databaseName, tableName);

      // THEN ensure no file is created
      File jsonFile = new File(System.getProperty("user.dir") + "/backend/data/raw_garmin_data/testDB/empty_table.json");
      assertFalse(jsonFile.exists());

      verify(garminSQLiteRepo).fetchTableData(databaseName, tableName);
   }

   /**
    * ❌ Test `saveTableAsJson()` throws GarminDatabaseException when data fetch fails.
    */
   @Test
   void givenDatabaseError_whenSaveTableAsJson_thenThrowsGarminDatabaseException() {
      String databaseName = "testDB";
      String tableName = "error_table";

      when(garminSQLiteRepo.fetchTableData(databaseName, tableName))
          .thenThrow(new RuntimeException("Mock database fetch failure"));

      Exception exception = assertThrows(GarminDatabaseException.class,
                                         () -> garminDataExportService.saveTableAsJson(databaseName, tableName));

      assertTrue(exception.getMessage().contains("Failed to retrieve data for table"));
   }

   /**
    * ❌ Test `saveTableAsJson()` throws GarminExportException when file writing fails.
    */
   @Test
   void givenIOException_whenSaveTableAsJson_thenThrowsGarminExportException() throws IOException {
      String databaseName = "testDB";
      String tableName = "daily_summary";
      List<Map<String, Object>> mockData = List.of(Map.of("day", "2025-01-30", "calories", 2500));

      System.out.println("Mocking IOException on objectMapper...");

      when(garminSQLiteRepo.fetchTableData(databaseName, tableName)).thenReturn(mockData);

      // ✅ Properly mock ObjectWriter
      ObjectWriter mockWriter = mock(ObjectWriter.class);
      when(objectMapper.writerWithDefaultPrettyPrinter()).thenReturn(mockWriter);

      // ✅ Now the IOException will be correctly thrown
      doThrow(new IOException("Mock IOException")).when(mockWriter).writeValue(any(File.class), any());

      // WHEN exporting data THEN exception should be thrown
      Exception exception = assertThrows(GarminExportException.class,
                                         () -> garminDataExportService.saveTableAsJson(databaseName, tableName));

      assertTrue(exception.getMessage().contains("Error saving table as JSON"));

      verify(garminSQLiteRepo).fetchTableData(databaseName, tableName);
      verify(objectMapper).writerWithDefaultPrettyPrinter();
      verify(mockWriter).writeValue(any(File.class), any());
   }

   /**
    * ✅ Test `saveAllTablesAsJson()` exports multiple tables.
    */
   @Test
   void givenMultipleTables_whenSaveAllTablesAsJson_thenExportsEachTable() {
      String databaseName = "testDB";
      List<String> mockTables = List.of("daily_summary", "weekly_summary");
      when(garminSQLiteRepo.getAllTableNames(databaseName)).thenReturn(mockTables);

      for (String table : mockTables) {
         when(garminSQLiteRepo.fetchTableData(databaseName, table))
             .thenReturn(List.of(Map.of("day", "2025-01-30", "metric", 100)));
      }

      List<String> result = garminDataExportService.saveAllTablesAsJson(databaseName);

      assertNotNull(result);
      assertEquals(2, result.size());

      for (String table : mockTables) {
         File jsonFile = new File(System.getProperty("user.dir") + "/backend/data/raw_garmin_data/testDB/" + table + ".json");
         assertTrue(jsonFile.exists());
      }

      verify(garminSQLiteRepo).getAllTableNames(databaseName);
   }
}
