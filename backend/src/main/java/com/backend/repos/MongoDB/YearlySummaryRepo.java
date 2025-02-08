package com.backend.repos.MongoDB;

import com.backend.models.YearlySummary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Repository interface for managing YearlySummary documents in MongoDB.
 * This interface extends the Spring Data MongoRepository to provide standard CRUD operations.
 *
 * <p>Additional custom query methods:
 * <ul>
 *   <li>{@link #findByFirstDay(LocalDate)} - Retrieves a YearlySummary based on its firstDay value.</li>
 * </ul>
 * </p>
 */
@Repository
public interface YearlySummaryRepo extends MongoRepository<YearlySummary, String> {

   /**
    * Finds a YearlySummary by its starting day (firstDay).
    *
    * <p>This method is used to check for the existence of a YearlySummary with a specific firstDay,
    * which is considered the unique key for the yearly summary. Duplicate prevention in the processing
    * service is based on this field.</p>
    *
    * @param firstDay the starting day of the year summary.
    * @return an {@code Optional} containing the matching YearlySummary if found, or an empty Optional otherwise.
    */
   Optional<YearlySummary> findByFirstDay(LocalDate firstDay);
}
