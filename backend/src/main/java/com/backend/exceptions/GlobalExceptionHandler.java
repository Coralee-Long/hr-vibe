package com.backend.exceptions;

import com.backend.dtos.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler to catch and process exceptions thrown in the application.
 * This controller advice handles both specific exceptions such as GarminProcessingException and any unhandled exceptions.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

   private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

   /**
    * Handles GarminProcessingException and returns a structured error response.
    *
    * @param ex the GarminProcessingException thrown.
    * @return ResponseEntity containing the error details and BAD_REQUEST status.
    */
   @ExceptionHandler(GarminProcessingException.class)
   public ResponseEntity<ErrorResponse> handleGarminProcessingException(GarminProcessingException ex) {
      logger.error("Garmin processing error: {}", ex.getMessage());
      ErrorResponse error = new ErrorResponse("Garmin Processing Error", ex.getMessage());
      return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
   }

   /**
    * Handles any other exceptions not explicitly handled elsewhere.
    *
    * @param ex the exception thrown.
    * @return ResponseEntity containing the error details and INTERNAL_SERVER_ERROR status.
    */
   @ExceptionHandler(Exception.class)
   public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
      logger.error("Unexpected error: {}", ex.getMessage());
      ErrorResponse error = new ErrorResponse("Internal Server Error", "An unexpected error occurred");
      return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
   }
}
