// utils/mergeData.ts
import { convertTimeToMinutes } from "@/utils/timeUtils.ts";
import { HealthData, CombinedData } from "@/types/HealthData.ts"

export const mergeLifestyleData = (healthData: HealthData[]): CombinedData[] => {
  return healthData.map((day) => {
    // Convert REM sleep to minutes.
    const remMinutes = convertTimeToMinutes(day.remSleepAvg);

    // Fake logic: if REM sleep is low (< 90 minutes) or resting HR is high (>75 bpm),
    // assume a “bad” day where alcohol might be the cause.
    const alcohol = remMinutes < 90 || day.rhrAvg > 75;

    // Conversely, if REM sleep is good (>100 minutes) and resting HR is low (<65 bpm),
    // flag positive factors.
    const magnesium = !alcohol && remMinutes > 100 && day.rhrAvg < 65;
    const binauralBeats = !alcohol && remMinutes > 100 && day.rhrAvg < 65;

    return { ...day, magnesium, binauralBeats, alcohol };
  });
};
