package com.amorgakco.backend.fixture.group;

import com.amorgakco.backend.group.domain.Duration;

import java.time.LocalDateTime;

public class TestDurationFactory {

    public static Duration create(final int hours) {
        return new Duration(LocalDateTime.now(), LocalDateTime.now().plusHours(hours));
    }
}
