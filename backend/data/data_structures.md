## **📌 Model Names and Purpose**

| **Model Name**         | **Purpose**                                               | **Data Source**              | **Stored in MongoDB as** |
| ---------------------- | --------------------------------------------------------- | ---------------------------- | ------------------------ |
| `BaseSummary`          | Embedded summary model for all timeframes                 | -                            | -                        |
| `CurrentDaySummary`    | Latest available day’s data                               | `days_summary`               | `current_day_summaries`  |
| `RecentDailySummaries` | Last 7 days of daily data (pre-processed for mini-graphs) | `days_summary` (last 7 days) | `recent_daily_summaries` |
| `WeeklySummary`        | Aggregated weekly data                                    | `weeks_summary`              | `weekly_summaries`       |
| `MonthlySummary`       | Aggregated monthly data                                   | `months_summary`             | `monthly_summaries`      |
| `YearlySummary`        | Aggregated yearly data                                    | `years_summary`              | `yearly_summaries`       |

## **📌 Model Structures**

### **1️⃣ `BaseSummary` (Embedded Summary Model)**

- Used as a shared model for `CurrentDaySummary`, `WeeklySummary`, `MonthlySummary`, and `YearlySummary`.

```java
@Document // This will be embedded in other summary documents
public record BaseSummary(

    Integer hrMin, // Min heart rate
    Integer hrMax, // Max heart rate
    Integer hrAvg, // Avg heart rate

    Integer rhrMin, // Min resting heart rate
    Integer rhrMax, // Max resting heart rate
    Integer rhrAvg, // Avg resting heart rate

    Integer inactiveHrMin, // Min inactive heart rate
    Integer inactiveHrMax, // Max inactive heart rate
    Integer inactiveHrAvg, // Avg inactive heart rate

    Integer caloriesAvg, // Avg daily calories burned
    Integer caloriesGoal, // Target calorie burn goal
    Integer caloriesBmrAvg, // Avg Basal Metabolic Rate (BMR)
    Integer caloriesConsumedAvg, // Avg calories consumed
    Integer caloriesActiveAvg, // Avg active calories burned
    Integer activitiesCalories, // Total calories burned in activities

    Double weightMin, // Min weight recorded
    Double weightMax, // Max weight recorded
    Double weightAvg, // Avg weight recorded

    Integer hydrationGoal, // Target hydration intake (ml)
    Integer hydrationIntake, // Total hydration intake (ml)
    Integer hydrationAvg, // Avg daily hydration intake
    Integer sweatLoss, // Total sweat loss (ml)
    Integer sweatLossAvg, // Avg sweat loss (ml)

    Integer bbMin, // Min body battery
    Integer bbMax, // Max body battery
    Integer stressAvg, // Avg stress level

    Integer rrMin, // Min respiration rate (RR)
    Integer rrMax, // Max respiration rate (RR)
    Integer rrWakingAvg, // Avg waking respiration rate (RR)

    String sleepMin, // Min sleep duration (HH:MM:SS)
    String sleepMax, // Max sleep duration (HH:MM:SS)
    String sleepAvg, // Avg sleep duration (HH:MM:SS)

    String remSleepMin, // Min REM sleep duration (HH:MM:SS)
    String remSleepMax, // Max REM sleep duration (HH:MM:SS)
    String remSleepAvg, // Avg REM sleep duration (HH:MM:SS)

    Integer spo2Min, // Min SpO2
    Integer spo2Avg, // Avg SpO2

    Integer stepsGoal, // Target steps goal
    Integer steps, // Total steps taken
    Integer floorsGoal, // Target floors climbed
    Integer floors, // Total floors climbed

    Integer activities, // Total logged activities
    Double activitiesDistance, // Total distance traveled (km)

    String intensityTimeGoal, // Total intensity time goal (HH:MM:SS)
    String intensityTime, // Total intensity minutes (HH:MM:SS)
    String moderateActivityTime, // Total time in moderate activity (HH:MM:SS)
    String vigorousActivityTime // Total time in vigorous activity (HH:MM:SS)
) {}
```

---

### **2️⃣ `CurrentDaySummary` (Latest Available Day’s Data)**

- Stores **a single day's health metrics**.

```java
@Document(collection = "current_day_summaries") // Stores the latest day's summary
public record CurrentDaySummary(
    @Id String id, // Unique MongoDB ID
    LocalDate day, // Specific day for the summary (YYYY-MM-DD)
    BaseSummary summary // Embedded summary data
) {}
```

---

### **3️⃣ `RecentDailySummaries` (Last 7 Days Pre-Processed for Mini-Graphs)**

- Stores **processed arrays** for the last 7 days instead of individual `CurrentDaySummary` objects.
- Used for **trend graphs and visualizations** in the frontend.

