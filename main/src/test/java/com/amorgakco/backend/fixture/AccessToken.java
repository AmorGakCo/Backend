package com.amorgakco.backend.fixture;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AccessToken {
    HEADER("Authorization"),
    BEARER_WITH_TOKEN("Bearer ajefojaekf.aoiejfoaiwjefo.J68TfTiGw");
    private final String value;
}
