export type HealthData = {
  date: string;       // e.g. "2025-01-13"
  rhrAvg: number;          // Avg resting heart rate (20-150 bpm)
  sweatLoss: number;        // Total sweat loss (ml, ≥ 0)
  bbMin: number;    // Min body battery (0-100)
  bbMax: number;    // Max body battery (0-100)
  stressAvg: number; // Avg stress level (0-100)
  rrMin: number;       // Min respiration rate (5-50 bpm)
  rrMax: number;       // Max respiration rate (5-50 bpm)
  rrWakingAvg: number; // Avg waking respiration rate (5-50 bpm)
  spo2Min: number;     // Min SpO2 level (70-100%)
  spo2Avg: number;     // Avg SpO2 level (70-100%)
  sleepAvg: string;   // Avg sleep duration
  remSleepAvg: string; // Avg REM sleep duration
  activitiesDistance: number; // Total distance traveled (km, ≥ 0)
  intensityTime: string;      // Total intensity minutes
  moderateActivityTime: string; // Total moderate activity time
  vigorousActivityTime: string; // Total vigorous activity time
}

// Your combined data will include the lifestyle booleans:
export interface CombinedData extends HealthData {
  magnesium: boolean;
  binauralBeats: boolean;
  alcohol: boolean;
}