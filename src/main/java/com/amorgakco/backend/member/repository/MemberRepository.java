package com.amorgakco.backend.member.repository;

import com.amorgakco.backend.global.oauth.provider.Oauth2Provider;
import com.amorgakco.backend.member.domain.Member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByOauth2ProviderAndOauth2Id(Oauth2Provider provider, String id);

    @Query("select m from Member m join fetch m.roles")
    Optional<Member> findByIdWithRoles(Long memberId);
}
