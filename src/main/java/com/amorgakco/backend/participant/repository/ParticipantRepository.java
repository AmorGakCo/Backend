package com.amorgakco.backend.participant.repository;

import com.amorgakco.backend.group.domain.Participant;
import com.amorgakco.backend.member.domain.Member;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    @Query("select p from Participant p join fetch p.group where p.member = :member")
    Slice<Participant> findByMember(Member member, Pageable pageable);

    @Query(
            "select p from Participant p join fetch p.group where p.group.id = :groupId and p.member.id = :memberId")
    Optional<Participant> findByGroupAndMember(final Long groupId, final Long memberId);
}
