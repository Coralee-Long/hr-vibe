export type BaseSummaryDTO = {
  hrMin: number;
  hrMax: number;
  hrAvg: number;
  rhrMin: number;
  rhrMax: number;
  rhrAvg: number;
  inactiveHrMin: number;
  inactiveHrMax: number;
  inactiveHrAvg: number;

  caloriesAvg: number;
  caloriesGoal: number;
  caloriesBmrAvg: number;
  caloriesConsumedAvg: number | null;
  caloriesActiveAvg: number;
  activitiesCalories: number;

  weightMin: number | null;
  weightMax: number | null;
  weightAvg: number | null;

  hydrationGoal: number;
  hydrationIntake: number;
  hydrationAvg: number;
  sweatLoss: number;
  sweatLossAvg: number;

  bbMin: number;
  bbMax: number;
  stressAvg: number;

  rrMin: number;
  rrMax: number;
  rrWakingAvg: number;
  spo2Min: number;
  spo2Avg: number;

  sleepMin: string;
  sleepMax: string;
  sleepAvg: string;
  remSleepMin: string;
  remSleepMax: string;
  remSleepAvg: string;

  stepsGoal: number;
  steps: number;
  floorsGoal: number;
  floors: number;

  activities: number;
  activitiesDistance: number;
  intensityTimeGoal: string;
  intensityTime: string;
  moderateActivityTime: string;
  vigorousActivityTime: string;
};

export type CurrentDaySummaryDTO = {
  id: string;
  day: string;
  summary: BaseSummaryDTO;
};
