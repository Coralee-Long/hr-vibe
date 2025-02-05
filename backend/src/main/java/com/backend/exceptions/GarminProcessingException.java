package com.backend.exceptions;

public class GarminProcessingException extends RuntimeException {
   public GarminProcessingException(String message) {
      super(message);
   }

   public GarminProcessingException(String message, Throwable cause) {
      super(message, cause);
   }
}