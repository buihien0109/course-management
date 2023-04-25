package com.example.coursemanagement.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true
)
public class SecurityConfig {
    private final CustomFilter customFilter;
    private final AuthenticationProvider authenticationProvider;
    private final CustomAccessDenied customAccessDenied;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String[] PUBLIC_ROUTE = {
                "/khoa-hoc/**",
                "/css/**",
                "/js/**",
                "/image/**",
                "/admin/login",
                "/api/admin/login"
        };
        http
                .csrf().disable()
                .authorizeHttpRequests()
                    .requestMatchers(PUBLIC_ROUTE).permitAll()
                    .requestMatchers("GET", "/api/files/**").permitAll()
                    .requestMatchers("POST", "/api/files").hasAnyRole("ADMIN", "SALE")
                    .requestMatchers(
                            "/admin/courses/**",
                            "/api/admin/courses/**",
                            "/admin/topics/**",
                            "/api/admin/topics/**"
                    ).hasAnyRole("ADMIN", "SALE")
                    .requestMatchers(
                            "/admin/users/**",
                            "/api/admin/users/**"
                    ).hasRole("ADMIN")
                    .anyRequest().permitAll()
                .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(customAuthenticationEntryPoint)
                    .accessDeniedHandler(customAccessDenied)
                .and()
                    .logout()
                    .logoutUrl("/api/admin/logout")
                    .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                    .permitAll()
                .and()
                    .authenticationProvider(authenticationProvider)
                    .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
