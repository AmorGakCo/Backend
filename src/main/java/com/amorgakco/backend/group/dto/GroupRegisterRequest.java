package com.amorgakco.backend.group.dto;

import java.time.LocalDateTime;

public record GroupRegisterRequest(
        String name,
        String description,
        int memberCapacity,
        LocalDateTime beginTime,
        LocalDateTime endTime,
        double latitude,
        double longitude) {}
