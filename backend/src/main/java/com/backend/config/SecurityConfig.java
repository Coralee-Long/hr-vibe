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
      // Create a custom CookieCsrfTokenRepository.
      CookieCsrfTokenRepository csrfRepo = CookieCsrfTokenRepository.withHttpOnlyFalse();
      // NOSONAR: The HttpOnly flag is intentionally set to false to allow client-side JavaScript access to the CSRF token.
      // This is safe because the application enforces strict XSS mitigations (e.g. CSP, input validation) and uses secure practices.
      // You could further configure this repository (e.g., setting the cookie as Secure in production) if needed:
      // csrfRepo.setCookieSecure(true);

      http
          .cors(cors -> cors.configurationSource(corsConfigurationSource()))
          .csrf(csrf -> csrf
                    .csrfTokenRepository(csrfRepo)
                    .ignoringRequestMatchers("/auth/logout") // Optionally bypass CSRF for logout
               )
          .authorizeHttpRequests(auth -> auth
                                     .requestMatchers("/auth/guest").permitAll()
                                     .requestMatchers("/auth/admin").authenticated()
                                     // Allow read (GET) requests for lifestyle factors to everyone.
                                     .requestMatchers(HttpMethod.GET, "/lifestyle/**").permitAll()
                                     // Restrict POST/PUT/DELETE requests for lifestyle factors to admin only.
                                     .requestMatchers(HttpMethod.POST, "/lifestyle/**").hasRole("ADMIN")
                                     .requestMatchers(HttpMethod.PUT, "/lifestyle/**").hasRole("ADMIN")
                                     .requestMatchers(HttpMethod.DELETE, "/lifestyle/**").hasRole("ADMIN")
                                     .anyRequest().permitAll()
                                )
          .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
          .exceptionHandling(ex -> ex.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
          .oauth2Login(oauth2 -> oauth2.defaultSuccessUrl("http://localhost:5173/dashboard", true))
          .logout(logout -> logout.logoutSuccessUrl("/"));

      return http.build();
   }

   @Bean
   public CorsConfigurationSource corsConfigurationSource() {
      CorsConfiguration configuration = new CorsConfiguration();
      configuration.setAllowedOrigins(List.of("http://localhost:5173"));
      configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
      configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
      configuration.setAllowCredentials(true);
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", configuration);
      return source;
   }
}
