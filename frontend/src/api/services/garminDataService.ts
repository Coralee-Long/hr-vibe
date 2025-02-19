import axios from 'axios';
import { RecentDailySummariesDTO } from "@/types/RecentDailySummariesDTO.ts";
import { CurrentDaySummaryDTO } from "@/types/CurrentDaySummaryDTO.ts";
import { WeeklySummaryDTO } from "@/types/WeeklySummaryDTO.ts";
import { MonthlySummaryDTO } from "@/types/MonthlySummaryDTO.ts";
import { YearlySummaryDTO } from "@/types/YearlySummaryDTO.ts";

// Base URL for Garmin endpoints
const BASE_URL = "http://localhost:8080/garmin";

class GarminDataService {
  /**
   * Retrieves an array of day summary DTOs.
   * @param limit Number of summaries to return (default: 30)
   */
  async getAllDaySummaries(limit: number = 30): Promise<CurrentDaySummaryDTO[]> {
    console.log(`[API CALL] GET /days?limit=${limit}`);
    const response = await axios.get(`${BASE_URL}/days`, { params: { limit } });
    return response.data;
  }

  /**
   * Retrieves a single day summary for the specified date.
   * @param day Date string in yyyy-mm-dd format.
   */
  async getDaySummary(day: string): Promise<CurrentDaySummaryDTO> {
    console.log(`[API CALL] GET /days/${day}`);
    const response = await axios.get(`${BASE_URL}/days/${day}`);
    return response.data;
  }

  /**
   * Retrieves the recent daily summaries (last 7 days) based on a reference date.
   * @param referenceDate Date string in yyyy-mm-dd format.
   */
  async getRecentDailySummaries(referenceDate: string): Promise<RecentDailySummariesDTO> {
    console.log(`[API CALL] GET /recent/${referenceDate}`);
    const response = await axios.get(`${BASE_URL}/recent/${referenceDate}`);
    return response.data;
  }

  /**
   * Retrieves an array of weekly summary DTOs.
   * @param limit Number of summaries to return (default: 30)
   */
  async getAllWeekSummaries(limit: number = 30): Promise<WeeklySummaryDTO[]> {
    console.log(`[API CALL] GET /weeks?limit=${limit}`);
    const response = await axios.get(`${BASE_URL}/weeks`, { params: { limit } });
    return response.data;
  }

  /**
   * Retrieves a weekly summary for a given reference date.
   * @param referenceDate Date string in ISO format.
   */
  async getWeekSummary(referenceDate: string): Promise<WeeklySummaryDTO> {
    console.log(`[API CALL] GET /weeks/${referenceDate}`);
    const response = await axios.get(`${BASE_URL}/weeks/${referenceDate}`);
    return response.data;
  }

  /**
   * Retrieves an array of monthly summary DTOs.
   * Optionally filters by year if provided.
   * @param year Optional year filter.
   */
  async getMonthSummaries(year?: number): Promise<MonthlySummaryDTO[]> {
    console.log(`[API CALL] GET /months${year ? `?year=${year}` : ""}`);
    const response = await axios.get(`${BASE_URL}/months`, { params: { year } });
    return response.data;
  }

  /**
   * Retrieves an array of yearly summary DTOs.
   */
  async getYearSummaries(): Promise<YearlySummaryDTO[]> {
    console.log(`[API CALL] GET /years`);
    const response = await axios.get(`${BASE_URL}/years`);
    return response.data;
  }
}

export default new GarminDataService();
