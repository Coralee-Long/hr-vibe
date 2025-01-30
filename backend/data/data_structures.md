## **📌 Model Names and Purpose**

| **Model Name** | **Purpose** | **Data Source** | **Stored in MongoDB as** |
|---------------|------------|----------------|--------------------------|
| `CurrentDaySummary` | Latest available day’s data | `days_summary` | `current_day_summaries` |
| `RecentDailySummaries` | Last 7 days of daily data (mini-graphs) | `days_summary` (last 7 days) | `recent_daily_summaries` |
| `WeeklySummary` | Aggregated weekly data | `weeks_summary` | `weekly_summaries` |
| `MonthlySummary` | Aggregated monthly data | `months_summary` | `monthly_summaries` |
| `YearlySummary` | Aggregated yearly data | `years_summary` | `yearly_summaries` |

## **📌 Model Structures**
### **1️⃣ `RecentDailySummaries` (Last 7 Days for Mini-Graphs)**
- Stores **an array of the last 7 `CurrentDaySummary` entries**.
- Used for **dashboard mini-graphs**.

```java
@Document(collection = "recent_daily_summaries") // Stores the last 7 days of daily data
public record RecentDailySummaries(
    @Id String id, // Unique MongoDB ID
    List<CurrentDaySummary> last7Days // Array of daily summaries (latest 7 days)
) {}
```

---

### **2️⃣ `WeeklySummary` (One Week Aggregation)**
- Stores **one aggregated week of data**.
- Uses `firstDay` as the reference point (e.g., `"2024-01-01"`).

```java
@Document(collection = "weekly_summaries") // Stores weekly aggregated data
public record WeeklySummary(
    @Id String id, // Unique MongoDB ID
    LocalDate firstDay, // Start date of the week (e.g., 2024-01-01)

    Double hrMax, // Maximum heart rate recorded during the week
    Double caloriesActiveAvg, // Average active calories burned
    Double hydrationIntake, // Total hydration intake (ml)
    String remSleepMax, // Maximum REM sleep duration (HH:MM:SS)
    String intensityTimeGoal, // Weekly intensity time goal (HH:MM:SS)
    Double caloriesAvg, // Average daily calories burned
    Double hydrationAvg, // Average daily hydration intake
    Integer caloriesGoal, // Weekly calorie burn goal
    Double sweatLoss, // Total sweat loss during the week
    
    Double rhrMax, // Maximum resting heart rate
    Double inactiveHrAvg, // Weekly average inactive heart rate
    Double inactiveHrMin, // Weekly minimum inactive heart rate
    Integer stepsGoal, // Weekly step goal
    Double caloriesBmrAvg, // Weekly average Basal Metabolic Rate (BMR)
    Double floors, // Total floors climbed
    Double caloriesConsumedAvg, // Weekly average calories consumed
    Integer hydrationGoal, // Weekly hydration goal (ml)
    
    String remSleepAvg, // Weekly average REM sleep duration (HH:MM:SS)
    String remSleepMin, // Weekly minimum REM sleep duration (HH:MM:SS)
    Integer stressAvg, // Weekly average stress level
    Double rrMax, // Maximum respiration rate (RR)
    Integer bbMin, // Minimum body battery during the week
    Double inactiveHrMax, // Maximum inactive heart rate
    Double weightMin, // Minimum weight recorded in the week
    Double sweatLossAvg, // Average sweat loss (ml)

    String sleepAvg, // Weekly average sleep duration (HH:MM:SS)
    String sleepMin, // Weekly minimum sleep duration (HH:MM:SS)
    Double rrMin, // Minimum respiration rate (RR)
    Integer bbMax, // Maximum body battery during the week
    Double spo2Avg, // Weekly average SpO2 (oxygen saturation %)
    Double spo2Min, // Weekly minimum SpO2
    Double weightAvg, // Weekly average weight recorded
    Double activitiesDistance, // Total distance traveled in activities (km)
    
    Integer steps, // Total steps taken during the week
    Double rrWakingAvg, // Weekly average waking respiration rate (RR)
    Double floorsGoal, // Weekly floors goal
    Double weightMax, // Maximum weight recorded
    Double hrAvg, // Weekly average heart rate
    Double hrMin, // Weekly minimum heart rate
    String vigorousActivityTime, // Weekly total time in vigorous activity (HH:MM:SS)
    
    Integer activities, // Total number of activities logged
    String moderateActivityTime, // Weekly total time in moderate activity (HH:MM:SS)
    String intensityTime, // Weekly total intensity minutes (HH:MM:SS)
    Double rhrAvg, // Weekly average resting heart rate
    Double rhrMin, // Weekly minimum resting heart rate
    String sleepMax, // Maximum sleep duration during the week (HH:MM:SS)
    Double activitiesCalories // Total calories burned during activities
) {}
```

