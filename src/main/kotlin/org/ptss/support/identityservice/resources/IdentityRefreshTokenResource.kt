package org.ptss.support.identityservice.resources

import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import org.ptss.support.identityservice.dtos.LoginResponseDTO
import org.ptss.support.identityservice.dtos.RefreshTokenRequestDTO
import org.ptss.support.identityservice.services.IdentityRefreshTokenService

@Path("/auth")
class IdentityRefreshTokenResource(private val identityRefreshTokenService: IdentityRefreshTokenService) {
    @POST
    @Path("/refresh")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun refreshToken(request: RefreshTokenRequestDTO): LoginResponseDTO =
        identityRefreshTokenService.refreshToken(request)
}