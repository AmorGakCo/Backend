package com.amorgakco.backend.global;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum GoogleS2Const {
    S2_CELL_LEVEL(14);

    private final Integer value;
}
