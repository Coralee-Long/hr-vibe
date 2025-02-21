import { useEffect, useState } from "react";
import { DefaultLayout } from "@/layout/DefaultLayout.tsx";
// Import our four separate chart components
import { MultiMetricChartOne } from "@/components/charts/MultiMetricChartOne.tsx";
import { MultiMetricChartTwo } from "@/components/charts/MulitMetricChartTwo.tsx";
import { MultiMetricChartThree } from "@/components/charts/MultiMetricChartThree.tsx";
import { MultiMetricChartFour } from "@/components/charts/MultiMetricChartFour.tsx";
import { GraphCardWrapper } from "@/common/GraphCardWrapper";
import { MultiMetricSeries } from "@/types/MultiMetricsChartTypes.ts";
import { CombinedData } from "@/types/HealthData";
import GarminDataService from "@/api/services/garminDataService";
import { RecentDailySummariesDTO } from "@/types/RecentDailySummariesDTO";
import { RiBattery2ChargeFill } from "react-icons/ri";
import { FaHeartPulse, FaPersonRunning } from "react-icons/fa6";
import { BsCloudSun } from "react-icons/bs";

const DESIRED_START_DATE = "2024-12-01";
const DESIRED_END_DATE = "2025-02-17";

/**
 * Transforms a RecentDailySummariesDTO (a 7-day block) into an array of CombinedData.
 * Provides safe defaults to avoid undefined values.
 */
