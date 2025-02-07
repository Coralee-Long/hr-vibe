package com.backend.repos.MongoDB;

import com.backend.models.MonthlySummary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for MonthlySummary.
 * Provides a method to retrieve monthly summaries based on month and year.
 */
@Repository
public interface MonthlySummaryRepo extends MongoRepository<MonthlySummary, String> {

   /**
    * Finds all monthly summaries for the specified month and year.
    *
    * @param month the month (1-12).
    * @param year the year.
    * @return a List of MonthlySummary objects that match the month and year.
    */
   List<MonthlySummary> findByMonthAndYear(Integer month, Integer year);
}
