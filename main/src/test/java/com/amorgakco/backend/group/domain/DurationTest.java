package com.amorgakco.backend.group.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.amorgakco.backend.global.exception.IllegalTimeException;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DurationTest {

    @Test
    @DisplayName("모임은 지속시간을 1-8시간 사이로 생성할 수 있다.")
    void duration() {
        // given
        final LocalDateTime beginAt = LocalDateTime.now();
        final LocalDateTime endAt = beginAt.plusHours(5);
        // when & then
        final Duration duration = new Duration(beginAt, endAt);
    }

    @Test
    @DisplayName("모임 지속 시간은 1시간 이하일 수 없다.")
    void validateMinDuration() {
        // given
        final LocalDateTime beginAt = LocalDateTime.now();
        final LocalDateTime endAt = beginAt.plusMinutes(30);
        // when&then
        assertThatThrownBy(() -> new Duration(beginAt, endAt))
            .isInstanceOf(IllegalTimeException.class);
    }

    @Test
    @DisplayName("모임 지속 시간은 8시간 이상일 수 없다.")
    void validateMaxDuration() {
        // given
        final LocalDateTime beginAt = LocalDateTime.now();
        final LocalDateTime endAt = beginAt.plusHours(9);
        // when&then
        assertThatThrownBy(() -> new Duration(beginAt, endAt))
            .isInstanceOf(IllegalTimeException.class);
    }
}
