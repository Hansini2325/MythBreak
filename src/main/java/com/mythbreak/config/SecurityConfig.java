package com.mythbreak.config;

import com.mythbreak.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * Spring Security configuration.
 * - Stateless JWT-based authentication.
 * - Role-based endpoint authorization.
 * - BCrypt password encoding.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    // ── Public REST endpoints ──────────────────────────────────────
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/courses/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/skills/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/opportunities/**").permitAll()

                    // ── All static frontend resources (HTML, CSS, JS, images) ──────
                    // Using /** so ANY static file is served without authentication.
                    // Role enforcement is handled client-side (auth.js requireRole).
                    .requestMatchers(
                            "/", "/*.html", "/css/**", "/js/**",
                            "/images/**", "/fonts/**", "/favicon.ico",
                            "/error"
                    ).permitAll()

                    // ── Role-protected API endpoints ──────────────────────────────
                    .requestMatchers("/api/learner/**").hasRole("LEARNER")
                    .requestMatchers("/api/earner/**").hasRole("EARNER")
                    .requestMatchers("/api/educator/**").hasRole("EDUCATOR")
                    .requestMatchers("/api/company/**").hasRole("COMPANY")
                    .requestMatchers("/api/admin/**").hasRole("ADMIN")
                    // Matching endpoint — earners and companies both need it
                    .requestMatchers("/api/opportunities/*/matches").hasAnyRole("EARNER", "COMPANY", "ADMIN")

                    // ── Everything else requires authentication ────────────────────
                    .anyRequest().authenticated()
            )
            // Return 401 JSON for unauthenticated API calls (not 403 or redirect)
            .exceptionHandling(ex -> ex
                    .authenticationEntryPoint((request, response, authException) -> {
                        response.setContentType("application/json");
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getWriter().write(
                                "{\"error\":\"Unauthorized\",\"message\":\"" +
                                authException.getMessage() + "\"}"
                        );
                    })
                    .accessDeniedHandler((request, response, accessDeniedException) -> {
                        response.setContentType("application/json");
                        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                        response.getWriter().write(
                                "{\"error\":\"Forbidden\",\"message\":\"You do not have permission to access this resource.\"}"
                        );
                    })
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
