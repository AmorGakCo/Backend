package com.amorgakco.backend.groupjoining.service;

import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.service.GroupService;
import com.amorgakco.backend.groupjoining.domain.GroupJoining;
import com.amorgakco.backend.groupjoining.repository.GroupJoiningRepository;
import com.amorgakco.backend.groupjoining.service.mapper.GroupJoiningMapper;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.sms.SmsSender;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GroupJoiningService {

    private final GroupJoiningRepository groupJoiningRepository;
    private final GroupService groupService;
    private final GroupJoiningMapper groupJoiningMapper;
    private final SmsSender smsSender;

    @Transactional
    public void requestJoin(final Long groupId, final Member newParticipant) {
        final Group group = groupService.getGroup(groupId);
        group.validateParticipation(newParticipant.getId());
        final GroupJoining groupJoining = groupJoiningMapper.toGroupJoining(group, newParticipant);
        groupJoiningRepository.save(groupJoining);
        smsSender.send();
    }
}
