package com.amorgakco.backend.member.domain;

import com.amorgakco.backend.global.BaseTime;
import com.amorgakco.backend.global.exception.IllegalFormatException;
import com.amorgakco.backend.global.oauth.provider.Oauth2Provider;

import jakarta.persistence.*;

import lombok.*;

import org.locationtech.jts.geom.Point;

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
    private Integer mogakcoTemperature;
    private String phoneNumber;
    private String githubUrl;

    @Enumerated(EnumType.STRING)
    private SmsNotificationSetting smsNotificationSetting;

    @Column(columnDefinition = "geometry(POINT, 4326)", name = "point")
    private Point point;

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
        this.mogakcoTemperature = 0;
        this.smsNotificationSetting = SmsNotificationSetting.OFF;
    }

    public void updateNicknameAndImgUrl(final String nickname, final String imgUrl) {
        this.nickname = nickname;
        this.imgUrl = imgUrl;
    }

    public void validateAndUpdateAdditionalInfo(
            final String githubUrl,
            final String phoneNumber,
            final SmsNotificationSetting setting,
            final Point point) {
        validateGithubUrl(githubUrl);
        validatePhoneNumber(phoneNumber);
        this.githubUrl = githubUrl;
        this.phoneNumber = phoneNumber;
        this.smsNotificationSetting = setting;
        this.point = point;
    }

    private void validateGithubUrl(final String githubUrl) {
        if (!githubUrl.startsWith(GITHUB_PREFIX) && !githubUrl.startsWith(HTTPS_GITHUB_PREFIX)) {
            throw IllegalFormatException.invalidGithubUrl();
        }
    }

    private void validatePhoneNumber(final String phoneNumber) {
        if (phoneNumber.contains("-")) {
            throw IllegalFormatException.dashNotAllowed();
        }
    }

    public boolean isEquals(final Long memberId) {
        return this.id.equals(memberId);
    }
}
