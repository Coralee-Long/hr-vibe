## 🚀 **HRVibe is under active development. Stay tuned!** 🎯

This is the **backend** for HRVibe, built with **Spring Boot** and **MongoDB**.  
It processes **Garmin health data** by fetching it from SQLite, converting it into structured models, and storing it in MongoDB for easy retrieval.

---

## 📌 Features
- REST API for **retrieving, storing, and processing Garmin health stats**.
- Uses **MongoDB** for structured health data storage.
- Fetches data from **GarminDB SQLite files** (which are manually updated for now).
- Supports **daily, weekly, monthly, and yearly data summaries**.
- **Future automation planned** for GarminDB fetching & syncing.

---

## 🛠 **Tech Stack**
- **Java 23** (Spring Boot)
- **MongoDB** (for storing processed Garmin data)
- **SQLite** (temporary storage before syncing to MongoDB)
- **GitHub Actions** (CI/CD automation)
- **SonarCloud** (code quality analysis)
- **Maven** (dependency management)

---

## 🚀 **GitHub Actions Workflows**
This backend uses **GitHub Actions** for CI/CD.

### ✅ `backend-ci.yml` - Java Backend CI
- **Builds** the backend with Maven.
- **Caches dependencies** for faster builds.
- **Ensures the project compiles successfully**.

### ✅ `sonar-backend.yml` - SonarCloud Code Analysis
- **Runs SonarCloud** to check code quality.
- **Caches Sonar dependencies** for faster runs.
- **Ensures the backend meets code standards**.

---

## **📌 How Data is Fetched & Stored**
### **🔹 Overview of Data Flow**
1. **GarminDB (SQLite) Fetching:**
    - Manually run `download.py` from the **GarminDB** project.
    - This fetches **new Garmin data** and saves it as `.db` files.

2. **HRVibe Backend Processing:**
    - HRVibe reads **SQLite tables** (`days_summary`, `weeks_summary`, etc.).
    - Converts them into structured **MongoDB documents**.

3. **MongoDB Storage & API Access:**
    - Once in MongoDB, the data is **queried via REST endpoints**.

### **🔹 Database & Storage Structure**
📂 **Raw SQLite Data (Before Processing)**
```
GarminDB/GarminData/DBs/
├── garmin.db
├── garmin_summary.db
├── garmin_monitoring.db
```

📂 **Saved JSON Files for Debugging**
```
backend/data/raw_garmin_data/
├── garmin_summary.db/
│     ├── days_summary.json
│     ├── weeks_summary.json
│     ├── months_summary.json
│     ├── years_summary.json
├── garmin_monitoring.db/
│     ├── heart_rate.json
│     ├── intensity.json
```

📂 **Final MongoDB Collections**

| **Model Name** | **Purpose** | **Stored in MongoDB as** |
|---------------|------------|--------------------------|
| `CurrentDaySummary` | Latest available day’s data | `current_day_summaries` |
| `RecentDailySummaries` | Last 7 days of daily data (mini-graphs) | `recent_daily_summaries` |
| `WeeklySummary` | Aggregated weekly data | `weekly_summaries` |
| `MonthlySummary` | Aggregated monthly data | `monthly_summaries` |
| `YearlySummary` | Aggregated yearly data | `yearly_summaries` |

---

## **📌 API Endpoints**
### **🔹 Fetch GarminDB Table Names**
| Method | Endpoint | Description |
|--------|---------|-------------|
| **GET** | `/api/table-names` | Get all available table names from SQLite |
| **GET** | `/api/fetch-table?tableName={tableName}` | Fetch raw data from a specific SQLite table |
| **GET** | `/api/save-table?tableName={tableName}` | Save a specific SQLite table as a JSON file |
| **GET** | `/api/save-all-tables` | Save all SQLite tables as JSON files |

---

### **🔹 Fetch Processed Data from MongoDB**
Once SQLite data is **processed and stored in MongoDB**, use these endpoints:

| Method | Endpoint | Description |
|--------|---------|-------------|
| **GET** | `/api/daily-summary/latest` | Fetch latest available day summary |
| **GET** | `/api/daily-summary/weekly` | Fetch last 7 days of data (for mini-graphs) |
| **GET** | `/api/weekly-summary` | Fetch weekly aggregated data |
| **GET** | `/api/monthly-summary` | Fetch monthly aggregated data |
| **GET** | `/api/yearly-summary` | Fetch yearly aggregated data |

---

## **✅ Syncing SQLite Data to MongoDB**
Since automation is **not yet implemented**, we manually trigger syncing.

📌 **Step 1: Run the GarminDB Fetch Script**
```sh
    cd /path/to/GarminDB
    python3 download.py
```

📌 **Step 2: Run HRVibe Backend Sync**
```sh
    curl -X POST http://localhost:8080/api/sync-database
```

🔹 **Planned Future Automation**

| Task | Current Approach | Future Automation |
|------|-----------------|-------------------|
| **GarminDB Data Fetching** | Manual (`python3 download.py`) | Cron job / Java API trigger |
| **SQLite → MongoDB Sync** | Manual (`POST /api/sync-database`) | Scheduled job in Java |
| **Fetching Only New Data** | Full re-fetch | Modify Python to fetch incrementally |

---

## **📌 Notes**
- **Overwriting Behavior:** JSON files **will be replaced** when saving again.
- **MongoDB Data Persistence:** New data is **appended**, not overwritten.
- **Automation:** Planned for **post-MVP phase**.

---

## 🛠 **Future Improvements**
- **Automate GarminDB fetching** via cron job or Java API.
- **Schedule SQLite → MongoDB sync** to run nightly.
- **Optimize queries** to fetch **only new data** since last sync.

---



