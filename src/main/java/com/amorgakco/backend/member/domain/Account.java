package com.amorgakco.backend.member.domain;

import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

    @Builder
    public Account(
            final Member member,
            final String provider,
            final String identifier,
            final String imgUrl,
            final String nickname) {
        this.member = member;
        this.provider = provider;
        this.identifier = identifier;
        this.imgUrl = imgUrl;
        this.nickname = nickname;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private String provider;
    private String identifier;
    private String imgUrl;
    private String nickname;

    public void updateAccount(String identifier, String imgUrl, String nickname) {
        this.identifier = identifier;
        this.imgUrl = imgUrl;
        this.nickname = nickname;
    }
}
