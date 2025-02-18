package com.backend.repos.MongoDB;

import com.backend.models.WeeklySummary;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for WeeklySummary.
 * Provides a method to retrieve a weekly summary based on the first day of the week.
 */
@Repository
public interface WeeklySummaryRepo extends MongoRepository<WeeklySummary, String> {

   /**
    * Finds the weekly summary that has the specified first day.
    *
    * @param firstDay the first day of the week.
    * @return an Optional containing the WeeklySummary if found.
    */
   Optional<WeeklySummary> findByFirstDay(LocalDate firstDay);

   /**
    * Retrieves all weekly summaries sorted in descending order by firstDay.
    *
    * @param pageable the pagination information.
    * @return a List of WeeklySummary objects.
    */
   List<WeeklySummary> findAllByOrderByFirstDayDesc (Pageable pageable);
}
