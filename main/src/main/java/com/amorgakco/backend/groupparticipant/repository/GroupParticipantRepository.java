package com.amorgakco.backend.groupparticipant.repository;

import com.amorgakco.backend.groupparticipant.domain.GroupParticipant;
import com.amorgakco.backend.member.domain.Member;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupParticipantRepository extends JpaRepository<GroupParticipant, Long> {

    @Query("select p from GroupParticipant p join fetch p.group where p.member.id = :memberId and"
        + " p.group.duration.endAt >= :now")
    Slice<GroupParticipant> findCurrentParticipationByMember(Long memberId, LocalDateTime now,
        Pageable pageable);

    @Query("select p from GroupParticipant p join fetch p.group where p.member.id = :memberId and"
        + " p.group.duration.endAt < :now")
    Slice<GroupParticipant> findPastParticipationByMember(Long memberId, LocalDateTime now,
        Pageable pageable);

    @Query(
        "select p from GroupParticipant p join fetch p.group g join fetch g.host join fetch p.member where p.group.id"
            + " = :groupId and p.member.id = :memberId")
    Optional<GroupParticipant> findByGroupAndMember(final Long groupId, final Long memberId);

    @Query("select count(*) from GroupParticipant p where p.member = :member and p.group.duration"
        + ".endAt > :now")
    Integer countCurrentParticipationByMember(Member member, LocalDateTime now);
}
