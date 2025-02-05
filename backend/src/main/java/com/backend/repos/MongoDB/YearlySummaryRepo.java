package com.backend.repos.MongoDB;

import com.backend.models.YearlySummary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface YearlySummaryRepo extends MongoRepository<YearlySummary, String> {
}