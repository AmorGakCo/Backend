package com.amorgakco.backend.member.repository;

import com.amorgakco.backend.member.domain.Member;
import com.amorgakco.backend.member.domain.Oauth2ProviderType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query(
            "select m from Member m join fetch m.roleNames where m.oauth2ProviderType =:provider and m.oauth2Id =:id")
    Optional<Member> findByOauth2ProviderAndOauth2Id(Oauth2ProviderType provider, String id);

    @Query("select m from Member m join fetch m.roleNames where m.id =:memberId")
    Optional<Member> findByIdWithRoles(Long memberId);
}