---

### **3️⃣ `MonthlySummary` (One Month Aggregation)**
- Stores **one aggregated month of data**.
- Uses `firstDay` as the reference point (always `YYYY-MM-01`).

```java
@Document(collection = "monthly_summaries") // Stores monthly aggregated data
public record MonthlySummary(
    @Id String id, // Unique MongoDB ID
    LocalDate firstDay, // First day of the month (YYYY-MM-01)

    Double hrMax, // Maximum heart rate recorded during the month
    Double caloriesActiveAvg, // Average active calories burned
    Double hydrationIntake, // Total hydration intake (ml)
    String remSleepMax, // Maximum REM sleep duration (HH:MM:SS)
    String intensityTimeGoal, // Monthly intensity time goal (HH:MM:SS)
    Double caloriesAvg, // Average daily calories burned
    Double hydrationAvg, // Average daily hydration intake
    Integer caloriesGoal, // Monthly calorie burn goal
    Double sweatLoss, // Total sweat loss during the month

    Double rhrMax, // Maximum resting heart rate
    Double inactiveHrAvg, // Monthly average inactive heart rate
    Double inactiveHrMin, // Monthly minimum inactive heart rate
    Integer stepsGoal, // Monthly step goal
    Double caloriesBmrAvg, // Monthly average Basal Metabolic Rate (BMR)
    Double floors, // Total floors climbed
    Double caloriesConsumedAvg, // Monthly average calories consumed
    Integer hydrationGoal, // Monthly hydration goal (ml)

    String remSleepAvg, // Monthly average REM sleep duration (HH:MM:SS)
    String remSleepMin, // Monthly minimum REM sleep duration (HH:MM:SS)
    Integer stressAvg, // Monthly average stress level
    Double rrMax, // Maximum respiration rate (RR)
    Integer bbMin, // Minimum body battery during the month
    Double inactiveHrMax, // Maximum inactive heart rate
    Double weightMin, // Minimum weight recorded in the month
    Double sweatLossAvg, // Average sweat loss (ml)

    String sleepAvg, // Monthly average sleep duration (HH:MM:SS)
    String sleepMin, // Monthly minimum sleep duration (HH:MM:SS)
    Double rrMin, // Minimum respiration rate (RR)
    Integer bbMax, // Maximum body battery during the month
    Double spo2Avg, // Monthly average SpO2 (oxygen saturation %)
    Double spo2Min, // Monthly minimum SpO2
    Double weightAvg, // Monthly average weight recorded
    Double activitiesDistance, // Total distance traveled in activities (km)

    Integer steps, // Total steps taken during the month
    Double rrWakingAvg, // Monthly average waking respiration rate (RR)
    Double floorsGoal, // Monthly floors goal
    Double weightMax, // Maximum weight recorded
    Double hrAvg, // Monthly average heart rate
    Double hrMin, // Monthly minimum heart rate
    String vigorousActivityTime, // Monthly total time in vigorous activity (HH:MM:SS)

    Integer activities, // Total number of activities logged
    String moderateActivityTime, // Monthly total time in moderate activity (HH:MM:SS)
    String intensityTime, // Monthly total intensity minutes (HH:MM:SS)
    Double rhrAvg, // Monthly average resting heart rate
    Double rhrMin, // Monthly minimum resting heart rate
    String sleepMax, // Maximum sleep duration during the month (HH:MM:SS)
    Double activitiesCalories // Total calories burned during activities
) {}
```

