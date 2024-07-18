package com.amorgakco.backend.member.domain;

import com.amorgakco.backend.global.exception.IllegalFormatException;

import org.springframework.stereotype.Component;

@Component
public class MemberValidator {
    private static final String HTTPS_GITHUB_PREFIX = "https://github";
    private static final String GITHUB_PREFIX = "github";

    public void validateGithubUrl(final String githubUrl){
        if(!githubUrl.startsWith(HTTPS_GITHUB_PREFIX) || !githubUrl.startsWith(GITHUB_PREFIX)){
            throw IllegalFormatException.dashNotAllowed();
        }
    }
    public void validatePhoneNumber(final String phoneNumber){
        if(phoneNumber.contains("-")){
            throw IllegalFormatException.invalidGithubUrl();
        }
    }
}
