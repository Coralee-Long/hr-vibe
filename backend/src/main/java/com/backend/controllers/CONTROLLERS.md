# Controller Structures

1. **`GarminSQLiteController`** – Responsible for debugging and exporting SQLite data as JSON files.
2. **`GarminController`** – Responsible for processing SQLite data and saving it into MongoDB.
3. // TODO: controller with mongoDB endpoints

---

## **️1️⃣ GarminSQLiteController (`/garmin-sqlite`)**

📂 **File:** `GarminSQLiteController.java`  
🔹 **Purpose:** Provides **debugging tools** for fetching SQLite table names and exporting tables as JSON files.

### **📌 Endpoints**

| **Method** | **Endpoint**                               | **Function**                                           |
| ---------- | ------------------------------------------ | ------------------------------------------------------ |
| `GET`      | `/garmin-sqlite/table-names`               | Retrieves a list of all tables in the SQLite database. |
| `POST`     | `/garmin-sqlite/export-table-as-json`      | Exports a **single** table as a JSON file.             |
| `POST`     | `/garmin-sqlite/export-all-tables-as-json` | Exports **all** tables as JSON files.                  |

### **📌 How it Works**

1. The controller **receives requests** to retrieve table names or export data.
2. It **calls `GarminDataExportService`**, which interacts with SQLite and saves JSON files.
3. A confirmation message is returned once the export is complete.

---

## **2️⃣ GarminController (`/garmin`)**

📂 **File:** `GarminController.java`  
🔹 **Purpose:** Processes and saves SQLite data into MongoDB.

### **📌 Endpoints**

| **Method** | **Endpoint**                             | **Function**                                    |
| ---------- | ---------------------------------------- | ----------------------------------------------- |
| `POST`     | `/garmin/process/current-day-summary`    | Processes & saves the current day's summary.    |
| `POST`     | `/garmin/process/weekly-summary`         | Processes & saves the weekly summary.           |
| `POST`     | `/garmin/process/monthly-summary`        | Processes & saves the monthly summary.          |
| `POST`     | `/garmin/process/yearly-summary`         | Processes & saves the yearly summary.           |
| `POST`     | `/garmin/process/recent-daily-summaries` | Processes & saves the last 7 days of summaries. |

### **📌 How it Works**

1. The controller **receives requests** to process SQLite data.
2. It **calls `GarminService`**, which fetches, processes, and saves data in MongoDB.
3. A confirmation message is returned once processing is complete.

---

## **📌 Why This Structure?**

✅ **Separation of concerns:**

- `GarminController` only processes and saves **data to MongoDB**.
- `GarminSQLiteController` only **fetches raw data** and saves JSON for debugging.

✅ **Clear Naming & RESTful Approach:**

- `/garmin` → Handles processing.
- `/garmin-sqlite` → Handles debugging.
- `/process/*` → Clearly indicates **data transformation and storage**.
- `/export/*` → Clearly indicates **saving data as JSON files**.

---