const transformBlockData = (dto: RecentDailySummariesDTO, blockEndDate: string): CombinedData[] => {
  const blockLength = dto.rhrAvg.length;
  const endDate = new Date(blockEndDate);
  const blockData: CombinedData[] = [];
  for (let i = 0; i < blockLength; i++) {
    const current = new Date(endDate);
    current.setDate(endDate.getDate() - (blockLength - 1 - i));
    const dateStr = current.toISOString().split("T")[0];
    blockData.push({
      date: dateStr,
      rhrAvg: dto.rhrAvg[i] ?? 0,
      sweatLoss: dto.sweatLoss[i] ?? 0,
      bbMin: dto.bbMin[i] ?? 0,
      bbMax: dto.bbMax[i] ?? 0,
      stressAvg: dto.stressAvg[i] ?? 0,
      rrMin: dto.rrMin[i] ?? 0,
      rrMax: dto.rrMax[i] ?? 0,
      rrWakingAvg: dto.rrWakingAvg[i] ?? 0,
      spo2Min: dto.spo2Min[i] ?? 0,
      spo2Avg: dto.spo2Avg[i] ?? 0,
      sleepAvg: dto.sleepAvg[i] ?? "00:00:00",
      remSleepAvg: dto.remSleepAvg[i] ?? "00:00:00",
      activitiesDistance: dto.activitiesDistance[i] ?? 0,
      intensityTime: dto.intensityTime[i] ?? "00:00:00",
      moderateActivityTime: dto.moderateActivityTime[i] ?? "00:00:00",
      vigorousActivityTime: dto.vigorousActivityTime[i] ?? "00:00:00",
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
      const currentEndDate = new Date(DESIRED_END_DATE);
      const startDate = new Date(DESIRED_START_DATE);
      while (currentEndDate >= startDate) {
        const refDateStr = currentEndDate.toISOString().split("T")[0];
        try {
          const blockDto: RecentDailySummariesDTO = await GarminDataService.getRecentDailySummaries(refDateStr);
          const blockData = transformBlockData(blockDto, refDateStr);
          allData.unshift(...blockData);
          const blockLength = blockDto.rhrAvg.length;
          currentEndDate.setDate(currentEndDate.getDate() - blockLength);
        } catch (error) {
          console.error("Error fetching block for", refDateStr, error);
          break;
        }
      }
      setCombinedData(allData);
      setLoading(false);
    };
    fetchFullRangeData();
  }, []);

  // Even if series are in xy format, we calculate categories as a fallback.
  // const categories = combinedData.map(d => d.date);

  /**
   * Chart 1: Body Battery & Cardio Metrics
   */
  const chart1Series: MultiMetricSeries[] = [
    {
      name: "Body Battery Range",
      type: "rangeArea" as const,
      data: combinedData.map(d => ({ x: d.date, y: [d.bbMin ?? 0, d.bbMax ?? 0] })),
    },
    {
      name: "Body Battery Average",
      type: "line" as const,
      data: combinedData.map(d => ({ x: d.date, y: ((d.bbMin ?? 0) + (d.bbMax ?? 0)) / 2 })),
    },
    {
      name: "Stress Avg",
      type: "line" as const,
      data: combinedData.map(d => ({ x: d.date, y: d.stressAvg ?? 0 })),
    },
    {
      name: "Resting HR",
      type: "line" as const,
      data: combinedData.map(d => ({ x: d.date, y: d.rhrAvg ?? 0 })),
    }
  ];

  /**
   * Chart 2: Respiration & SpO₂ Metrics
   */
  const chart2Series: MultiMetricSeries[] = [
    {
      name: "Respiration Range",
      type: "rangeArea" as const,
      data: combinedData.map(d => ({ x: d.date, y: [d.rrMin ?? 0, d.rrMax ?? 0] })),
    },
    {
      name: "Waking Respiration Avg",
      type: "line" as const,
      data: combinedData.map(d => ({ x: d.date, y: d.rrWakingAvg ?? 0 })),
    },
    {
      name: "SpO₂ Avg",
      type: "line" as const,
      data: combinedData.map(d => ({ x: d.date, y: d.spo2Avg ?? 0 })),
    }
  ];

  /**
   * Chart 3: Sleep Bars (Stacked)
   */
  const convertTime = (timeStr: string): number => {
    if (!timeStr) return 0;
    const parts = timeStr.split(":").map(Number);
    if (parts.length !== 3) return 0;
    const [h, m, s] = parts;
    return h * 60 + m + s / 60;
  };
  const remSleepMins = combinedData.map(d => convertTime(d.remSleepAvg));
  const totalSleepMins = combinedData.map(d => convertTime(d.sleepAvg));
  const chart3Series: MultiMetricSeries[] = [
    {
      name: "REM Sleep (min)",
      type: "bar" as const,
      data: remSleepMins.map((y, i) => ({ x: combinedData[i].date, y })),
    },
    {
      name: "Total Sleep (min)",
      type: "bar" as const,
      data: totalSleepMins.map((y, i) => ({ x: combinedData[i].date, y })),
    }
  ];

  /**
   * Chart 4: Activity Metrics
   */
  const chart4Series: MultiMetricSeries[] = [
    {
      name: "Intensity Time (min)",
      type: "area" as const,
      data: combinedData.map(d => ({ x: d.date, y: convertTime(d.intensityTime) })),
    },
    {
      name: "Moderate Activity (min)",
      type: "line" as const,
      data: combinedData.map(d => ({ x: d.date, y: convertTime(d.moderateActivityTime) })),
    },
    {
      name: "Vigorous Activity (min)",
      type: "line" as const,
      data: combinedData.map(d => ({ x: d.date, y: convertTime(d.vigorousActivityTime) })),
    }
  ];

  return (
    <DefaultLayout>
      <div className="grid grid-cols-1 gap-8">
        {loading ? (
          <div>Loading data...</div>
        ) : (
          <div className="insights">
            {/* Wrap each chart in a GraphCardWrapper */}
            <GraphCardWrapper
              title="Stress & Cardio Metrics"
              icon={<RiBattery2ChargeFill size={32} color="#FFB54C" />}
            >
              <MultiMetricChartOne
                seriesData={chart1Series}
                title="Body Battery Range, Stress & Resting Heart Rate"
                loading={loading}
              />
            </GraphCardWrapper>
            <GraphCardWrapper
              title="Respiration Metrics"
              icon={<FaHeartPulse size={32} color="#10B981" />}
            >
              <MultiMetricChartTwo
                seriesData={chart2Series}
                title="Respiration & SpO₂"
                loading={loading}
              />
            </GraphCardWrapper>
            <GraphCardWrapper
              title="Sleep Metrics"
              icon={<BsCloudSun size={32} color="#3C50E0" />}
            >
              <MultiMetricChartThree
                seriesData={chart3Series}
                title="REM & Total Sleep Duration"
                loading={loading}
              />
            </GraphCardWrapper>
            <GraphCardWrapper
              title="Activity Metrics"
              icon={<FaPersonRunning size={32} color="#ff9466" />}
            >
              <MultiMetricChartFour
                seriesData={chart4Series}
                title="Activity Intensity & Effort"
                loading={loading}
              />
            </GraphCardWrapper>
          </div>
        )}
      </div>
    </DefaultLayout>
  );
};

export default Insights;
