package com.amorgakco.backend.global.config;

import com.amorgakco.backend.oauth.handler.Oauth2SuccessHandler;
import com.amorgakco.backend.oauth.jwt.filter.JwtAuthenticationFilter;
import com.amorgakco.backend.oauth.service.Oauth2UserService;

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
@EnableWebSecurity(debug = true)
public class SecurityConfig {

    private final Oauth2UserService oauth2UserService;
    private final Oauth2SuccessHandler oauth2SuccessHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        request ->
                                request.requestMatchers("/", "/token", "/login")
                                        .anonymous()
                                        .anyRequest()
                                        .authenticated())
                .oauth2Login(
                        oauth ->
                                oauth.userInfoEndpoint(c -> c.userService(oauth2UserService))
                                        .successHandler(oauth2SuccessHandler))
                .addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
