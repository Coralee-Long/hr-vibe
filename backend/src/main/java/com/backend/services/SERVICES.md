# Services Overview

## **1️⃣ GarminService** (`/garmin`)

📂 **File:** `GarminService.java`  
🔹 **Purpose:** Handles data processing and saving SQLite summaries into MongoDB.

### **📌 Responsibilities**

- Fetches data from **SQLite** tables.
- Processes **daily, weekly, monthly, and yearly summaries**.
- Validates data before saving it to MongoDB.
- Aggregates the **last 7 days** into a `RecentDailySummaries` object.

### **📌 Methods**

| **Method**                                                                                                                               | **Function**                                                               |
| ---------------------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------- |
| `processAndSaveSummary(String databaseName, String tableName, Function<Map<String, Object>, T> mapper, MongoRepository<T, String> repo)` | Generic method to fetch, process, validate, and save summaries.            |
| `processAndSaveCurrentDaySummary(String db, String table)`                                                                               | Processes and saves the current day's summary.                             |
| `processAndSaveWeeklySummary(String db, String table)`                                                                                   | Processes and saves the weekly summary.                                    |
| `processAndSaveMonthlySummary(String db, String table)`                                                                                  | Processes and saves the monthly summary.                                   |
| `processAndSaveYearlySummary(String db, String table)`                                                                                   | Processes and saves the yearly summary.                                    |
| `processAndSaveRecentDailySummaries()`                                                                                                   | Aggregates the last **7 days** and saves them as a `RecentDailySummaries`. |

### **📌 How It Works**

1. **Fetches** data from SQLite via `GarminSQLiteRepo`.
2. **Processes** the raw data (calls `DataParsingUtils`).
3. **Validates** the summary (calls `ValidationService`).
4. **Saves** the processed data in the correct MongoDB collection.

---

## **2️⃣ GarminDataExportService** (`/garmin-sqlite`)

📂 **File:** `GarminDataExportService.java`  
🔹 **Purpose:** Exports raw SQLite data for debugging and analysis.

### **📌 Responsibilities**

- Fetches all table names from **SQLite**.
- Saves **individual** tables as JSON.
- Saves **all** SQLite tables as JSON.

### **📌 Methods**

| **Method**                                               | **Function**                              |
| -------------------------------------------------------- | ----------------------------------------- |
| `getAllTableNames(String databaseName)`                  | Retrieves a list of all tables in SQLite. |
| `saveTableAsJson(String databaseName, String tableName)` | Exports a specific SQLite table as JSON.  |
| `saveAllTablesAsJson(String databaseName)`               | Exports all SQLite tables as JSON.        |

### **📌 How It Works**

1. **Retrieves data** from SQLite using `GarminSQLiteRepo`.
2. **Converts** the data into a structured JSON file.
3. **Saves** the exported JSON files into `backend/data/raw_garmin_data/`.

---

## **3️⃣ ValidationService**

📂 **File:** `ValidationService.java`  
🔹 **Purpose:** Ensures data integrity before saving to MongoDB.

### **📌 Responsibilities**

- Uses **Java Bean Validation** to check DTOs.
- Throws errors when validation **fails**.

### **📌 Methods**

| **Method**                | **Function**                                     |
| ------------------------- | ------------------------------------------------ |
| `validate(@Valid T data)` | Validates any model using **Jakarta Validator**. |

---

# **Utils Overview (UTILS.md)**

## **1️⃣ DataParsingUtils**

📂 **File:** `DataParsingUtils.java`  
🔹 **Purpose:** Converts raw SQLite data into **strongly-typed objects**.

### **📌 Responsibilities**

- Parses and cleans raw **SQLite data**.
- Converts **Map<String, Object> → Java Models**.
- Handles **data type conversion (String → Integer, Double, LocalDate, etc.)**.

### **📌 Methods**

| **Method**                                                     | **Function**                                                    |
| -------------------------------------------------------------- | --------------------------------------------------------------- |
| `cleanTimeFormat(String time)`                                 | Fixes time formatting issues.                                   |
| `getString(Map<String, Object> data, String key)`              | Retrieves a **String** value safely.                            |
| `getInteger(Map<String, Object> data, String key)`             | Converts values into an **Integer**.                            |
| `getDouble(Map<String, Object> data, String key)`              | Converts values into a **Double**.                              |
| `roundDoubleToInteger(Double value)`                           | Rounds a **Double** to the nearest Integer.                     |
| `getNumber(Map<String, Object> data, String key)`              | Retrieves any **numeric value** safely.                         |
| `mapToBaseSummary(Map<String, Object> data)`                   | Converts raw data into a **BaseSummary** model.                 |
| `mapToCurrentDaySummary(Map<String, Object> data)`             | Converts data to a **CurrentDaySummary** model.                 |
| `mapToWeeklySummary(Map<String, Object> data)`                 | Converts data to a **WeeklySummary** model.                     |
| `mapToMonthlySummary(Map<String, Object> data)`                | Converts data to a **MonthlySummary** model.                    |
| `mapToYearlySummary(Map<String, Object> data)`                 | Converts data to a **YearlySummary** model.                     |
| `mapToRecentDailySummaries(List<CurrentDaySummary> summaries)` | Aggregates **last 7 days** into a `RecentDailySummaries` model. |

### **📌 How It Works**

1. **Receives raw SQLite data** as `Map<String, Object>`.
2. **Converts each field** to the correct Java type.
3. **Creates** a Java model (e.g., `CurrentDaySummary`).
4. **Returns the transformed object** for saving in MongoDB.

---

## **2️⃣ SummaryUtils**

📂 **File:** `SummaryUtils.java`  
🔹 **Purpose:** Finds and extracts the latest **date-based** records.

### **📌 Methods**

| **Method**                                                            | **Function**                                       |
| --------------------------------------------------------------------- | -------------------------------------------------- |
| `getLatestData(List<Map<String, Object>> rawData, String dateColumn)` | Finds the most recent entry based on a date field. |

### **📌 How It Works**

1. **Filters out invalid dates**.
2. **Sorts records by date**.
3. **Returns the latest record** for processing.

---

## **3️⃣ ValidationPatterns**

📂 **File:** `ValidationPatterns.java`  
🔹 **Purpose:** Stores reusable **validation regex patterns**.

### **📌 Patterns**

| **Pattern**    | **Description**                              |
| -------------- | -------------------------------------------- |
| `TIME_PATTERN` | Regex for validating `HH:mm:ss` time format. |

---
