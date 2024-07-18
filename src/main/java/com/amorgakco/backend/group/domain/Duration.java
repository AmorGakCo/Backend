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
    private LocalDateTime beginAt;
    private LocalDateTime endAt;

    public Duration(final LocalDateTime beginAt, final LocalDateTime endAt) {
        validate(beginAt, endAt);
        this.beginAt = beginAt;
        this.endAt = endAt;
    }

    private void validate(final LocalDateTime beginAt, final LocalDateTime endAt) {
        if (beginAt.isAfter(endAt)) {
            throw IllegalTimeException.startTimeAfterEndTime();
        }
        if (endAt.getHour() - beginAt.getHour() > 12) {
            throw IllegalTimeException.maxDuration();
        }
        if (endAt.getHour() - beginAt.getHour() < 1) {
            throw IllegalTimeException.minDuration();
        }
    }
}
