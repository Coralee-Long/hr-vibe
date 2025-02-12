import axios from 'axios';
import { RecentDailySummariesDTO} from "@/types/RecentDailySummariesDTO.ts";
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
    try {
      const response = await axios.get(`${BASE_URL}/days`, { params: { limit } });
      return response.data;
    } catch (error) {
      console.error("Error fetching day summaries:", error);
      throw error;
    }
  }

  /**
   * Retrieves a single day summary for the specified date.
   * @param day Date string in yyyy-mm-dd format.
   */
  async getDaySummary(day: string): Promise<CurrentDaySummaryDTO> {
    try {
      const response = await axios.get(`${BASE_URL}/days/${day}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching day summary for ${day}:`, error);
      throw error;
    }
  }

  /**
   * Retrieves the recent daily summaries (last 7 days) based on a reference date.
   * @param referenceDate Date string in yyyy-mm-dd format.
   */
  async getRecentDailySummaries(referenceDate: string): Promise<RecentDailySummariesDTO> {
    try {
      const response = await axios.get(`${BASE_URL}/recent/${referenceDate}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching recent daily summaries for ${referenceDate}:`, error);
      throw error;
    }
  }

  /**
   * Retrieves an array of weekly summary DTOs.
   * @param limit Number of summaries to return (default: 30)
   */
  async getAllWeekSummaries(limit: number = 30): Promise<WeeklySummaryDTO[]> {
    try {
      const response = await axios.get(`${BASE_URL}/weeks`, { params: { limit } });
      return response.data;
    } catch (error) {
      console.error("Error fetching weekly summaries:", error);
      throw error;
    }
  }

  /**
   * Retrieves a weekly summary for a given reference date.
   * @param referenceDate Date string in ISO format.
   */
  async getWeekSummary(referenceDate: string): Promise<WeeklySummaryDTO> {
    try {
      const response = await axios.get(`${BASE_URL}/weeks/${referenceDate}`);
      return response.data;
    } catch (error) {
      console.error(`Error fetching weekly summary for ${referenceDate}:`, error);
      throw error;
    }
  }

  /**
   * Retrieves an array of monthly summary DTOs.
   * Optionally filters by year if provided.
   * @param year Optional year filter.
   */
  async getMonthSummaries(year?: number): Promise<MonthlySummaryDTO[]> {
    try {
      // The backend supports an optional "year" query parameter.
      const response = await axios.get(`${BASE_URL}/months`, { params: { year } });
      return response.data;
    } catch (error) {
      console.error("Error fetching monthly summaries:", error);
      throw error;
    }
  }

  /**
   * Retrieves an array of yearly summary DTOs.
   */
  async getYearSummaries(): Promise<YearlySummaryDTO[]> {
    try {
      const response = await axios.get(`${BASE_URL}/years`);
      return response.data;
    } catch (error) {
      console.error("Error fetching yearly summaries:", error);
      throw error;
    }
  }
}

export default new GarminDataService();