package com.amorgakco.backend.participant.dto;

import org.hibernate.validator.constraints.Range;

public record TardinessRequest(
        @Range(min = 5, max = 60, message = "5분 이상 60분 이하만 입력해주세요.") Integer minute) {
}
