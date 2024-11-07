package com.amorgakco.backend.group.repository;

import com.amorgakco.backend.fixture.group.TestGroupFactory;
import com.amorgakco.backend.fixture.member.TestMemberFactory;
import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.groupparticipant.domain.GroupParticipant;
import com.amorgakco.backend.member.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@Transactional
class GroupRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("")
    void test() {
        // given
        Long groupId = saveMemberAndGroup();
        // when
        entityManager.clear();
        Group group = entityManager.find(Group.class, groupId.toString());
        Set<GroupParticipant> groupParticipants = group.getGroupParticipants();
        for (GroupParticipant groupParticipant : groupParticipants) {
            System.out.println(
                "groupParticipant = " + groupParticipant.getLocationVerificationStatus());
        }
        // then
    }

    private Long saveMemberAndGroup() {
        Member host = TestMemberFactory.createEntity();
        Member member1 = TestMemberFactory.createEntity();
        Member member2 = TestMemberFactory.createEntity();
        Member member3 = TestMemberFactory.createEntity();
        entityManager.persist(host);
        entityManager.persist(member1);
        entityManager.persist(member2);
        entityManager.persist(member3);
        Group group = TestGroupFactory.createActiveGroup(host);
        group.addParticipant(new GroupParticipant(member1));
        group.addParticipant(new GroupParticipant(member2));
        group.addParticipant(new GroupParticipant(member3));
        entityManager.persist(group);
        return group.getId();
    }

}