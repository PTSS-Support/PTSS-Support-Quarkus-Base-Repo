package org.ptss.support.identityservice.interfaces

import org.ptss.support.identityservice.dtos.LoginResponseDTO
import org.ptss.support.identityservice.dtos.RefreshTokenRequestDTO

interface IIdentityRefreshTokenService {
    fun refreshToken(request: RefreshTokenRequestDTO): LoginResponseDTO
}