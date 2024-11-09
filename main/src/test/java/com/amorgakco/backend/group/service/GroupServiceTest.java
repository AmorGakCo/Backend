package com.amorgakco.backend.group.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.amorgakco.backend.fixture.group.TestGroupFactory;
import com.amorgakco.backend.fixture.member.TestMemberFactory;
import com.amorgakco.backend.global.exception.ResourceNotFoundException;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.group.dto.GroupDetailResponse;
import com.amorgakco.backend.group.dto.GroupMember;
import com.amorgakco.backend.group.dto.GroupRegisterRequest;
import com.amorgakco.backend.group.dto.GroupRegisterResponse;
import com.amorgakco.backend.group.repository.GroupRepository;
import com.amorgakco.backend.groupparticipant.domain.GroupParticipant;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.repository.MemberRepository;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
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

    @Test
    @DisplayName("")
    void test() {
        // given
        Member member = TestMemberFactory.createEntity();
        memberRepository.save(member);
        Long groupId = saveMemberAndGroup();
        // when
        GroupDetailResponse detailGroup = groupService.getDetailGroup(groupId, member.getId());
        List<GroupMember> groupMembers = detailGroup.groupMembers();
        for (GroupMember groupMember : groupMembers) {
            System.out.println("groupMember = " + groupMember.memberId());
        }
        // then
    }

    private Long saveMemberAndGroup() {
        Member host = TestMemberFactory.createEntity();
        Member member1 = TestMemberFactory.createEntity();
        Member member2 = TestMemberFactory.createEntity();
        Member member3 = TestMemberFactory.createEntity();
        memberRepository.save(host);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        Group group = TestGroupFactory.createActiveGroup(host);
        group.addParticipant(new GroupParticipant(member1));
        group.addParticipant(new GroupParticipant(member2));
        group.addParticipant(new GroupParticipant(member3));
        groupRepository.save(group);
        return group.getId();
    }
}