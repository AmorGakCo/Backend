package com.amorgakco.backend.fcmtoken.service;

import com.amorgakco.backend.fcmtoken.domain.FcmToken;
import com.amorgakco.backend.fcmtoken.repository.FcmTokenRepository;
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