---

### **4️⃣ `YearlySummary` (One Year Aggregation)**
- Stores **one aggregated year of data**.
- Uses `firstDay` as the reference point (always `YYYY-01-01`).

```java
@Document(collection = "yearly_summaries") // Stores yearly aggregated data
public record YearlySummary(
    @Id String id, // Unique MongoDB ID
    LocalDate firstDay, // First day of the year (YYYY-01-01)

    Double hrMax, // Maximum heart rate recorded during the year
    Double caloriesActiveAvg, // Average active calories burned
    Double hydrationIntake, // Total hydration intake (ml)
    String remSleepMax, // Maximum REM sleep duration (HH:MM:SS)
    String intensityTimeGoal, // Yearly intensity time goal (HH:MM:SS)
    Double caloriesAvg, // Average daily calories burned
    Double hydrationAvg, // Average daily hydration intake
    Integer caloriesGoal, // Yearly calorie burn goal
    Double sweatLoss, // Total sweat loss during the year

    Double rhrMax, // Maximum resting heart rate
    Double inactiveHrAvg, // Yearly average inactive heart rate
    Double inactiveHrMin, // Yearly minimum inactive heart rate
    Integer stepsGoal, // Yearly step goal
    Double caloriesBmrAvg, // Yearly average Basal Metabolic Rate (BMR)
    Double floors, // Total floors climbed
    Double caloriesConsumedAvg, // Yearly average calories consumed
    Integer hydrationGoal, // Yearly hydration goal (ml)

    String remSleepAvg, // Yearly average REM sleep duration (HH:MM:SS)
    String remSleepMin, // Yearly minimum REM sleep duration (HH:MM:SS)
    Integer stressAvg, // Yearly average stress level
    Double rrMax, // Maximum respiration rate (RR)
    Integer bbMin, // Minimum body battery during the year
    Double inactiveHrMax, // Maximum inactive heart rate
    Double weightMin, // Minimum weight recorded in the year
    Double sweatLossAvg, // Average sweat loss (ml)

    String sleepAvg, // Yearly average sleep duration (HH:MM:SS)
    String sleepMin, // Yearly minimum sleep duration (HH:MM:SS)
    Double rrMin, // Minimum respiration rate (RR)
    Integer bbMax, // Maximum body battery during the year
    Double spo2Avg, // Yearly average SpO2 (oxygen saturation %)
    Double spo2Min, // Yearly minimum SpO2
    Double weightAvg, // Yearly average weight recorded
    Double activitiesDistance, // Total distance traveled in activities (km)

    Integer steps, // Total steps taken during the year
    Double rrWakingAvg, // Yearly average waking respiration rate (RR)
    Double floorsGoal, // Yearly floors goal
    Double weightMax, // Maximum weight recorded
    Double hrAvg, // Yearly average heart rate
    Double hrMin, // Yearly minimum heart rate
    String vigorousActivityTime, // Yearly total time in vigorous activity (HH:MM:SS)

    Integer activities, // Total number of activities logged
    String moderateActivityTime, // Yearly total time in moderate activity (HH:MM:SS)
    String intensityTime, // Yearly total intensity minutes (HH:MM:SS)
    Double rhrAvg, // Yearly average resting heart rate
    Double rhrMin, // Yearly minimum resting heart rate
    String sleepMax, // Maximum sleep duration during the year (HH:MM:SS)
    Double activitiesCalories // Total calories burned during activities
) {}
```
