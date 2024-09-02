package com.ThreeK_Project.api_server.global.security.config;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationFilterCustom authenticationFilterCustom;
    private final AuthenticationEntryPointCustom authenticationEntryPointCustom;
    private final AccessDeniedHandlerCustom accessDeniedHandlerCustom;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // JWT 인증
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(AbstractHttpConfigurer::disable)
                .addFilterBefore(authenticationFilterCustom, UsernamePasswordAuthenticationFilter.class)
                // 예외 처리 핸들러
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler(accessDeniedHandlerCustom)
                        .authenticationEntryPoint(authenticationEntryPointCustom)
                )
                .authorizeHttpRequests(authz -> authz
                        // public api
                        .requestMatchers(antMatcher( "/api/public/**")).permitAll()
                        // Swagger
                        .requestMatchers("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
