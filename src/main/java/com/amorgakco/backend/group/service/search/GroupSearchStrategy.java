package com.amorgakco.backend.group.service.search;

import com.amorgakco.backend.group.dto.GroupSearchRequest;

import java.util.List;

public abstract class GroupSearchStrategy {
    abstract boolean isValid(double diagonalSize);

    abstract List<String> getTokens(GroupSearchRequest request);
}
