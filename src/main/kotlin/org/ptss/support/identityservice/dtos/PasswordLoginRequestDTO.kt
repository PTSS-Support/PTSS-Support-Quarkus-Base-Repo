package org.ptss.support.identityservice.dtos

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class PasswordLoginRequestDTO(
    val email: String,
    val password: String
)
