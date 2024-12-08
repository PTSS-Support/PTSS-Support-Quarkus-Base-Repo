package org.ptss.support.identityservice.models

data class ValidationResult(
    val valid: Boolean,
    val accessToken: String?,
    val refreshToken: String?,
    val roles: Set<String>?,
    val error: String? = null
) {
    companion object {
        fun invalid(error: String): ValidationResult {
            return ValidationResult(false, null, null, null, error)
        }
    }
}