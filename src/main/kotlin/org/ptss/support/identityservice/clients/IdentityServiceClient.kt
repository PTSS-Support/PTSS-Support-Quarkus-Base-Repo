package org.ptss.support.identityservice.clients

import jakarta.ws.rs.FormParam
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import org.ptss.support.identityservice.dtos.TokenResponse

@RegisterRestClient(baseUri = "http://localhost:8080/auth")
interface IdentityServiceClient {
    @POST
    @Path("/refresh")
    fun refreshTokens(@FormParam("refreshToken") refreshToken: String): TokenResponse
}