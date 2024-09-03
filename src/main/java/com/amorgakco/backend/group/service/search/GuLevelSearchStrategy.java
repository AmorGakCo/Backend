package com.amorgakco.backend.group.service.search;

import com.amorgakco.backend.group.dto.GroupSearchRequest;

import java.util.List;

public class GuLevelSearchStrategy implements GroupSearchStrategy {

    @Override
    public boolean isValid(final double diagonalSize) {
        return false;
    }

    @Override
    public List<String> getTokens(final GroupSearchRequest request) {
        return null;
    }
}
