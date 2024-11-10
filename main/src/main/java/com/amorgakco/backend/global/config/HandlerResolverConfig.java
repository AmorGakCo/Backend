package com.amorgakco.backend.global.config;

import com.amorgakco.backend.global.argumentresolver.AuthMemberArgumentResolver;
import com.amorgakco.backend.global.argumentresolver.AuthMemberIdArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class HandlerResolverConfig implements WebMvcConfigurer {

    private final AuthMemberIdArgumentResolver authMemberIdArgumentResolver;
    private final AuthMemberArgumentResolver authMemberArgumentResolver;

    @Override
    public void addArgumentResolvers(final List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authMemberIdArgumentResolver);
        resolvers.add(authMemberArgumentResolver);
    }
}
