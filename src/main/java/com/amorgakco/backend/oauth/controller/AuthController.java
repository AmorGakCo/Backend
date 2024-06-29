package com.amorgakco.backend.oauth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    @GetMapping("/")
    public String home() {
        return "<a href=http://localhost:8080/login/oauth2/code/kakao></a> </br>"
                + "<a href=http://localhost:8080/login/oauth2/code/github></a>";
    }

    @GetMapping("/others")
    public String other() {
        return "Something";
    }
}
