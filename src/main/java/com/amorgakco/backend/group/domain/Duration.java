package com.amorgakco.backend.group.domain;

import jakarta.persistence.Embeddable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Duration {
    private LocalDateTime beginAt;
    private LocalDateTime endAt;

    public Duration(final LocalDateTime beginAt, final LocalDateTime endAt) {
        this.beginAt = beginAt;
        this.endAt = endAt;
    }
}
