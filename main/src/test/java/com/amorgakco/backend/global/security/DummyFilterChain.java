package com.amorgakco.backend.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

public class DummyFilterChain implements FilterChain {

    @Override
    public void doFilter(
        final ServletRequest servletRequest, final ServletResponse servletResponse) {
        // do nothing
    }
}