```java
@Document(collection = "recent_daily_summaries") // Stores pre-processed 7-day history
public record RecentDailySummaries(
    @Id String id, // Unique MongoDB ID
    LocalDate latestDay, // Most recent date in the dataset

    List<Integer> hrMin, // Last 7 days of min heart rate
    List<Integer> hrMax, // Last 7 days of max heart rate
    List<Integer> hrAvg, // Last 7 days of avg heart rate

    List<Integer> rhrMin, // Last 7 days of min resting heart rate
    List<Integer> rhrMax, // Last 7 days of max resting heart rate
    List<Integer> rhrAvg, // Last 7 days of avg resting heart rate

    List<Integer> inactiveHrMin, // Last 7 days of min inactive heart rate
    List<Integer> inactiveHrMax, // Last 7 days of max inactive heart rate
    List<Integer> inactiveHrAvg, // Last 7 days of avg inactive heart rate

    List<Integer> caloriesAvg, // Last 7 days of avg daily calories burned
    List<Integer> caloriesGoal, // Last 7 days of calorie burn goals
    List<Integer> caloriesBmrAvg, // Last 7 days of avg BMR
    List<Integer> caloriesConsumedAvg, // Last 7 days of avg calories consumed
    List<Integer> caloriesActiveAvg, // Last 7 days of avg active calories burned
    List<Integer> activitiesCalories, // Last 7 days of calories burned in activities

    List<Double> weightMin, // Last 7 days of min weight recorded
    List<Double> weightMax, // Last 7 days of max weight recorded
    List<Double> weightAvg, // Last 7 days of avg weight recorded

    List<Integer> hydrationGoal, // Last 7 days of hydration goals (ml)
    List<Integer> hydrationIntake, // Last 7 days of total hydration intake (ml)
    List<Integer> hydrationAvg, // Last 7 days of avg daily hydration intake
    List<Integer> sweatLoss, // Last 7 days of total sweat loss (ml)
    List<Integer> sweatLossAvg, // Last 7 days of avg sweat loss (ml)

    List<Integer> bbMin, // Last 7 days of min body battery
    List<Integer> bbMax, // Last 7 days of max body battery
    List<Integer> stressAvg, // Last 7 days of avg stress level

    List<Integer> rrMin, // Last 7 days of min respiration rate (RR)
    List<Integer> rrMax, // Last 7 days of max respiration rate (RR)
    List<Integer> rrWakingAvg, // Last 7 days of avg waking respiration rate (RR)

    List<String> sleepMin, // Last 7 days of min sleep duration (HH:MM:SS)
    List<String> sleepMax, // Last 7 days of max sleep duration (HH:MM:SS)
    List<String> sleepAvg, // Last 7 days of avg sleep duration (HH:MM:SS)

    List<String> remSleepMin, // Last 7 days of min REM sleep duration (HH:MM:SS)
    List<String> remSleepMax, // Last 7 days of max REM sleep duration (HH:MM:SS)
    List<String> remSleepAvg, // Last 7 days of avg REM sleep duration (HH:MM:SS)

    List<Integer> spo2Min, // Last 7 days of min SpO2
    List<Integer> spo2Avg, // Last 7 days of avg SpO2

    List<Integer> stepsGoal, // Last 7 days of step goals
    List<Integer> steps, // Last 7 days of steps taken
    List<Integer> floorsGoal, // Last 7 days of floors climbed goals
    List<Integer> floors, // Last 7 days of floors climbed

    List<Integer> activities, // Last 7 days of logged activities
    List<Double> activitiesDistance, // Last 7 days of total distance traveled (km) (nullable)

    List<String> intensityTimeGoal, // Last 7 days of total intensity time goal (HH:MM:SS)
    List<String> intensityTime, // Last 7 days of total intensity minutes (HH:MM:SS)
    List<String> moderateActivityTime, // Last 7 days of total time in moderate activity (HH:MM:SS)
    List<String> vigorousActivityTime // Last 7 days of total time in vigorous activity (HH:MM:SS)
) {}
```

---

### **4️⃣ `WeeklySummary` (One Week Aggregation)**

```java
@Document(collection = "weekly_summaries") // Stores weekly aggregated data
public record WeeklySummary(
    @Id String id, // Unique MongoDB ID
    LocalDate firstDay, // Start date of the week (YYYY-MM-DD)
    BaseSummary summary // Embedded summary data
) {}
```

---

### **5️⃣ `MonthlySummary` (One Month Aggregation)**

```java
@Document(collection = "monthly_summaries") // Stores monthly aggregated data
public record MonthlySummary(
    @Id String id, // Unique MongoDB ID
    LocalDate firstDay, // First day of the month (YYYY-MM-01)
    BaseSummary summary // Embedded summary data
) {}
```

---

### **6️⃣ `YearlySummary` (One Year Aggregation)**

```java
@Document(collection = "yearly_summaries") // Stores yearly aggregated data
public record YearlySummary(
    @Id String id, // Unique MongoDB ID
    LocalDate firstDay, // First day of the year (YYYY-01-01)
    BaseSummary summary // Embedded summary data
) {}
```

---

## **📌 Key Changes & Improvements**

✅ **Pre-Processed Mini-Graph Data**: `RecentDailySummaries` now stores **arrays** instead of raw `CurrentDaySummary` objects.
✅ **Consistent Summary Models**: `WeeklySummary`, `MonthlySummary`, and `YearlySummary` use **`BaseSummary`** to avoid duplication.
✅ **Optimized for Fast Retrieval**: Data is **pre-processed before saving to MongoDB**, reducing query overhead at runtime.

---

## **📌 Next Steps**

- Implement **DTOs with validation** for all summaries.
- Develop **service logic** to:
  1. Fetch the last 7 `CurrentDaySummary` entries.
  2. Process & store them in `RecentDailySummaries`.
  3. Generate aggregated data for Daily, Weekly, Monthly, and Yearly
     summaries.
- Set up **API endpoints** for fetching summary data.
