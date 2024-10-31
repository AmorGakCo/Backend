package com.amorgakco.backend.groupapplication.repository;

import com.amorgakco.backend.group.domain.Group;
import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.groupapplication.domain.GroupApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupApplicationRepository extends JpaRepository<GroupApplication, Long> {
    @Query(
            "select gp from GroupApplication gp join fetch gp.group where gp.group.id = :groupId and gp.participant.id =:memberId")
    Optional<GroupApplication> findByGroupIdAndMemberId(
            @Param("groupId") Long groupId, @Param("memberId") Long memberId);

    boolean existsByGroupAndParticipant(Group group, Member member);
}
