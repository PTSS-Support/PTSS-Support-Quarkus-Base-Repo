package org.ptss.support.identityservice.dtos

import io.quarkus.runtime.annotations.RegisterForReflection

@RegisterForReflection
data class TokenValidationResponseDTO(
    val valid: Boolean,
    val role: String? = null
)
