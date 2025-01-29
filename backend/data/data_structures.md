## **📌 Model Names and Purpose**
| **Model Name** | **Purpose** | **Data Source** |
|---------------|------------|----------------|
| **`CurrentDaySummary`** | Stores the latest available day's data | `days_summary` (latest entry) |
| **`RecentDailySummaries`** | Stores the last 7 days of daily data for mini-graphs | `days_summary` (last 7 days) |
| **`WeeklySummary`** | Stores one week’s aggregated data | `weeks_summary` |
| **`MonthlySummary`** | Stores one month’s aggregated data | `months_summary` |
| **`YearlySummary`** | Stores one year’s aggregated data | `years_summary` |

---

## **📌 Final Model Naming Scheme**
| **Type of Summary** | **Singular Name (One Entry)** | **Plural Name (Collection)** |
|--------------------|------------------|---------------------|
| **Today's Data** | `CurrentDaySummary` | `current_day_summaries` |
| **Last 7 Days (Mini-Graphs)** | `RecentDailySummaries` | `recent_daily_summaries` |
| **Weekly Data** | `WeeklySummary` | `weekly_summaries` |
| **Monthly Data** | `MonthlySummary` | `monthly_summaries` |
| **Yearly Data** | `YearlySummary` | `yearly_summaries` |

---
