package com.backend.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;

@Document(collection = "weekly_summaries") // Stores weekly aggregated data
@JsonInclude(JsonInclude.Include.ALWAYS)  // Ensures null fields are included
public record WeeklySummary(
    @Id String id, // Unique MongoDB ID

    @JsonFormat (shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Field ("firstDay") LocalDate firstDay, // First day of the week : Stored as LocalDate but formatted as a string in API responses

    @Field("summary") BaseSummary summary // Embedded summary data
) {}