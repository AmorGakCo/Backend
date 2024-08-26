package com.amorgakco.backend.global.config;

import com.amorgakco.backend.oauth2.provider.kakao.KakaoRestClient;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Bean
    public KakaoRestClient authCodeApi() {
        final RestClient restClient = RestClient.create();
        final RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        final HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return factory.createClient(KakaoRestClient.class);
    }
}
