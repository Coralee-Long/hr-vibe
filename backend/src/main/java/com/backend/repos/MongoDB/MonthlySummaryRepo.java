package com.backend.repos.MongoDB;

import com.backend.models.MonthlySummary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for MonthlySummary.
 * Provides methods to retrieve monthly summaries.
 *
 * In the simplified design:
 * - To get all monthly summaries, use the inherited {@code findAll()} method.
 * - To get monthly summaries for a specific year, use {@code findByYear()}.
 * - To check for duplicates (by the first day of the month), use {@code findByFirstDay()}.
 */
@Repository
public interface MonthlySummaryRepo extends MongoRepository<MonthlySummary, String> {

   /**
    * Finds all monthly summaries for the specified year.
    *
    * @param year the year.
    * @return a List of MonthlySummary objects that match the given year.
    */
   @Query("{ '$expr': { '$eq': [ { '$year': '$firstDay' }, ?0 ] } }")
   List<MonthlySummary> findByYear(Integer year);

   /**
    * Finds a monthly summary with the given firstDay.
    *
    * @param firstDay the first day of the month.
    * @return an Optional containing the MonthlySummary if found.
    */
   @Query("{ 'firstDay': ?0 }")
   Optional<MonthlySummary> findByFirstDay(LocalDate firstDay);
}
