package com.backend.controllers;

import com.backend.services.GarminDataExportService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 📌 Test Summary for GarminSQLiteController Integration Tests

 * 1️⃣ Tests for GET /garmin-sqlite/table-names:
 *    - ✅ givenValidDatabaseName_whenGetTableNames_thenReturnsListOfTables
 *    - ❌ givenMissingDatabaseName_whenGetTableNames_thenReturnsBadRequest

 * 2️⃣ Tests for POST /garmin-sqlite/export-table-as-json:
 *    - ✅ givenValidRequest_whenExportTableAsJson_thenReturnsSuccessMessage
 *    - ✅ givenMissingTableName_whenExportTableAsJson_thenReturnsSuccessMessageWithNullTable
 *    - ❌ givenMalformedJson_whenExportTableAsJson_thenReturnsBadRequest

 * 3️⃣ Tests for POST /garmin-sqlite/export-all-tables-as-json:
 *    - ✅ givenValidDatabaseName_whenExportAllTablesAsJson_thenReturnsSuccessMessageAndTables
 */

@ExtendWith(MockitoExtension.class)
public class GarminSQLiteControllerTest {

   private MockMvc mockMvc;

   @Mock
   private GarminDataExportService garminDataExportService;

   @InjectMocks
   private GarminSQLiteController garminSQLiteController;

   @BeforeEach
   void setUp() {
      // Build MockMvc using standaloneSetup with the controller under test.
      mockMvc = MockMvcBuilders.standaloneSetup(garminSQLiteController).build();
   }

   // 1. --------------------------- GET /garmin-sqlite/table-names --------------------------- //

   /**
    * ✅ Test Case: givenValidDatabaseName_whenGetTableNames_thenReturnsListOfTables
    */
   @Test
   void givenValidDatabaseName_whenGetTableNames_thenReturnsListOfTables() throws Exception {
      String databaseName = "testDB";
      List<String> tables = List.of("table1", "table2", "table3");
      when(garminDataExportService.getAllTableNames(databaseName)).thenReturn(tables);

      mockMvc.perform(get("/garmin-sqlite/table-names")
                          .param("databaseName", databaseName))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.tables").isArray())
          .andExpect(jsonPath("$.tables.length()").value(tables.size()))
          .andExpect(jsonPath("$.tables[0]").value("table1"));
   }

   /**
    * ❌ Test Case: givenMissingDatabaseName_whenGetTableNames_thenReturnsBadRequest
    */
   @Test
   void givenMissingDatabaseName_whenGetTableNames_thenReturnsBadRequest() throws Exception {
      // Missing required parameter "databaseName"
      mockMvc.perform(get("/garmin-sqlite/table-names"))
          .andExpect(status().isBadRequest());
   }

   // 2. --------------------------- POST /garmin-sqlite/export-table-as-json --------------------------- //

   /**
    * ✅ Test Case: givenValidRequest_whenExportTableAsJson_thenReturnsSuccessMessage
    */
   @Test
   void givenValidRequest_whenExportTableAsJson_thenReturnsSuccessMessage() throws Exception {
      String databaseName = "testDB";
      String tableName = "table1";
      doNothing().when(garminDataExportService).saveTableAsJson(databaseName, tableName);

      String requestBody = "{\"databaseName\":\"" + databaseName + "\", \"tableName\":\"" + tableName + "\"}";

      mockMvc.perform(post("/garmin-sqlite/export-table-as-json")
                          .contentType(MediaType.APPLICATION_JSON)
                          .content(requestBody))
          .andExpect(status().isOk())
          .andExpect(content().string("Table '" + tableName + "' has been exported as JSON."));
   }

   /**
    * ✅ Test Case: givenMissingTableName_whenExportTableAsJson_thenReturnsSuccessMessageWithNullTable
    */
   @Test
   void givenMissingTableName_whenExportTableAsJson_thenReturnsSuccessMessageWithNullTable() throws Exception {
      String databaseName = "testDB";
      // Here, the request JSON is missing the "tableName" key, so tableName will be null.
      String requestBody = "{\"databaseName\":\"" + databaseName + "\"}";
      doNothing().when(garminDataExportService).saveTableAsJson(databaseName, null);

      mockMvc.perform(post("/garmin-sqlite/export-table-as-json")
                          .contentType(MediaType.APPLICATION_JSON)
                          .content(requestBody))
          .andExpect(status().isOk())
          .andExpect(content().string("Table 'null' has been exported as JSON."));
   }

   /**
    * ❌ Test Case: givenMalformedJson_whenExportTableAsJson_thenReturnsBadRequest
    */
   @Test
   void givenMalformedJson_whenExportTableAsJson_thenReturnsBadRequest() throws Exception {
      // Malformed JSON (not valid JSON)
      String malformedJson = "this is not valid json";

      mockMvc.perform(post("/garmin-sqlite/export-table-as-json")
                          .contentType(MediaType.APPLICATION_JSON)
                          .content(malformedJson))
          .andExpect(status().isBadRequest());
   }

   // 3. --------------------------- POST /garmin-sqlite/export-all-tables-as-json --------------------------- //

   /**
    * ✅ Test Case: givenValidDatabaseName_whenExportAllTablesAsJson_thenReturnsSuccessMessageAndTables
    */
   @Test
   void givenValidDatabaseName_whenExportAllTablesAsJson_thenReturnsSuccessMessageAndTables() throws Exception {
      String databaseName = "testDB";
      List<String> savedTables = List.of("table1", "table2");
      when(garminDataExportService.saveAllTablesAsJson(databaseName)).thenReturn(savedTables);

      mockMvc.perform(post("/garmin-sqlite/export-all-tables-as-json")
                          .param("databaseName", databaseName))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.message").value("All tables exported successfully"))
          .andExpect(jsonPath("$.tables").isArray())
          .andExpect(jsonPath("$.tables.length()").value(savedTables.size()))
          .andExpect(jsonPath("$.tables[0]").value("table1"));
   }
}
