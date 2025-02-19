/**
 * Data representing the RecentDailySummariesDTO.
 * This type is based on the backend DTO for recent daily summaries.
 */
export type RecentDailySummariesDTO = {
  // Unique MongoDB ID
  id: string;

  // The most recent date in the dataset (ISO string)
  latestDay: string;

  // ------ Heart Rate (last 7 days) ------ //
  hrMin: number[];            // Minimum heart rate values
  hrMax: number[];            // Maximum heart rate values
  hrAvg: number[];            // Average heart rate values
  rhrMin: number[];           // Minimum resting heart rate values
  rhrMax: number[];           // Maximum resting heart rate values
  rhrAvg: number[];           // Average resting heart rate values
  inactiveHrMin: number[];    // Minimum inactive heart rate values
  inactiveHrMax: number[];    // Maximum inactive heart rate values
  inactiveHrAvg: number[];    // Average inactive heart rate values

  // ------ Calories (last 7 days) ------ //
  caloriesAvg: number[];      // Average daily calories burned
  caloriesGoal: number[];     // Calorie burn goals
  caloriesBmrAvg: number[];   // Average BMR (Basal Metabolic Rate)
  caloriesConsumedAvg: number[]; // Average calories consumed
  caloriesActiveAvg: number[];   // Average active calories burned
  activitiesCalories: number[];  // Calories burned in activities

  // ------ Weight (last 7 days) ------ //
  weightMin: number[];        // Minimum weight recorded
  weightMax: number[];        // Maximum weight recorded
  weightAvg: number[];        // Average weight recorded

  // ------ Hydration (last 7 days) ------ //
  hydrationGoal: number[];    // Hydration goals (ml)
  hydrationIntake: number[];  // Total hydration intake (ml)
  hydrationAvg: number[];     // Average daily hydration intake (ml)
  sweatLoss: number[];        // Total sweat loss (ml)
  sweatLossAvg: number[];     // Average sweat loss (ml)

  // ------ Stress & Body Battery (last 7 days) ------ //
  bbMin: number[];            // Minimum body battery values
  bbMax: number[];            // Maximum body battery values
  stressAvg: number[];        // Average stress levels

  // ------ Respiration & SPO2 (last 7 days) ------ //
  rrMin: number[];            // Minimum respiration rate values
  rrMax: number[];            // Maximum respiration rate values
  rrWakingAvg: number[];      // Average waking respiration rate values
  spo2Min: number[];          // Minimum SpO2 levels
  spo2Avg: number[];          // Average SpO2 levels

  // ------ Sleep (last 7 days) ------ //
  // Time values are represented as strings in HH:MM:SS format.
  sleepMin: string[];         // Minimum sleep duration
  sleepMax: string[];         // Maximum sleep duration
  sleepAvg: string[];         // Average sleep duration
  remSleepMin: string[];      // Minimum REM sleep duration
  remSleepMax: string[];      // Maximum REM sleep duration
  remSleepAvg: string[];      // Average REM sleep duration

  // ------ Steps & Floors (last 7 days) ------ //
  stepsGoal: number[];        // Steps goals
  steps: number[];            // Total steps taken
  floorsGoal: number[];       // Floors climbed goal
  floors: number[];           // Total floors climbed

  // ------ Activities (last 7 days) ------ //
  activities: number[];       // Total logged activities
  activitiesDistance: number[]; // Total distance traveled (km)
  intensityTimeGoal: string[];  // Intensity time goals (HH:MM:SS)
  intensityTime: string[];      // Total intensity minutes (HH:MM:SS)
  moderateActivityTime: string[]; // Total moderate activity time (HH:MM:SS)
  vigorousActivityTime: string[]; // Total vigorous activity time (HH:MM:SS)
}
