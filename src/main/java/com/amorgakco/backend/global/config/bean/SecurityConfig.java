package com.amorgakco.backend.global.config.bean;

import com.amorgakco.backend.global.config.security.JwtAccessDeniedHandler;
import com.amorgakco.backend.global.config.security.JwtAuthenticationEntryPoint;
import com.amorgakco.backend.global.config.security.JwtAuthenticationFilter;
import com.amorgakco.backend.global.oauth.handler.Oauth2SuccessHandler;
import com.amorgakco.backend.global.oauth.service.Oauth2UserService;

import io.jsonwebtoken.security.Keys;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final Oauth2UserService oauth2UserService;
    private final Oauth2SuccessHandler oauth2SuccessHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request ->
                                request.requestMatchers("/", "/logout", "/token")
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
                .exceptionHandling(
                        e ->
                                e.authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                        .accessDeniedHandler(jwtAccessDeniedHandler))
                .build();
    }

    @Bean
    public SecretKey secretKey(final @Value("${jwt.secret-key}") String jwtSign) {
        return Keys.hmacShaKeyFor(jwtSign.getBytes(StandardCharsets.UTF_8));
    }
}
