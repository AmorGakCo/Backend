package com.amorgakco.backend.group.service;

import com.amorgakco.backend.global.IdResponse;
import com.amorgakco.backend.global.exception.IllegalAccessException;
import com.amorgakco.backend.global.exception.ResourceNotFoundException;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.dto.GroupBasicResponse;
import com.amorgakco.backend.group.dto.GroupDetailResponse;
import com.amorgakco.backend.group.dto.GroupRegisterRequest;
import com.amorgakco.backend.group.repository.GroupRepository;
import com.amorgakco.backend.group.service.mapper.GroupMapper;
import com.amorgakco.backend.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class GroupService {
    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;

    @Transactional
    public IdResponse register(final GroupRegisterRequest request, final Member host) {
        final Group group = groupMapper.toGroup(host, request);
        final Long groupId = groupRepository.save(group).getId();
        return new IdResponse(groupId);
    }

    @Transactional
    public void delete(final Long hostId, final Long groupId) {
        final Group group = getGroup(groupId);
        if (group.isNotGroupHost(hostId)) {
            throw IllegalAccessException.noAuthorityForGroup();
        }
        groupRepository.delete(group);
    }

    public Group getGroup(final Long groupId) {
        return groupRepository
                .findById(groupId)
                .orElseThrow(ResourceNotFoundException::groupNotFound);
    }

    public GroupDetailResponse getDetailGroup(final Long groupId) {
        final Group group = getGroup(groupId);
        return groupMapper.toGroupDetailResponse(group);
    }

    public GroupBasicResponse getBasicGroup(final Long groupId) {
        final Group group =
                groupRepository
                        .findByIdWithHost(groupId)
                        .orElseThrow(ResourceNotFoundException::groupNotFound);
        return groupMapper.toGroupBasicInfoResponse(group);
    }
}
