package com.amorgakco.backend.member.repository;

import com.amorgakco.backend.member.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByProviderAndIdentifier(String provider, String identifier);

    Optional<Account> findByProviderAndIdentifier(String provider, String identifier);
}
