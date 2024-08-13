package com.amorgakco.backend.groupparticipation.repository;

import com.amorgakco.backend.groupparticipation.domain.GroupParticipation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupParticipationRepository extends JpaRepository<GroupParticipation, Long> {
    @Query(
            "select gp from GroupParticipation gp join fetch gp.group where gp.group.id = :groupId and gp.participant.id =:memberId")
    Optional<GroupParticipation> findByGroupIdAndMemberId(
            @Param("groupId") Long groupId, @Param("memberId") Long memberId);
}
