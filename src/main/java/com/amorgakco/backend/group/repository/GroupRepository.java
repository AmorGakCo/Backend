package com.amorgakco.backend.group.repository;

import com.amorgakco.backend.group.domain.Group;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    @Query("select g from Group g join fetch g.host where g.id = :groupId ")
    Optional<Group> findByIdWithHost(final Long groupId);
}
