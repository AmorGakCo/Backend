package com.amorgakco.backend.member.domain;

import com.amorgakco.backend.global.BaseTime;
import com.amorgakco.backend.global.exception.IllegalFormatException;
import com.amorgakco.backend.global.oauth.provider.Oauth2Provider;

import jakarta.persistence.*;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTime {
    private static final String HTTPS_GITHUB_PREFIX = "https://github";
    private static final String GITHUB_PREFIX = "github";
    @Id @GeneratedValue private Long id;

    @Enumerated(EnumType.STRING)
    private Oauth2Provider oauth2Provider;

    private String oauth2Id;
    private String imgUrl;
    private String nickname;
    private Integer point;
    private String phoneNumber;
    private String githubUrl;

    @Enumerated(EnumType.STRING)
    private SmsNotificationSetting smsNotificationSetting;

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
        this.smsNotificationSetting = SmsNotificationSetting.OFF;
    }

    public void updateNicknameAndImgUrl(final String nickname, final String imgUrl) {
        this.nickname = nickname;
        this.imgUrl = imgUrl;
    }

    public void validateAndUpdateAdditionalInfo(
            final String githubUrl,
            final String phoneNumber,
            final SmsNotificationSetting setting) {
        validateGithubUrl(githubUrl);
        validatePhoneNumber(phoneNumber);
        this.githubUrl = githubUrl;
        this.phoneNumber = phoneNumber;
        this.smsNotificationSetting = setting;
    }

    public void validateGithubUrl(final String githubUrl) {
        if (!githubUrl.startsWith(HTTPS_GITHUB_PREFIX) || !githubUrl.startsWith(GITHUB_PREFIX)) {
            throw IllegalFormatException.dashNotAllowed();
        }
    }

    public void validatePhoneNumber(final String phoneNumber) {
        if (phoneNumber.contains("-")) {
            throw IllegalFormatException.invalidGithubUrl();
        }
    }
}
