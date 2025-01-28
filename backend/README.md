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
