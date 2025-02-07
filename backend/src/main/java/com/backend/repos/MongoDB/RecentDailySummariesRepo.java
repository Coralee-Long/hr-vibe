package com.backend.repos.MongoDB;

import com.backend.models.RecentDailySummaries;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface RecentDailySummariesRepo extends MongoRepository<RecentDailySummaries, String> {
   Optional<RecentDailySummaries> findByLatestDay (LocalDate latestDay);
}
