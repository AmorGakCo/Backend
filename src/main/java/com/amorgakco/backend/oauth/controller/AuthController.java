package com.amorgakco.backend.oauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/")
    public String home() {
        return "Success";
    }

    @GetMapping("/others")
    public String other() {
        return "Something";
    }
}
