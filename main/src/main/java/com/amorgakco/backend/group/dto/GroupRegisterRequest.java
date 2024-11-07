package com.amorgakco.backend.group.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
public record GroupRegisterRequest(
    String name,
    String description,
    int groupCapacity,
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss") LocalDateTime beginAt,
    @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss") LocalDateTime endAt,
    double latitude,
    double longitude,
    String address) {

}
