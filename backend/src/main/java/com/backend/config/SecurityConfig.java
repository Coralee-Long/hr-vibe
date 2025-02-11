package com.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Security configuration for authentication, authorization, CORS, and session management.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http
          .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ Enable CORS
          // Instead of disabling CSRF, enable it using a CookieCsrfTokenRepository.
          .csrf(csrf -> csrf
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
               )
          .authorizeHttpRequests(auth -> auth
                                     // ✅ Guest Mode Access (Public)
                                     .requestMatchers("/auth/guest").permitAll() // Allow guests to start a session

                                     // 🔒 Admin-Only Routes
                                     .requestMatchers("/auth/admin").authenticated() // Only OAuth users can access admin info
                                     .requestMatchers(HttpMethod.POST, "/auth/logout").permitAll() // Allow all users (guests & OAuth) to logout

                                     // ✅ Guests & Admins Can Read Garmin Data (Allow All GET Requests)
                                     .requestMatchers(HttpMethod.GET, "/garmin/days").permitAll()
                                     .requestMatchers(HttpMethod.GET, "/garmin/days/**").permitAll()
                                     .requestMatchers(HttpMethod.GET, "/garmin/recent/**").permitAll()
                                     .requestMatchers(HttpMethod.GET, "/garmin/weeks").permitAll()
                                     .requestMatchers(HttpMethod.GET, "/garmin/weeks/**").permitAll()
                                     .requestMatchers(HttpMethod.GET, "/garmin/months").permitAll()
                                     .requestMatchers(HttpMethod.GET, "/garmin/years").permitAll()

                                     // 🔒 Admins Only - Modify Garmin Data
                                     .requestMatchers(HttpMethod.POST, "/garmin/**").authenticated()
                                     .requestMatchers(HttpMethod.PUT, "/garmin/**").authenticated()
                                     .requestMatchers(HttpMethod.DELETE, "/garmin/**").authenticated()
                                     // TODO: Add the extra lifestyle factors endpoints later
                                     //  (Guest: READ , Admin: READ & WRITE)

                                     // ✅ Allow all other requests by default
                                     .anyRequest().permitAll()
                                )
          .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)) // ✅ Enable session-based Guest login
          .exceptionHandling(ex -> ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))) // 🛑 Unauthorized users get 401
          .oauth2Login(oauth2 -> oauth2
              .defaultSuccessUrl("http://localhost:5173/dashboard", true)) // ✅ Redirect to frontend
          .logout(logout -> logout.logoutSuccessUrl("/")); // ✅ Redirect to frontend home page after logout

      return http.build();
   }

   @Bean
   public CorsConfigurationSource corsConfigurationSource() {
      CorsConfiguration configuration = new CorsConfiguration();
      configuration.setAllowedOrigins(List.of("http://localhost:5173")); // ✅ Allow frontend access
      configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // ✅ Allow standard HTTP methods
      configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type")); // ✅ Allow essential headers
      configuration.setAllowCredentials(true); // ✅ Allow cookies & session-based authentication

      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", configuration); // ✅ Apply CORS settings to all endpoints
      return source;
   }
}
