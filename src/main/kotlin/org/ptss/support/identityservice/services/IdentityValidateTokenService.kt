package org.ptss.support.identityservice.services

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.identityservice.dtos.TokenValidationResponseDTO
import org.ptss.support.identityservice.dtos.ValidateTokenRequestDTO
import org.ptss.support.identityservice.interfaces.IIdentityValidateTokenService

@ApplicationScoped
class IdentityValidateTokenService : IIdentityValidateTokenService {
    override fun validateToken(request: ValidateTokenRequestDTO): TokenValidationResponseDTO {
        // Mock token validation logic
        return when {
            request.token.startsWith("mock_access_token_") ->
                TokenValidationResponseDTO(
                    valid = true,
                    role = "USER"
                )
            else ->
                TokenValidationResponseDTO(
                    valid = false,
                    role = null
                )
        }
    }
}