package com.mkstudio.auth;

import java.util.UUID;

public record LoginResponse(
        String accessToken,
        String tokenType,
        long expiresIn,
        AuthenticatedUser user
) {
    public record AuthenticatedUser(
            UUID id,
            String name,
            String email,
            String role
    ) {
    }
}
