package com.amorgakco.backend.member.domain;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Member {
    @Builder
    public Member(
            final String provider,
            final String identifier,
            final String imgUrl,
            final String nickname,
            final Role role) {
        this.provider = provider;
        this.identifier = identifier;
        this.imgUrl = imgUrl;
        this.nickname = nickname;
        this.role = role;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String refreshToken;
    private String provider;
    private String identifier;
    private String imgUrl;
    private String nickname;
    private Role role;

    public void updateRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
