package com.backend.repos.MongoDB;

import com.backend.models.CurrentDaySummary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentDaySummaryRepo extends MongoRepository<CurrentDaySummary, String> {
}