package com.amorgakco.backend.oauth2.converter;

import com.amorgakco.backend.global.exception.InvalidOauth2ProviderException;
import com.amorgakco.backend.member.domain.Oauth2ProviderType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class Oauth2ProviderConverter implements Converter<String, Oauth2ProviderType> {
    @Override
    public Oauth2ProviderType convert(final String source) {
        try {
            return Oauth2ProviderType.valueOf(source.toUpperCase(Locale.ROOT));
        } catch (final IllegalArgumentException e) {
            throw InvalidOauth2ProviderException.invalidOauth2Provider();
        }
    }
}
