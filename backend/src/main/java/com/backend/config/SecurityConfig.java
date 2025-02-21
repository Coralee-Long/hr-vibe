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

@Configuration
@EnableWebSecurity
public class SecurityConfig {

   // Define a constant for the lifestyle URL pattern.
   private static final String LIFESTYLE_URL_PATTERN = "/lifestyle/**";

   @Bean
   public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      http
          .cors(cors -> cors.configurationSource(corsConfigurationSource()))
          // Enable CSRF protection using a CookieCsrfTokenRepository,
          // but ignore CSRF for the /auth/logout endpoint.
          .csrf(csrf -> csrf
                    .ignoringRequestMatchers("/auth/logout")
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
               )
          .authorizeHttpRequests(auth -> auth
                                     .requestMatchers("/auth/guest").permitAll()
                                     .requestMatchers("/auth/admin").authenticated()
                                     .requestMatchers(HttpMethod.GET, LIFESTYLE_URL_PATTERN).permitAll()
                                     .requestMatchers(HttpMethod.POST, LIFESTYLE_URL_PATTERN).hasRole("ADMIN")
                                     .requestMatchers(HttpMethod.PUT, LIFESTYLE_URL_PATTERN).hasRole("ADMIN")
                                     .requestMatchers(HttpMethod.DELETE, LIFESTYLE_URL_PATTERN).hasRole("ADMIN")
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
      // Allow the CSRF header to pass through.
      configuration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "X-XSRF-TOKEN"));
      configuration.setAllowCredentials(true);
      UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
      source.registerCorsConfiguration("/**", configuration);
      return source;
   }
}
