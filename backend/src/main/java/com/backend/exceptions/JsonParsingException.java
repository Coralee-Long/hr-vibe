package com.backend.exceptions;

/**
 * Custom exception for handling JSON parsing errors.
 */
public class JsonParsingException extends RuntimeException {
   public JsonParsingException(String message, Throwable cause) {
      super(message, cause);
   }
}

