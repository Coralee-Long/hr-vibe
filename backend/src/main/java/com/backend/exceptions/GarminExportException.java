package com.backend.exceptions;

public class GarminExportException extends RuntimeException {
   public GarminExportException(String message) {
      super(message);
   }

   public GarminExportException(String message, Throwable cause) {
      super(message, cause);
   }
}

