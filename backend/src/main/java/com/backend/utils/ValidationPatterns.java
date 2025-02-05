package com.backend.utils;

/**
 * Defines reusable validation patterns for DTOs.
 */
public class ValidationPatterns {
   public static final String TIME_PATTERN = "^([0-9]{2}):([0-5][0-9]):([0-5][0-9])$";

   private ValidationPatterns() {
      // Private constructor to prevent instantiation
   }
}
