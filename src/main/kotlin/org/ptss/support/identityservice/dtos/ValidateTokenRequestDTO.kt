package org.ptss.support.identityservice.dtos

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class ValidateTokenRequestDTO(
    val token: String
)
