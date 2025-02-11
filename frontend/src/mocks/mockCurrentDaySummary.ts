import { CurrentDaySummaryDTO } from "@/types/CurrentDaySummary";

export const mockCurrentDaySummary: CurrentDaySummaryDTO = {
  id: "mock-id-12345",
  day: "2025-01-12",
  summary: {
    hrMin: 67,
    hrMax: 157,
    hrAvg: 87,
    rhrMin: 68,
    rhrMax: 68,
    rhrAvg: 68,
    inactiveHrMin: 69,
    inactiveHrMax: 98,
    inactiveHrAvg: 78,

    caloriesAvg: 3064,
    caloriesGoal: 1240,
    caloriesBmrAvg: 2030,
    caloriesConsumedAvg: null,
    caloriesActiveAvg: 1034,
    activitiesCalories: 1015,

    weightMin: null,
    weightMax: null,
    weightAvg: null,

    hydrationGoal: 3000,
    hydrationIntake: 0,
    hydrationAvg: 0,
    sweatLoss: 1032,
    sweatLossAvg: 1032,

    bbMin: 11,
    bbMax: 39,
    stressAvg: 40,

    rrMin: 10,
    rrMax: 24,
    rrWakingAvg: 15,
    spo2Min: 84,
    spo2Avg: 95,

    sleepMin: "11:04:00",
    sleepMax: "11:04:00",
    sleepAvg: "11:04:00",
    remSleepMin: "02:25:00",
    remSleepMax: "02:25:00",
    remSleepAvg: "02:25:00",

    stepsGoal: 10000,
    steps: 20476,
    floorsGoal: 10,
    floors: 9,

    activities: 1,
    activitiesDistance: 12.65751,
    intensityTimeGoal: "00:21:25",
    intensityTime: "03:15:00",
    moderateActivityTime: "00:49:00",
    vigorousActivityTime: "01:13:00",
  },
};
