package org.ptss.support.identityservice.resources

import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.ptss.support.identityservice.dtos.TokenValidationResponseDTO
import org.ptss.support.identityservice.dtos.ValidateTokenRequestDTO
import org.ptss.support.identityservice.services.IdentityValidateTokenService

class IdentityValidateTokenResource(private val identityValidateTokenService: IdentityValidateTokenService) {
    @POST
    @Path("/validate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun validateToken(request: ValidateTokenRequestDTO): TokenValidationResponseDTO =
        identityValidateTokenService.validateToken(request)
}