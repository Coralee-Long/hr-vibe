package com.backend.repos;

import com.backend.models.DailySummary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DailySummaryRepo extends MongoRepository<DailySummary, String> {
   // Custom query to find a daily summary by the 'day' field
   Optional<DailySummary> findByDay(String day);
}
