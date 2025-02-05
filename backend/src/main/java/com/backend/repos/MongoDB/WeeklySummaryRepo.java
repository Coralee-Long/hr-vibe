package com.backend.repos.MongoDB;

import com.backend.models.WeeklySummary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeeklySummaryRepo extends MongoRepository<WeeklySummary, String> {
}