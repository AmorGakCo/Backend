package com.amorgakco.backend.member.domain;

import com.amorgakco.backend.global.oauth.provider.Oauth2Provider;

import jakarta.persistence.*;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Oauth2Provider oauth2Provider;

    private String oauth2Id;
    private String imgUrl;
    private String nickname;
    private String refreshToken;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private List<RoleEntity> roles = new ArrayList<>();

    @Builder
    public Member(
            final Oauth2Provider oauth2Provider,
            final String oauth2Id,
            final String imgUrl,
            final String nickname,
            final List<RoleEntity> roles) {
        this.oauth2Provider = oauth2Provider;
        this.oauth2Id = oauth2Id;
        this.imgUrl = imgUrl;
        this.nickname = nickname;
        this.roles = roles;
    }

    public void updateRefreshToken(final String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
