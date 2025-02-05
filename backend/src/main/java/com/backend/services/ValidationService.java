package com.backend.services;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ValidationService {
   private final Validator validator;

   public ValidationService(Validator validator) {
      this.validator = validator;
   }

   public <T> void validate (@Valid T data) { // Ensure T is a valid JavaBean
      Set<ConstraintViolation<T>> violations = validator.validate(data);
      if (!violations.isEmpty()) {
         throw new IllegalArgumentException("Validation failed: " + violations);
      }
   }
}

