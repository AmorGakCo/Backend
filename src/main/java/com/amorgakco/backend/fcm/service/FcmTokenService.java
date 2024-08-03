package com.amorgakco.backend.fcm.service;

import com.amorgakco.backend.fcm.domain.FcmToken;
import com.amorgakco.backend.fcm.repository.FcmTokenRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FcmTokenService {

    private final FcmTokenRepository fcmTokenRepository;

    @Transactional
    public void save(final String notificationToken, final Long memberId) {
        final FcmToken token = new FcmToken(notificationToken, memberId.toString());
        fcmTokenRepository.save(token);
    }
}
