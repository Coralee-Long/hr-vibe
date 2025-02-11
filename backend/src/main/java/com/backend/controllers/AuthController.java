package com.backend.controllers;

import com.backend.exceptions.LogoutException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

   /**
    * Returns details of the logged-in admin user (GitHub OAuth).
    */
   @GetMapping("/admin")
   public ResponseEntity<Map<String, String>> getAdminUser(@AuthenticationPrincipal OAuth2User user) {
      if (user == null) {
         return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not authenticated"));
      }

      return ResponseEntity.ok(Map.of(
          "username", user.getAttribute("login"),
          "firstName", "Coralee",  // -> Hardcode for now
          "lastName", "Long",          // -> Hardcode for now
          "city", "Bremen",            // GitHub doesn't provide city data -> Hardcode for now
          "country", "Germany",        // GitHub doesn't provide country data  -> Hardcode for now
          "role", "ADMIN"
          ));
   }

   /**
    * Creates a guest session and returns a limited guest profile.
    */
   @GetMapping("/guest")
   public ResponseEntity<Map<String, String>> guestLogin(HttpSession session) {
      session.setAttribute("guest", true);
      return ResponseEntity.ok(Map.of(
          "username", "GuestUser",
          "firstName", "Guest",
          "lastName", "Mode",
          "city", "Cape Town",   // -> Hardcode for now
          "country", "South Africa", // -> Hardcode for now
          "role", "GUEST"
          ));
   }

   /**
    * Logs the user out by invalidating their session.
    */
   @PostMapping("/logout")
   public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
      try {
         request.getSession().invalidate();
         request.logout();
         return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
      } catch (ServletException e) {
         throw new LogoutException("Error during logout: " + e.getMessage());
      }
   }
}
