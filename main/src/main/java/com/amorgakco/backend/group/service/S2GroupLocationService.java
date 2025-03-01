package com.amorgakco.backend.group.service;

import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.dto.GroupSearchRequest;
import com.amorgakco.backend.group.dto.GroupSearchResponse;
import com.amorgakco.backend.group.repository.GroupRepository;
import com.amorgakco.backend.group.service.mapper.GroupMapper;
import com.amorgakco.backend.group.service.search.S2CellSearch;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class S2GroupLocationService implements GroupLocationService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final S2CellSearch s2CellSearch;

    public GroupSearchResponse findGroups(final GroupSearchRequest request) {
        final List<String> cellTokens = s2CellSearch.getCellTokens(request);
        return groupRepository.findByCellToken(cellTokens)
            .stream()
            .filter(Group::isActivatedGroup)
            .map(groupMapper::toGroupLocation)
            .collect(
                Collectors.collectingAndThen(
                    Collectors.toList(), GroupSearchResponse::new));
    }
}
