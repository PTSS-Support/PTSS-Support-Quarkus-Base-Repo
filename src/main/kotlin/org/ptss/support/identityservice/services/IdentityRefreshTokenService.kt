package org.ptss.support.identityservice.services

import jakarta.enterprise.context.ApplicationScoped
import org.ptss.support.identityservice.dtos.LoginResponseDTO
import org.ptss.support.identityservice.dtos.RefreshTokenRequestDTO
import org.ptss.support.identityservice.interfaces.IIdentityRefreshTokenService

@ApplicationScoped
class IdentityRefreshTokenService : IIdentityRefreshTokenService {
    override fun refreshToken(request: RefreshTokenRequestDTO): LoginResponseDTO {
        // Mock refresh token logic
        return LoginResponseDTO(
            accessToken = "new_mock_access_token_${System.currentTimeMillis()}",
            refreshToken = request.refreshToken
        )
    }
}