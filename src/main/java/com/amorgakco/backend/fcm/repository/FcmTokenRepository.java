package com.amorgakco.backend.fcm.repository;

import com.amorgakco.backend.fcm.domain.FcmToken;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FcmTokenRepository extends CrudRepository<FcmToken, String> {}
