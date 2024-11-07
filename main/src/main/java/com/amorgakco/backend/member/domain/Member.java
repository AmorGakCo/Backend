package com.amorgakco.backend.member.domain;

import com.amorgakco.backend.global.BaseTime;
import com.amorgakco.backend.global.exception.IllegalFormatException;
import com.amorgakco.backend.global.exception.MemberTemperatureException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTime {

    private static final String HTTPS_GITHUB_PREFIX = "https://github";
    private static final String GITHUB_PREFIX = "github";
    private static final Integer MAX_MOGAKCO_TEMPERATURE = 100;
    private static final Integer MIN_MOGAKCO_TEMPERATURE = -100;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private final List<Roles> roleNames = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private Oauth2ProviderType oauth2ProviderType;
    private String oauth2Id;
    private String imgUrl;
    private String nickname;
    private Integer moGakCoTemperature;
    private String phoneNumber;
    private String githubUrl;
    private boolean smsNotificationSetting;
    private String cellToken;
    @Version
    private Integer version;

    @Builder
    public Member(
        final Oauth2ProviderType oauth2ProviderType,
        final String oauth2Id,
        final String imgUrl,
        final String nickname) {
        this.oauth2ProviderType = oauth2ProviderType;
        this.oauth2Id = oauth2Id;
        this.imgUrl = imgUrl;
        this.nickname = nickname;
        this.roleNames.add(new Roles(Role.ROLE_MEMBER));
        this.moGakCoTemperature = 0;
        this.smsNotificationSetting = false;
    }

    public void updateNicknameAndImgUrl(final String nickname, final String imgUrl) {
        this.nickname = nickname;
        this.imgUrl = imgUrl;
    }

    public void validateAndUpdateAdditionalInfo(
        final String githubUrl,
        final String phoneNumber,
        final boolean setting,
        final String cellToken) {
        validateGithubUrl(githubUrl);
        validatePhoneNumber(phoneNumber);
        this.githubUrl = githubUrl;
        this.phoneNumber = phoneNumber;
        this.smsNotificationSetting = setting;
        this.cellToken = cellToken;
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

    public Integer increaseMoGakCoTemperature() {
        if (moGakCoTemperature + 1 > MAX_MOGAKCO_TEMPERATURE) {
            throw MemberTemperatureException.exceedMaxTemperature();
        }
        return ++moGakCoTemperature;
    }

    public Integer decreaseMoGakCoTemperature() {
        if (moGakCoTemperature - 1 < MIN_MOGAKCO_TEMPERATURE) {
            throw MemberTemperatureException.underMinTemperature();
        }
        return --moGakCoTemperature;
    }

    public boolean isEquals(final Long memberId) {
        return this.id.equals(memberId);
    }

    public boolean isSmsNotificationActivated() {
        return smsNotificationSetting;
    }
}
