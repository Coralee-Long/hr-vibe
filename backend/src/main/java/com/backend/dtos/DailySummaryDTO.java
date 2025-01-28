package com.backend.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DailySummaryDTO(
    @JsonProperty("step_goal") int step_goal,
    @JsonProperty("hr_max") int hr_max,
    @JsonProperty("distance") double distance,
    @JsonProperty("hydration_intake") int hydration_intake,
    @JsonProperty("intensity_time_goal") String intensity_time_goal,
    @JsonProperty("calories_total") int calories_total,
    @JsonProperty("description") String description,
    @JsonProperty("calories_goal") int calories_goal,
    @JsonProperty("sweat_loss") int sweat_loss,
    @JsonProperty("floors_up") double floors_up,
    @JsonProperty("hydration_goal") int hydration_goal,
    @JsonProperty("day") String day,
    @JsonProperty("stress_avg") int stress_avg,
    @JsonProperty("bb_charged") int bb_charged,
    @JsonProperty("rr_max") double rr_max,
    @JsonProperty("calories_bmr") int calories_bmr,
    @JsonProperty("bb_min") int bb_min,
    @JsonProperty("rr_min") double rr_min,
    @JsonProperty("bb_max") int bb_max,
    @JsonProperty("spo2_avg") double spo2_avg,
    @JsonProperty("spo2_min") double spo2_min,
    @JsonProperty("steps") int steps,
    @JsonProperty("floors_down") double floors_down,
    @JsonProperty("calories_consumed") Integer calories_consumed,
    @JsonProperty("rr_waking_avg") double rr_waking_avg,
    @JsonProperty("calories_active") int calories_active,
    @JsonProperty("floors_goal") double floors_goal,
    @JsonProperty("hr_min") int hr_min,
    @JsonProperty("vigorous_activity_time") String vigorous_activity_time,
    @JsonProperty("moderate_activity_time") String moderate_activity_time,
    @JsonProperty("rhr") int rhr
) {}
