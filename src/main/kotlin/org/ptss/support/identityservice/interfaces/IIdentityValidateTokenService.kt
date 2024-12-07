package org.ptss.support.identityservice.interfaces

import org.ptss.support.identityservice.dtos.TokenValidationResponseDTO
import org.ptss.support.identityservice.dtos.ValidateTokenRequestDTO

interface IIdentityValidateTokenService {
    fun validateToken(request: ValidateTokenRequestDTO): TokenValidationResponseDTO
}