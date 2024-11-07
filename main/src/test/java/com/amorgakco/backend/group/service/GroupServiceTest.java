package com.amorgakco.backend.group.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.amorgakco.backend.fixture.group.TestGroupFactory;
import com.amorgakco.backend.fixture.member.TestMemberFactory;
import com.amorgakco.backend.global.exception.ResourceNotFoundException;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.dto.GroupRegisterRequest;
import com.amorgakco.backend.group.dto.GroupRegisterResponse;
import com.amorgakco.backend.group.repository.GroupRepository;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.repository.MemberRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class GroupServiceTest {

    @Autowired
    private GroupService groupService;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("사용자는 그룹을 등록할 수 있다.")
    void register() {
        // given
        final int duration = 5;
        final Member host = TestMemberFactory.createEntity();
        memberRepository.save(host);
        final GroupRegisterRequest groupRegisterRequest =
            TestGroupFactory.groupRegisterRequest(LocalDateTime.now(),
                LocalDateTime.now().plusHours(duration));
        // when
        final GroupRegisterResponse groupRegisterResponse = groupService.register(
            groupRegisterRequest, host);
        // then
        final Group group = groupRepository.findById(groupRegisterResponse.groupId()).get();
        assertThat(group.getHost().getId()).isEqualTo(host.getId());
    }

    @Test
    @DisplayName("호스트는 그룹을 삭제할 수 있다.")
    void deleteGroup() {
        // given
        final Member host = TestMemberFactory.createEntity();
        final Group group = TestGroupFactory.createActiveGroup(host);
        memberRepository.save(host);
        groupRepository.save(group);
        // when
        groupService.delete(host, group.getId());
        // then
        assertThatThrownBy(() -> groupService.getGroup(group.getId()))
            .isInstanceOf(ResourceNotFoundException.class);
    }

}