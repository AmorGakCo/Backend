package com.amorgakco.backend.jwt.repository;

import com.amorgakco.backend.jwt.domain.RefreshToken;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {}
