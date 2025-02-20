/**
 * Insights Page
 *
 * This page retrieves Garmin data by making multiple API calls—one for each 7‑day block over the full range
 * from "2025-01-01" to "2025-02-17". It then maps each block’s DTO into CombinedData objects (adding constant lifestyle factors)
 * and concatenates all the blocks together in chronological order.
 *
 * We then group the metrics into three charts:
 *   1. Cardio & Stress (bbMin, bbMax, stressAvg, rhrAvg)
 *   2. Respiration & SpO₂ (rrMin, rrMax, rrWakingAvg, spo2Min, spo2Avg)
 *   3. Time Durations (sleepAvg, remSleepAvg, intensityTime, moderateActivityTime, vigorousActivityTime)
 *
 * This grouping simplifies the y-axis scaling and improves clarity.
 */

import { useEffect, useState } from "react";
import { DefaultLayout } from "@/layout/DefaultLayout.tsx";
import { CombinedInsightsChart } from "@/components/charts/CombinedInsightsChart.tsx";
import { SyncedCharts } from "@/components/charts/SyncedCharts.tsx";
import { MultiMetricChart } from "@/components/charts/MultiMetricChart";
import { CombinedData } from "@/types/HealthData";
import GarminDataService from "@/api/services/garminDataService";
import { RecentDailySummariesDTO } from "@/types/RecentDailySummariesDTO";
import { formatDate } from "@/utils/dateUtils"; // This function formats a date string to "DD Month YYYY"

const DESIRED_START_DATE = "2025-01-01";
const DESIRED_END_DATE = "2025-02-17";

/**
 * Transforms a RecentDailySummariesDTO (a 7-day block) into an array of CombinedData.
 *
 * @param dto - The DTO returned by the API for a block of days.
 * @param blockEndDate - The reference date representing the block’s end (in "YYYY-MM-DD" format).
 * @returns An array of CombinedData objects for that block.
 */
const transformBlockData = (dto: RecentDailySummariesDTO, blockEndDate: string): CombinedData[] => {
  const blockLength = dto.rhrAvg.length; // expected to be 7
  const endDate = new Date(blockEndDate);
  const blockData: CombinedData[] = [];
  for (let i = 0; i < blockLength; i++) {
    const current = new Date(endDate);
    // Calculate the date for this index.
    current.setDate(endDate.getDate() - (blockLength - 1 - i));
    // Use toISOString to convert the Date to "YYYY-MM-DD"
    const dateStr = current.toISOString().split("T")[0];
    blockData.push({
      date: dateStr,
      rhrAvg: dto.rhrAvg[i],
      sweatLoss: dto.sweatLoss[i],
      bbMin: dto.bbMin[i],
      bbMax: dto.bbMax[i],
      stressAvg: dto.stressAvg[i],
      rrMin: dto.rrMin[i],
      rrMax: dto.rrMax[i],
      rrWakingAvg: dto.rrWakingAvg[i],
      spo2Min: dto.spo2Min[i],
      spo2Avg: dto.spo2Avg[i],
      sleepAvg: dto.sleepAvg[i],
      remSleepAvg: dto.remSleepAvg[i],
      activitiesDistance: dto.activitiesDistance[i],
      intensityTime: dto.intensityTime[i],
      moderateActivityTime: dto.moderateActivityTime[i],
      vigorousActivityTime: dto.vigorousActivityTime[i],
      // Add constant lifestyle factors for demo purposes.
      magnesium: false,
      binauralBeats: false,
      alcohol: true,
    });
  }
  return blockData;
};

