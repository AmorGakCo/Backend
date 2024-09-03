package com.amorgakco.backend.group.service.search;

import com.google.common.geometry.S2Cell;

import java.util.List;

public interface GroupSearchStrategy {

    boolean supports();

    List<S2Cell> getCells();
}
