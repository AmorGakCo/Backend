package com.amorgakco.backend.fcmtoken.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@AllArgsConstructor
@RedisHash(value = "fcmToken")
public class FcmToken {

    private String token;

    @Id
    private String memberId;
}
