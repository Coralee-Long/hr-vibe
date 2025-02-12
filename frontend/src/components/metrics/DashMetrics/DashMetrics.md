# DashMetrics Component Data Structure

The `DashMetrics` component displays a **daily health summary** with metrics for:

- Heart Rate
- HRV
- Intensity Minutes
- Sleep
- Stress
- Body Battery
- VO2 Max

## Data Structure (`dailySummary`)

```json
{
  "date": "2025-01-01",
  "averageHR": {
    "today": 54,
    "week": [50, 52, 55, 53, 56, 54, 52]
  },
  "restingHR": {
    "today": 45,
    "week": [43, 44, 45, 46, 44, 43, 45]
  },
  "hrvStatus": {
    "today": 78,
    "week": [75, 77, 78, 76, 79, 80, 78]
  },
  "intensityMinutes": {
    "today": 60,
    "week": [45, 50, 55, 65, 70, 60, 50]
  },
  "stressLevel": {
    "today": 30,
    "week": [25, 28, 32, 35, 30, 27, 26]
  },
  "bodyBattery": {
    "today": 65,
    "week": [60, 63, 68, 70, 65, 62, 64]
  },
  "sleepDuration": {
    "today": 7.5,
    "week": [7, 7.2, 7.5, 6.8, 7.6, 7.3, 7.1]
  },
  "sleepQuality": {
    "today": 80,
    "week": [75, 78, 82, 85, 80, 77, 79]
  },
  "vo2Max": {
    "today": 45,
    "week": [44, 44, 45, 45, 45, 45, 45]
  }
}
```
