package com.amorgakco.backend.global.oauth.provider;

import com.amorgakco.backend.global.oauth.exception.InvalidOauth2ProviderException;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Oauth2Selector {
    private final List<Oauth2Mapper> oauth2UserMappers;

    public Oauth2Mapper getOauth2Mapper(final String oauth2Provider) {
        return oauth2UserMappers.stream()
                .filter(provider -> provider.isEqualsTo(oauth2Provider))
                .findFirst()
                .orElseThrow(InvalidOauth2ProviderException::new);
    }
}
