package com.amorgakco.backend.group.service.search;

import com.amorgakco.backend.group.dto.GroupSearchRequest;
import com.google.common.geometry.S2Cell;

import java.util.List;

public class DongLevelGroupSearch implements GroupSearchStrategy {

    private static int MAX_CELL_SIZE = 10;

    @Override
    public boolean supports(final GroupSearchRequest request) {}

    @Override
    public List<S2Cell> getCells() {
        return null;
    }
}