export const Insights = () => {
  const [combinedData, setCombinedData] = useState<CombinedData[]>([]);
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    const fetchFullRangeData = async () => {
      setLoading(true);
      const allData: CombinedData[] = [];
      let currentEndDate = new Date(DESIRED_END_DATE);
      const startDate = new Date(DESIRED_START_DATE);
      while (currentEndDate >= startDate) {
        // Format currentEndDate as "YYYY-MM-DD" using toISOString, not formatDate.
        const refDateStr = currentEndDate.toISOString().split("T")[0];
        console.log("Fetching block ending at:", refDateStr);
        try {
          const blockDto: RecentDailySummariesDTO = await GarminDataService.getRecentDailySummaries(refDateStr);
          console.log("Fetched block DTO for", refDateStr, ":", blockDto);
          const blockData = transformBlockData(blockDto, refDateStr);
          console.log("Transformed block data:", blockData);
          // Prepend this block's data so that earlier dates come first.
          allData.unshift(...blockData);
          const blockLength = blockDto.rhrAvg.length;
          currentEndDate.setDate(currentEndDate.getDate() - blockLength);
        } catch (error) {
          console.error("Error fetching block for", refDateStr, error);
          break;
        }
      }
      console.log("Final combinedData passed to charts:", allData);
      setCombinedData(allData);
      setLoading(false);
    };
    fetchFullRangeData();
  }, []);

  // Prepare x-axis categories (dates).
  const categories = combinedData.map((d) => d.date);

  // --- Chart 1: Cardio & Stress Metrics ---
  const chart1Series = [
    { name: "Low Body Battery", data: combinedData.map((d) => d.bbMin) },
    { name: "High Body Battery", data: combinedData.map((d) => d.bbMax) },
    { name: "Stress Avg", data: combinedData.map((d) => d.stressAvg) },
    { name: "Resting HR", data: combinedData.map((d) => d.rhrAvg) },
  ];

  // --- Chart 2: Respiration & SpO₂ Metrics ---
  const chart2Series = [
    { name: "Respiration Min", data: combinedData.map((d) => d.rrMin) },
    { name: "Respiration Max", data: combinedData.map((d) => d.rrMax) },
    { name: "Waking Respiration Avg", data: combinedData.map((d) => d.rrWakingAvg) },
    { name: "SpO₂ Min", data: combinedData.map((d) => d.spo2Min) },
    { name: "SpO₂ Avg", data: combinedData.map((d) => d.spo2Avg) },
  ];

  // --- Chart 3: Time Durations (converted to minutes) ---
  const convertTime = (timeStr: string): number => {
    const [h, m, s] = timeStr.split(":").map(Number);
    return h * 60 + m + s / 60;
  };

  const chart3Series = [
    { name: "Sleep Avg (min)", data: combinedData.map((d) => convertTime(d.sleepAvg)) },
    { name: "REM Sleep (min)", data: combinedData.map((d) => convertTime(d.remSleepAvg)) },
    { name: "Intensity Time (min)", data: combinedData.map((d) => convertTime(d.intensityTime)) },
    { name: "Moderate Activity (min)", data: combinedData.map((d) => convertTime(d.moderateActivityTime)) },
    { name: "Vigorous Activity (min)", data: combinedData.map((d) => convertTime(d.vigorousActivityTime)) },
  ];

  return (
    <DefaultLayout>
      <div className="grid grid-cols-1 gap-8">
        {loading ? (
          <div>Loading data...</div>
        ) : (
          <>
            {/* Existing charts */}
            <CombinedInsightsChart data={combinedData} />
            <SyncedCharts data={combinedData} />
            {/* New MultiMetricCharts */}
            <MultiMetricChart
              categories={categories}
              seriesData={chart1Series}
              title="Cardio & Stress Metrics"
              loading={loading}
            />
            <MultiMetricChart
              categories={categories}
              seriesData={chart2Series}
              title="Respiration & SpO₂ Metrics"
              loading={loading}
            />
            <MultiMetricChart
              categories={categories}
              seriesData={chart3Series}
              title="Time Durations (minutes)"
              loading={loading}
            />
          </>
        )}
      </div>
    </DefaultLayout>
  );
};

export default Insights;
