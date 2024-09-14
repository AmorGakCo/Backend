package com.amorgakco.backend.participant.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record TardinessRequest(
        @Min(5) @Max(60) Integer minute) {}
