package com.amorgakco.backend.global.config;

import com.amorgakco.backend.oauth2.converter.Oauth2ProviderConverter;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class ConverterConfig implements WebMvcConfigurer {

    private final Oauth2ProviderConverter converter;

    @Override
    public void addFormatters(final FormatterRegistry registry) {
        registry.addConverter(converter);
    }
}
