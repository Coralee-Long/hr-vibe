# 🚀 HRVibe Backend

This is the **backend** for HRVibe, built with **Spring Boot** and **MongoDB**.

## 📌 Features
- REST API with endpoints for retrieving Garmin data.
- Fetches and stores **HRV, sleep, training load, and other health stats**.
- Uses **MongoDB** as the database.
- Built with **Java 23**.

---

## 🛠 **Tech Stack**
- **Java 23** (Spring Boot)
- **MongoDB** (for storing Garmin data)
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

## 🔧 **Setup & Run**
### 1️⃣ **Clone the repo**
```sh
git clone https://github.com/YOUR_GITHUB_USERNAME/hr-vibe.git
cd hr-vibe/backend
```

---

Here’s the **updated README** with a section listing the **endpoint for each table name** dynamically.

---

# HRVibe Backend API

This backend provides RESTful endpoints to fetch and store Garmin health data.

## 📂 Base URL
```
http://localhost:8080/api
```

---

## **🔹 Endpoints Overview**

| Method | Endpoint | Description |
|--------|---------|-------------|
| **GET** | `/api/table-names` | Get all available table names from the database |
| **GET** | `/api/fetch-table?tableName={tableName}` | Fetch data from a specific table |
| **GET** | `/api/save-table?tableName={tableName}` | Save a specific table as a JSON file |
| **GET** | `/api/save-all-tables` | Save all tables as JSON files |

📂 **Saved JSON files are stored in:**  
```
backend/data/raw_garmin_data/
```

---

## **🔹 Fetch Data for Each Table**
Use the following endpoints to fetch data for each available table:

| Table Name | Fetch Data Endpoint |
|------------|----------------------|
| `daily_summary` | `GET /api/fetch-table?tableName=daily_summary` |
| `device_info` | `GET /api/fetch-table?tableName=device_info` |
| `stress` | `GET /api/fetch-table?tableName=stress` |
| `resting_hr` | `GET /api/fetch-table?tableName=resting_hr` |
| `sleep_events` | `GET /api/fetch-table?tableName=sleep_events` |
| `files` | `GET /api/fetch-table?tableName=files` |
| `attributes` | `GET /api/fetch-table?tableName=attributes` |
| `_attributes` | `GET /api/fetch-table?tableName=_attributes` |

---

## **🔹 Save Data for Each Table**
Use the following endpoints to **save** each table as a JSON file:

| Table Name | Save Data Endpoint |
|------------|----------------------|
| `daily_summary` | `GET /api/save-table?tableName=daily_summary` |
| `device_info` | `GET /api/save-table?tableName=device_info` |
| `stress` | `GET /api/save-table?tableName=stress` |
| `resting_hr` | `GET /api/save-table?tableName=resting_hr` |
| `sleep_events` | `GET /api/save-table?tableName=sleep_events` |
| `files` | `GET /api/save-table?tableName=files` |
| `attributes` | `GET /api/save-table?tableName=attributes` |
| `_attributes` | `GET /api/save-table?tableName=_attributes` |

---

## **✅ Save All Tables at Once**
📌 **To save all tables as JSON files in `backend/data/raw_garmin_data/`, use:**
```
GET /api/save-all-tables
```

### **Example Response:**
```json
{
   "message": "All tables saved successfully",
   "tables": ["daily_summary", "device_info", "stress", "resting_hr", "sleep_events"]
}
```

---

## **📌 Notes**
- **Overwriting Behavior:** JSON files **will be replaced** when saving again.
- **Database Connection:** Uses the **SQLite database** specified in `GarminDatabaseConfig.java`.

---



