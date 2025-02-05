package com.backend.exceptions;

public class GarminDatabaseException extends RuntimeException {
   public GarminDatabaseException(String message) {
      super(message);
   }

   public GarminDatabaseException(String message, Throwable cause) {
      super(message, cause);
   }
}
