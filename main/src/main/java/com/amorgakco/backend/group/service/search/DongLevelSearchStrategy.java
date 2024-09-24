package com.amorgakco.backend.group.service.search;

import com.amorgakco.backend.group.dto.GroupSearchRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DongLevelSearchStrategy extends GroupSearchStrategy {

    @Override
    public boolean isValid(final double diagonalSize) {
        return diagonalSize <= DiagonalDistanceConst.MIN_DISTANCE.getValue();
    }

    @Override
    public List<String> selectCellTokens(final GroupSearchRequest request) {
        return getCoveringCells(request);
    }
}
