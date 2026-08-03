package com.hospital.config;

import com.hospital.security.CustomUserDetailsService;
import com.hospital.security.JwtAuthenticationFilter;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {


    private final JwtAuthenticationFilter jwtAuthFilter;

    private final CustomUserDetailsService userDetailsService;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {


        http

                // ⭐ हे ADD करा
                .cors(cors ->
                        cors.configurationSource(corsConfigurationSource())
                )

                .csrf(AbstractHttpConfigurer::disable)


                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )


                .authorizeHttpRequests(auth -> auth

                        // ==========================
                        // Public APIs
                        // ==========================
                        .requestMatchers(
                                "/api/auth/login",
                                "/api/auth/register/patient",

                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**"
                        ).permitAll()



                        .requestMatchers(
                                "/api/receptionists/consultation-receipt/**"
                        ).hasAnyRole("PATIENT", "ADMIN", "RECEPTIONIST")

                        .requestMatchers(
                                "/api/pharmacy/receipt/**"
                        ).hasAnyRole("PATIENT", "ADMIN", "RECEPTIONIST")

                        .requestMatchers(
                                "/api/lab-technicians/receipt/**"
                        ).hasAnyRole("PATIENT", "ADMIN", "RECEPTIONIST")


                                // ==========================
// Doctor APIs
// ==========================


                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/api/doctors",
                                        "/api/doctors/**"
                                )
                                .hasAnyRole(
                                        "PATIENT",
                                        "ADMIN",
                                        "DOCTOR",
                                        "RECEPTIONIST"
                                )
//

                        // ==========================
                        // Admin Only
                        // ==========================

                        .requestMatchers(
                                "/api/auth/register/doctor",
                                "/api/auth/register/nurse",
                                "/api/auth/register/receptionist",
                                "/api/nurses",
                                "/api/receptionists",
                                "/api/lab-technicians"
                        ).hasRole("ADMIN")

                        // ==========================
                        // Receptionist
                        // ==========================

                        .requestMatchers("/api/receptionists/**")
                        .hasRole("RECEPTIONIST")

                        // ==========================
                        // Pharmacist
                        // ==========================

                        .requestMatchers("/api/pharmacy/**")
                        .hasRole("PHARMACIST")

                        // ==========================
                        // Lab Technician
                        // ==========================

                        .requestMatchers("/api/lab-technicians/**")
                        .hasRole("LAB_TECHNICIAN")

                        // ==========================
                        // Nurse
                        // ==========================

                        .requestMatchers("/api/nurses/**")
                        .authenticated()

                        .anyRequest()
                        .authenticated()
                )



                .authenticationProvider(authenticationProvider())

                .addFilterBefore(
                        jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter.class
                );


        return http.build();
    }





    @Bean
    public CorsConfigurationSource corsConfigurationSource() {


        CorsConfiguration configuration =
                new CorsConfiguration();


        configuration.setAllowedOrigins(
                List.of(
                        "http://localhost:5173"
                )
        );


        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "PATCH",
                        "OPTIONS"
                )
        );


        configuration.setAllowedHeaders(
                List.of(
                        "*"
                )
        );


        configuration.setAllowCredentials(true);



        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();


        source.registerCorsConfiguration(
                "/**",
                configuration
        );


        return source;
    }





    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }





    @Bean
    public AuthenticationProvider authenticationProvider() {


        DaoAuthenticationProvider authProvider =
                new DaoAuthenticationProvider(
                        userDetailsService
                );


        authProvider.setPasswordEncoder(
                passwordEncoder()
        );


        return authProvider;
    }





    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    )
            throws Exception {


        return config.getAuthenticationManager();

    }

}