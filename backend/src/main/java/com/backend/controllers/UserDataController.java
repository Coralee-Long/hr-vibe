package com.backend.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

public class UserDataController {

   // For Later - Prevent Guest from posting/changing data
//   @PostMapping ("/api/data")
//   public ResponseEntity<String> createData (HttpSession session) {
//      Boolean isGuest = (Boolean) session.getAttribute("guest");
//      if (Boolean.TRUE.equals(isGuest)) {
//         return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Guest users cannot create data.");
//      }
//      return ResponseEntity.ok("Data created successfully!");
//   }
}
