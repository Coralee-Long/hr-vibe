package com.backend.repos.MongoDB;

import com.backend.models.MonthlySummary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonthlySummaryRepo extends MongoRepository<MonthlySummary, String> {
}