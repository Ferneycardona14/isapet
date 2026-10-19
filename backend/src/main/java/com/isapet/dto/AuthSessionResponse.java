package com.isapet.dto;

import java.util.Set;

public record AuthSessionResponse(
        String username,
        Set<String> roles
) {
}
