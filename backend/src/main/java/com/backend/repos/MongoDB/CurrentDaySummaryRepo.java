package com.backend.repos.MongoDB;

import com.backend.models.CurrentDaySummary;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CurrentDaySummaryRepo extends MongoRepository<CurrentDaySummary, String> {
   // Existing method (if needed) for fetching summaries up to a reference date.
   List<CurrentDaySummary> findTop7ByDayLessThanEqualOrderByDayDesc(LocalDate referenceDate);

   // New method to fetch a single day summary by its date.
   Optional<CurrentDaySummary> findByDay(LocalDate day);

   // New method to fetch day summaries sorted in descending order with dynamic limit support.
   List<CurrentDaySummary> findAllByOrderByDayDesc(Pageable pageable);
}
