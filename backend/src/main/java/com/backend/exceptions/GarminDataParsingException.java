package com.backend.exceptions;

public class GarminDataParsingException extends RuntimeException {
   public GarminDataParsingException(String message) {
      super(message);
   }

   public GarminDataParsingException(String message, Throwable cause) {
      super(message, cause);
   }
}
