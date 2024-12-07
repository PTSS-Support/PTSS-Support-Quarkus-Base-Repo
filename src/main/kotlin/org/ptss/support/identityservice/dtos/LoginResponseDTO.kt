package org.ptss.support.identityservice.dtos

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class LoginResponseDTO(
    val accessToken: String,
    val refreshToken: String
)
