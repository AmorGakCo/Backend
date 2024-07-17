package com.amorgakco.backend.member.domain;

import com.amorgakco.backend.global.BaseTime;
import com.amorgakco.backend.global.oauth.provider.Oauth2Provider;

import jakarta.persistence.*;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTime {
    @Id @GeneratedValue private Long id;

    @Enumerated(EnumType.STRING)
    private Oauth2Provider oauth2Provider;

    private String oauth2Id;
    private String imgUrl;
    private String nickname;
    private Integer point;
    private String githubUrl;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private List<Roles> roleNames = new ArrayList<>();

    @Builder
    public Member(
            final Oauth2Provider oauth2Provider,
            final String oauth2Id,
            final String imgUrl,
            final String nickname) {
        this.oauth2Provider = oauth2Provider;
        this.oauth2Id = oauth2Id;
        this.imgUrl = imgUrl;
        this.nickname = nickname;
        this.roleNames.add(new Roles(Role.ROLE_MEMBER));
        this.point = 0;
        this.githubUrl = "";
    }

    public void updateNicknameAndImgUrl(final String nickname, final String imgUrl) {
        this.nickname = nickname;
        this.imgUrl = imgUrl;
    }
}
