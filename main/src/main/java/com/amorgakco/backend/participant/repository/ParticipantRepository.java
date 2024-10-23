package com.amorgakco.backend.participant.repository;

import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.participant.domain.Participant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    @Query("select p from Participant p join fetch p.group where p.member.id = :memberId")
    Slice<Participant> findByMember(Long memberId, Pageable pageable);

    @Query(
            "select p from Participant p join fetch p.group join fetch p.member where p.group.id = :groupId and p.member.id = :memberId")
    Optional<Participant> findByGroupAndMember(final Long groupId, final Long memberId);

    @Query("select count(*) from Participant p where p.member = :member")
    Integer countByParticipantMember(Member member);
}
