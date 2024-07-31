package com.amorgakco.backend.groupjoining.repository;

import com.amorgakco.backend.groupjoining.domain.GroupJoining;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupJoiningRepository extends JpaRepository<GroupJoining, Long> {}
