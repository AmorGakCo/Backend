package com.amorgakco.backend.fcmtoken.repository;

import com.amorgakco.backend.fcmtoken.domain.FcmToken;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FcmTokenRepository extends CrudRepository<FcmToken, String> {}
