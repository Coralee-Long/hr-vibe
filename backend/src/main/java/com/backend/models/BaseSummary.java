package com.backend.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document // This will be embedded in other summary documents
@JsonInclude (JsonInclude.Include.ALWAYS)  // Ensures null fields are included
public record BaseSummary(

    // ------ Heart Rate ----- //
    @Field("hrMin") Integer hrMin, // Min heart rate
    @Field("hrMax") Integer hrMax, // Max heart rate
    @Field("hrAvg") Integer hrAvg, // Avg heart rate
    @Field("rhrMin") Integer rhrMin, // Min resting heart rate
    @Field("rhrMax") Integer rhrMax, // Max resting heart rate
    @Field("rhrAvg") Integer rhrAvg, // Avg resting heart rate
    @Field("inactiveHrMin") Integer inactiveHrMin, // Min inactive heart rate
    @Field("inactiveHrMax") Integer inactiveHrMax, // Max inactive heart rate
    @Field("inactiveHrAvg") Integer inactiveHrAvg, // Avg inactive heart rate

    // ------ Calories ----- //
    @Field("caloriesAvg") Integer caloriesAvg, // Avg daily calories burned
    @Field("caloriesGoal") Integer caloriesGoal, // Target calorie burn goal
    @Field("caloriesBmrAvg") Integer caloriesBmrAvg, // Avg Basal Metabolic Rate (BMR)
    @Field("caloriesConsumedAvg") Integer caloriesConsumedAvg, // Avg calories consumed
    @Field("caloriesActiveAvg") Integer caloriesActiveAvg, // Avg active calories burned
    @Field("activitiesCalories") Integer activitiesCalories, // Total calories burned in activities

    // ------ Weight ----- //
    @Field("weightMin") Double weightMin, // Min weight recorded
    @Field("weightMax") Double weightMax, // Max weight recorded
    @Field("weightAvg") Double weightAvg, // Avg weight recorded

    // ------ Hydration ----- //
    @Field("hydrationGoal") Integer hydrationGoal, // Target hydration intake (ml)
    @Field("hydrationIntake") Integer hydrationIntake, // Total hydration intake (ml)
    @Field("hydrationAvg") Integer hydrationAvg, // Avg daily hydration intake
    @Field("sweatLoss") Integer sweatLoss, // Total sweat loss (ml)
    @Field("sweatLossAvg") Integer sweatLossAvg, // Avg sweat loss (ml)

    // ------ Stress & Body Battery ----- //
    @Field("bbMin") Integer bbMin, // Min body battery
    @Field("bbMax") Integer bbMax, // Max body battery
    @Field("stressAvg") Integer stressAvg, // Avg stress level

    // ------ Respiration & SPO ----- //
    @Field("rrMin") Integer rrMin, // Min respiration rate (RR)
    @Field("rrMax") Integer rrMax, // Max respiration rate (RR)
    @Field("rrWakingAvg") Integer rrWakingAvg, // Avg waking respiration rate (RR)
    @Field("spo2Min") Integer spo2Min, // Min SpO2
    @Field("spo2Avg") Integer spo2Avg, // Avg SpO2

    // ------ Sleep ----- //
    @Field("sleepMin") String sleepMin, // Min sleep duration (HH:MM:SS)
    @Field("sleepMax") String sleepMax, // Max sleep duration (HH:MM:SS)
    @Field("sleepAvg") String sleepAvg, // Avg sleep duration (HH:MM:SS)
    @Field("remSleepMin") String remSleepMin, // Min REM sleep duration (HH:MM:SS)
    @Field("remSleepMax") String remSleepMax, // Max REM sleep duration (HH:MM:SS)
    @Field("remSleepAvg") String remSleepAvg, // Avg REM sleep duration (HH:MM:SS)

    // ------ Steps & Floors ----- //
    @Field("stepsGoal") Integer stepsGoal, // Target steps goal
    @Field("steps") Integer steps, // Total steps taken
    @Field("floorsGoal") Integer floorsGoal, // Target floors climbed
    @Field("floors") Integer floors, // Total floors climbed

    // ------ Activities ----- //
    @Field("activities") Integer activities, // Total logged activities
    @Field("activitiesDistance") Double activitiesDistance, // Total distance traveled (km)
    @Field("intensityTimeGoal") String intensityTimeGoal, // Total intensity time goal (HH:MM:SS)
    @Field("intensityTime") String intensityTime, // Total intensity minutes (HH:MM:SS)
    @Field("moderateActivityTime") String moderateActivityTime, // Total time in moderate activity (HH:MM:SS)
    @Field("vigorousActivityTime") String vigorousActivityTime // Total time in vigorous activity (HH:MM:SS)
) {}

