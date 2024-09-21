package com.amorgakco.backend.participant.repository;

import com.amorgakco.backend.participant.domain.Participant;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    @Query("select p from Participant p join fetch p.group where p.member.id = :memberId")
    Slice<Participant> findByMember(Long memberId, Pageable pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(
            "select p from Participant p join fetch p.group where p.group.id = :groupId and p.member.id = :memberId")
    Optional<Participant> findByGroupAndMember(final Long groupId, final Long memberId);
}
