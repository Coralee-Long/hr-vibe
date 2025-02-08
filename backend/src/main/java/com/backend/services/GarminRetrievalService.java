package com.backend.services;

import com.backend.dtos.CurrentDaySummaryDTO;
import com.backend.dtos.MonthlySummaryDTO;
import com.backend.dtos.RecentDailySummariesDTO;
import com.backend.dtos.WeeklySummaryDTO;
import com.backend.dtos.YearlySummaryDTO;
import com.backend.exceptions.GarminProcessingException;
import com.backend.models.MonthlySummary;
import com.backend.models.YearlySummary;
import com.backend.repos.MongoDB.CurrentDaySummaryRepo;
import com.backend.repos.MongoDB.MonthlySummaryRepo;
import com.backend.repos.MongoDB.WeeklySummaryRepo;
import com.backend.repos.MongoDB.YearlySummaryRepo;
import com.backend.repos.MongoDB.RecentDailySummariesRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * GarminRetrievalService provides methods to retrieve Garmin summary data from various MongoDB repositories.
 * It converts the retrieved models into DTOs before returning them to the controller layer.

 * The following types of summaries are available:

 *   - Day summaries
 *   - Recent daily summaries (last 7 days)
 *   - Week summaries
 *   - Month summaries (either all, or filtered by year)
 *   - Year summaries
 */
@Service
public class GarminRetrievalService {

   private static final Logger logger = LoggerFactory.getLogger(GarminRetrievalService.class);

   private final CurrentDaySummaryRepo currentDaySummaryRepo;
   private final WeeklySummaryRepo weeklySummaryRepo;
   private final MonthlySummaryRepo monthlySummaryRepo;
   private final YearlySummaryRepo yearlySummaryRepo;
   private final RecentDailySummariesRepo recentDailySummariesRepo;

   public GarminRetrievalService(CurrentDaySummaryRepo currentDaySummaryRepo,
                                 WeeklySummaryRepo weeklySummaryRepo,
                                 MonthlySummaryRepo monthlySummaryRepo,
                                 YearlySummaryRepo yearlySummaryRepo,
                                 RecentDailySummariesRepo recentDailySummariesRepo) {
      this.currentDaySummaryRepo = currentDaySummaryRepo;
      this.weeklySummaryRepo = weeklySummaryRepo;
      this.monthlySummaryRepo = monthlySummaryRepo;
      this.yearlySummaryRepo = yearlySummaryRepo;
      this.recentDailySummariesRepo = recentDailySummariesRepo;
   }

   /**
    * Retrieves a list of day summaries up to the specified limit.
    *
    * @param limit the maximum number of day summaries to return.
    * @return a List of CurrentDaySummaryDTO objects.
    */
   public List<CurrentDaySummaryDTO> getAllDaySummaries(int limit) {
      logger.info("Retrieving up to {} day summaries from MongoDB...", limit);
      Pageable pageable = PageRequest.of(0, limit);
      return currentDaySummaryRepo.findAllByOrderByDayDesc(pageable).stream()
          .map(CurrentDaySummaryDTO::fromModel)
          .collect(Collectors.toList());
   }

   /**
    * Retrieves a day summary for a specific date.
    *
    * @param day the date to look up.
    * @return a CurrentDaySummaryDTO for the given day.
    * @throws GarminProcessingException if no summary is found for the day.
    */
   public CurrentDaySummaryDTO getDaySummary(LocalDate day) {
      logger.info("Retrieving day summary for date {}...", day);
      return currentDaySummaryRepo.findByDay(day)
          .map(CurrentDaySummaryDTO::fromModel)
          .orElseThrow(() -> new GarminProcessingException("No day summary found for " + day));
   }

   /**
    * Retrieves recent daily summaries based on the given reference date.
    *
    * @param referenceDate the reference date for fetching recent summaries.
    * @return a RecentDailySummariesDTO for the reference date.
    * @throws GarminProcessingException if no recent daily summaries are found.
    */
   public RecentDailySummariesDTO getRecentDailySummaries(LocalDate referenceDate) {
      logger.info("Retrieving recent daily summaries for reference date {}...", referenceDate);
      return recentDailySummariesRepo.findByLatestDay(referenceDate)
          .map(RecentDailySummariesDTO::fromModel)
          .orElseThrow(() -> new GarminProcessingException("No recent daily summaries found for " + referenceDate));
   }

   /**
    * Retrieves a list of weekly summary DTOs up to the specified limit.
    *
    * @param limit the maximum number of weekly summaries to return.
    * @return a List of WeeklySummaryDTO objects.
    */
   public List<WeeklySummaryDTO> getAllWeekSummaries(int limit) {
      logger.info("Retrieving up to {} weekly summaries from MongoDB...", limit);
      Pageable pageable = PageRequest.of(0, limit);
      return weeklySummaryRepo.findAllByOrderByFirstDayDesc(pageable).stream()
          .map(WeeklySummaryDTO::fromModel)
          .collect(Collectors.toList());
   }

   /**
    * Retrieves a weekly summary based on the reference date.
    *
    * @param referenceDate the reference date used to determine the week.
    * @return a WeeklySummaryDTO for the reference week.
    * @throws GarminProcessingException if no weekly summary is found.
    */
   public WeeklySummaryDTO getWeekSummary(LocalDate referenceDate) {
      logger.info("Retrieving weekly summary for reference date {}...", referenceDate);
      return weeklySummaryRepo.findByFirstDay(referenceDate)
          .map(WeeklySummaryDTO::fromModel)
          .orElseThrow(() -> new GarminProcessingException("No weekly summary found for " + referenceDate));
   }

   /**
    * Retrieves monthly summaries.
    *
    * <p>If the "year" parameter is provided, only summaries for that year are returned.
    * Otherwise, all monthly summaries are returned.
    *
    * <p>The list is sorted in descending order by the <code>firstDay</code> field.
    *
    * @param year the year to filter by, or null to retrieve all summaries.
    * @return a List of MonthlySummaryDTO objects.
    * @throws GarminProcessingException if processing fails.
    */
   public List<MonthlySummaryDTO> getMonthSummaries(Integer year) {
      logger.info("Retrieving monthly summaries with year={}", year);
      List<MonthlySummary> summaries;
      if (year == null) {
         summaries = monthlySummaryRepo.findAll();
      } else {
         summaries = monthlySummaryRepo.findByYear(year);
      }
      // Sort the summaries in descending order by the firstDay field.
      summaries.sort(Comparator.comparing(MonthlySummary::firstDay).reversed());
      return summaries.stream()
          .map(MonthlySummaryDTO::fromModel)
          .collect(Collectors.toList());
   }

   /**
    * Retrieves all yearly summaries.
    *
    * <p>The list is sorted in descending order by the <code>firstDay</code> field.
    *
    * @return a List of YearlySummaryDTO objects.
    */
   public List<YearlySummaryDTO> getYearSummaries() {
      logger.info("Retrieving yearly summaries from MongoDB...");
      List<YearlySummary> summaries = yearlySummaryRepo.findAll();
      // Sort the yearly summaries in descending order by the firstDay field.
      summaries.sort(Comparator.comparing(YearlySummary::firstDay).reversed());
      return summaries.stream()
          .map(YearlySummaryDTO::fromModel)
          .collect(Collectors.toList());
   }
}
