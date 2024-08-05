package com.amorgakco.backend.global.config;

import com.amorgakco.backend.global.oauth.handler.Oauth2SuccessHandler;
import com.amorgakco.backend.global.oauth.service.Oauth2UserService;
import com.amorgakco.backend.global.security.JwtAccessDeniedHandler;
import com.amorgakco.backend.global.security.JwtAuthenticationEntryPoint;
import com.amorgakco.backend.global.security.JwtAuthenticationFilter;
import com.amorgakco.backend.global.security.JwtExceptionHandlingFilter;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final Oauth2UserService oauth2UserService;
    private final Oauth2SuccessHandler oauth2SuccessHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtExceptionHandlingFilter jwtExceptionHandlingFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request ->
                                request.requestMatchers(
                                                "/logout",
                                                "/token",
                                                "/favicon.ico",
                                                "/errors",
                                                "/groups/locations",
                                                "/groups/**/joining")
                                        .permitAll()
                                        .anyRequest()
                                        .authenticated())
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2Login(
                        oauth ->
                                oauth.userInfoEndpoint(c -> c.userService(oauth2UserService))
                                        .successHandler(oauth2SuccessHandler))
                .addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtExceptionHandlingFilter, JwtAuthenticationFilter.class)
                .exceptionHandling(
                        e ->
                                e.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                        .accessDeniedHandler(jwtAccessDeniedHandler))
                .build();
    }
}
