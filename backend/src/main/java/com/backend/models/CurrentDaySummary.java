package com.backend.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Document(collection = "current_day_summaries") // Stores the latest day's summary
@JsonInclude(JsonInclude.Include.ALWAYS)  // Ensures null fields are included
public record CurrentDaySummary(
    @Id String id, // Unique MongoDB ID

    @Field ("day")
    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate day, // Specific day for the summary: // Stored as LocalDate but formatted as a string in API responses

    @Field("summary") BaseSummary summary // Embedded summary data
) {}
