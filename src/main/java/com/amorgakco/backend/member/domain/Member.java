package com.amorgakco.backend.member.domain;

import jakarta.persistence.*;

import lombok.*;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String refreshToken;
    private String provider;
    private String identifier;
    private String imgUrl;
    private String nickname;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
    private List<RoleEntity> role;

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

    public void updateRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
