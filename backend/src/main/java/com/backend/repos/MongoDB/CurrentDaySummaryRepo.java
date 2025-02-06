package com.backend.repos.MongoDB;

import com.backend.models.CurrentDaySummary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CurrentDaySummaryRepo extends MongoRepository<CurrentDaySummary, String> {
   // Fetch the last 7 daily summaries whose 'day' is less than or equal to the provided reference date,
   // sorted by 'day' in descending order (latest first).
   List<CurrentDaySummary> findTop7ByDayLessThanEqualOrderByDayDesc (LocalDate referenceDate);
}