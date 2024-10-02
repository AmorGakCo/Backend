package com.amorgakco.backend.fixture.security;

import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

public class TestSecretKey {

    private static final String testSecret =
            "c28xOTJuZ2tlbGZrYW8xa2Rtdm5hbHNrZW1jemxlZmoxb2VrYW9qZajwoefjaw12901jfij02fj02WFla2ZsYWtlZms";

    public static SecretKey create() {
        return Keys.hmacShaKeyFor(testSecret.getBytes(StandardCharsets.UTF_8));
    }
}
