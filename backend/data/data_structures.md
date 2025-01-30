## **📌 Model Names and Purpose**

| **Model Name** | **Purpose** | **Data Source** | **Stored in MongoDB as** |
|---------------|------------|----------------|--------------------------|
| `BaseSummary` | Embedded summary model for all timeframes | - | - |
| `CurrentDaySummary` | Latest available day’s data | `days_summary` | `current_day_summaries` |
| `RecentDailySummaries` | Last 7 days of daily data (pre-processed for mini-graphs) | `days_summary` (last 7 days) | `recent_daily_summaries` |
| `WeeklySummary` | Aggregated weekly data | `weeks_summary` | `weekly_summaries` |
| `MonthlySummary` | Aggregated monthly data | `months_summary` | `monthly_summaries` |
| `YearlySummary` | Aggregated yearly data | `years_summary` | `yearly_summaries` |

## **📌 Model Structures**

### **1️⃣ `BaseSummary` (Embedded Summary Model)**
- Used as a shared model for `CurrentDaySummary`, `WeeklySummary`, `MonthlySummary`, and `YearlySummary`.

```java
@Document // This will be embedded in other summary documents
public record BaseSummary(
    Double hrMax, // Max heart rate recorded

    Double caloriesActiveAvg, // Avg active calories burned
    Double hydrationIntake, // Total hydration intake (ml) (nullable)

    String remSleepMax, // Max REM sleep duration (HH:MM:SS)
    String intensityTimeGoal, // Total intensity time goal (HH:MM:SS)

    Double caloriesAvg, // Avg daily calories burned
    Double hydrationAvg, // Avg daily hydration intake (nullable)
    Integer caloriesGoal, // Target calorie burn goal
    Double sweatLoss, // Total sweat loss (ml) (nullable)

    Double rhrMax, // Max resting heart rate
    Double inactiveHrAvg, // Avg inactive heart rate
    Double inactiveHrMin, // Min inactive heart rate

    Integer stepsGoal, // Target steps goal
    Double caloriesBmrAvg, // Avg Basal Metabolic Rate (BMR)
    Double floors, // Total floors climbed
    Double caloriesConsumedAvg, // Avg calories consumed (nullable)
    Integer hydrationGoal, // Target hydration intake (ml)

    String remSleepAvg, // Avg REM sleep duration (HH:MM:SS)
    String remSleepMin, // Min REM sleep duration (HH:MM:SS)

    Double stressAvg, // Avg stress level
    Double rrMax, // Max respiration rate (RR)
    Double bbMin, // Min body battery

    Double inactiveHrMax, // Max inactive heart rate
    Double weightMin, // Min weight recorded (nullable)
    Double sweatLossAvg, // Avg sweat loss (ml) (nullable)

    String sleepAvg, // Avg sleep duration (HH:MM:SS)
    String sleepMin, // Min sleep duration (HH:MM:SS)

    Double rrMin, // Min respiration rate (RR)
    Double bbMax, // Max body battery
    Double spo2Avg, // Avg SpO2 (nullable)
    Double spo2Min, // Min SpO2 (nullable)
    Double weightAvg, // Avg weight recorded (nullable)
    Double activitiesDistance, // Total distance traveled (km) (nullable)

    Integer steps, // Total steps taken
    Double rrWakingAvg, // Avg waking respiration rate (RR)
    Double floorsGoal, // Target floors climbed
    Double weightMax, // Max weight recorded (nullable)

    Double hrAvg, // Avg heart rate
    Double hrMin, // Min heart rate

    String vigorousActivityTime, // Total time in vigorous activity (HH:MM:SS)
    Integer activities, // Total logged activities

    String moderateActivityTime, // Total time in moderate activity (HH:MM:SS)
    String intensityTime, // Total intensity minutes (HH:MM:SS)

    Double rhrAvg, // Avg resting heart rate
    Double rhrMin, // Min resting heart rate
    String sleepMax, // Max sleep duration (HH:MM:SS)

    Double activitiesCalories // Total calories burned in activities (nullable)
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

    List<Double> hrMax, // Last 7 days of max heart rate
    List<Double> caloriesActiveAvg, // Last 7 days of active calories burned
    List<Double> hydrationIntake, // Last 7 days of hydration intake (ml)

    List<String> remSleepMax, // Last 7 days of max REM sleep duration (HH:MM:SS)
    List<String> intensityTimeGoal, // Last 7 days of intensity time goal (HH:MM:SS)

    List<Double> caloriesAvg, // Last 7 days of avg daily calories burned
    List<Double> hydrationAvg, // Last 7 days of avg daily hydration intake
    List<Integer> caloriesGoal, // Last 7 days of calorie burn goals
    List<Double> sweatLoss, // Last 7 days of sweat loss

    List<Double> rhrMax, // Last 7 days of max resting heart rate
    List<Double> inactiveHrAvg, // Last 7 days of avg inactive heart rate
    List<Double> inactiveHrMin, // Last 7 days of min inactive heart rate

    List<Integer> stepsGoal, // Last 7 days of step goals
    List<Double> caloriesBmrAvg, // Last 7 days of avg BMR
    List<Double> floors, // Last 7 days of floors climbed
    List<Double> caloriesConsumedAvg, // Last 7 days of avg calories consumed
    List<Integer> hydrationGoal, // Last 7 days of hydration goals

    List<String> remSleepAvg, // Last 7 days of avg REM sleep duration (HH:MM:SS)
    List<String> remSleepMin, // Last 7 days of min REM sleep duration (HH:MM:SS)

    List<Double> stressAvg, // Last 7 days of avg stress level
    List<Double> rrMax, // Last 7 days of max respiration rate
    List<Double> bbMin, // Last 7 days of min body battery
    List<Double> inactiveHrMax, // Last 7 days of max inactive heart rate
    List<Double> weightMin, // Last 7 days of min weight recorded
    List<Double> sweatLossAvg, // Last 7 days of avg sweat loss

    List<String> sleepAvg, // Last 7 days of avg sleep duration (HH:MM:SS)
    List<String> sleepMin, // Last 7 days of min sleep duration (HH:MM:SS)

    List<Double> rrMin, // Last 7 days of min respiration rate
    List<Double> bbMax, // Last 7 days of max body battery
    List<Double> spo2Avg, // Last 7 days of avg SpO2
    List<Double> spo2Min, // Last 7 days of min SpO2
    List<Double> weightAvg, // Last 7 days of avg weight recorded
    List<Double> activitiesDistance, // Last 7 days of total distance traveled

    List<Integer> steps, // Last 7 days of steps taken
    List<Double> rrWakingAvg, // Last 7 days of avg waking respiration rate
    List<Double> floorsGoal, // Last 7 days of floors climbed goals
    List<Double> weightMax, // Last 7 days of max weight recorded

    List<Double> hrAvg, // Last 7 days of avg heart rate
    List<Double> hrMin, // Last 7 days of min heart rate

    List<String> vigorousActivityTime, // Last 7 days of vigorous activity duration (HH:MM:SS)
    List<Integer> activities, // Last 7 days of activities logged

    List<String> moderateActivityTime, // Last 7 days of moderate activity duration (HH:MM:SS)
    List<String> intensityTime, // Last 7 days of total intensity time (HH:MM:SS)

    List<Double> rhrAvg, // Last 7 days of avg resting heart rate
    List<Double> rhrMin, // Last 7 days of min resting heart rate
    List<String> sleepMax, // Last 7 days of max sleep duration (HH:MM:SS)

    List<Double> activitiesCalories // Last 7 days of calories burned in activities
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

