package com.amorgakco.backend.groupparticipation.repository;

import com.amorgakco.backend.groupparticipation.domain.GroupParticipation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupParticipationRepository extends JpaRepository<GroupParticipation, Long> {}
