package com.amorgakco.backend.oauth2.provider;

import com.amorgakco.backend.global.exception.InvalidOauth2ProviderException;
import com.amorgakco.backend.member.domain.Oauth2ProviderType;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class Oauth2ProviderComposite {
    private final Map<Oauth2ProviderType, Oauth2ProviderService> urlProviders;

    public Oauth2ProviderComposite(final Set<Oauth2ProviderService> urlProviders) {
        this.urlProviders =
                urlProviders.stream()
                        .collect(
                                Collectors.toMap(
                                        Oauth2ProviderService::getOauth2ProviderType,
                                        Function.identity()));
    }

    public Oauth2ProviderService getProvider(final Oauth2ProviderType oauth2ProviderType) {
        return Optional.ofNullable(urlProviders.get(oauth2ProviderType))
                .orElseThrow(InvalidOauth2ProviderException::invalidOauth2Provider);
    }
}