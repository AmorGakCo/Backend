package com.amorgakco.backend.member.repository;

import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.domain.Oauth2ProviderType;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query(
        "select m from Member m join fetch m.roleNames where m.oauth2ProviderType =:provider and "
            + "m.oauth2Id =:id")
    @Lock(LockModeType.PESSIMISTIC_READ)
    Optional<Member> findByOauth2ProviderAndOauth2Id(Oauth2ProviderType provider, String id);

    @Query("select m from Member m join fetch m.roleNames where m.id =:memberId")
    Optional<Member> findByIdWithRoles(Long memberId);

    Optional<Member> findByNickname(String nickname);
}
