package com.amorgakco.backend.group.dto;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public record GroupRegisterRequest(
        String name,
        String description,
        int groupCapacity,
        @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss") LocalDateTime beginAt,
        @DateTimeFormat(pattern = "yyyy-MM-ddTHH:mm:ss") LocalDateTime endAt,
        double latitude,
        double longitude,
        String address) {}
