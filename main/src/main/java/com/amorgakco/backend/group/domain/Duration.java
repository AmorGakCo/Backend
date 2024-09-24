package com.amorgakco.backend.group.domain;

import com.amorgakco.backend.global.exception.IllegalTimeException;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Duration {
    private static final int MAX_DURATION = 7;
    private static final int MIN_DURATION = 1;
    private LocalDateTime beginAt;
    private LocalDateTime endAt;

    public Duration(final LocalDateTime beginAt, final LocalDateTime endAt) {
        validate(beginAt, endAt);
        this.beginAt = beginAt;
        this.endAt = endAt;
    }

    private void validate(final LocalDateTime beginAt, final LocalDateTime endAt) {
        final long hours = java.time.Duration.between(beginAt, endAt).toHours();
        if (beginAt.isAfter(endAt)) {
            throw IllegalTimeException.startTimeAfterEndTime();
        }
        if (hours > MAX_DURATION) {
            throw IllegalTimeException.maxDuration();
        }
        if (hours < MIN_DURATION) {
            throw IllegalTimeException.minDuration();
        }
    }
}
