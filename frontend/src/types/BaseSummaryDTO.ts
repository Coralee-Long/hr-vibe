/**
 * A summary of key health metrics.
 * This type is built based on the backend BaseSummaryDTO.
 */
export type BaseSummaryDTO = {
  // ------ Heart Rate ----- //
  hrMin: number;           // Min heart rate (20-250 bpm)
  hrMax: number;           // Max heart rate (20-250 bpm)
  hrAvg: number;           // Avg heart rate (20-250 bpm)
  rhrMin: number;          // Min resting heart rate (20-150 bpm)
  rhrMax: number;          // Max resting heart rate (20-150 bpm)
  rhrAvg: number;          // Avg resting heart rate (20-150 bpm)
  inactiveHrMin: number;   // Min inactive heart rate
  inactiveHrMax: number;   // Max inactive heart rate
  inactiveHrAvg: number;   // Avg inactive heart rate

  // ------ Calories ----- //
  caloriesAvg: number;         // Avg daily calories burned (≥0)
  caloriesGoal: number;        // Target calorie burn goal (≥0)
  caloriesBmrAvg: number;      // Avg Basal Metabolic Rate (BMR) (≥0)
  caloriesConsumedAvg: number; // Avg daily calories consumed (≥0)
  caloriesActiveAvg: number;   // Avg active calories burned (≥0)
  activitiesCalories: number;  // Total calories burned in activities (≥0)

  // ------ Weight ----- //
  weightMin: number;       // Min weight recorded (0-300 kg)
  weightMax: number;       // Max weight recorded (0-300 kg)
  weightAvg: number;       // Avg weight recorded (0-300 kg)

  // ------ Hydration ----- //
  hydrationGoal: number;    // Target hydration intake (ml, ≥0)
  hydrationIntake: number;  // Total hydration intake (ml, ≥0)
  hydrationAvg: number;     // Avg daily hydration intake (ml, ≥0)
  sweatLoss: number;        // Total sweat loss (ml, ≥0)
  sweatLossAvg: number;     // Avg sweat loss (ml, ≥0)

  // ------ Stress & Body Battery ----- //
  bbMin: number;    // Min body battery (0-100)
  bbMax: number;    // Max body battery (0-100)
  stressAvg: number; // Avg stress level (0-100)

  // ------ Respiration & SPO2 ----- //
  rrMin: number;       // Min respiration rate (5-50 bpm)
  rrMax: number;       // Max respiration rate (5-50 bpm)
  rrWakingAvg: number; // Avg waking respiration rate (5-50 bpm)
  spo2Min: number;     // Min SpO2 level (70-100%)
  spo2Avg: number;     // Avg SpO2 level (70-100%)

  // ------ Sleep ----- //
  // Expected format: "HH:MM:SS"
  sleepMin: string;   // Min sleep duration
  sleepMax: string;   // Max sleep duration
  sleepAvg: string;   // Avg sleep duration
  remSleepMin: string; // Min REM sleep duration
  remSleepMax: string; // Max REM sleep duration
  remSleepAvg: string; // Avg REM sleep duration

  // ------ Steps & Floors ----- //
  stepsGoal: number;  // Target step count (≥0)
  steps: number;      // Total steps taken (≥0)
  floorsGoal: number; // Target floors climbed (≥0)
  floors: number;     // Total floors climbed (≥0)

  // ------ Activities ----- //
  activities: number;         // Total logged activities (≥0)
  activitiesDistance: number; // Total distance traveled (km, ≥0)
  // Expected format: "HH:MM:SS"
  intensityTimeGoal: string;  // Total intensity time goal
  intensityTime: string;      // Total intensity minutes
  moderateActivityTime: string; // Total moderate activity time
  vigorousActivityTime: string; // Total vigorous activity time
}
