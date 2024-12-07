package org.ptss.support.identityservice.resources

import jakarta.ws.rs.Consumes
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.NewCookie
import jakarta.ws.rs.core.Response
import org.ptss.support.identityservice.dtos.LoginResponseDTO
import org.ptss.support.identityservice.dtos.PasswordLoginRequestDTO
import org.ptss.support.identityservice.services.IdentityLoginService
import java.time.Duration

@Path("/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
class IdentityLoginResource(private val identityLoginService: IdentityLoginService) {
    
    @POST
    @Path("/login")
    fun login(request: PasswordLoginRequestDTO): Response {
        val loginResponse = identityLoginService.login(request)

        return Response.ok(loginResponse)
            .cookie(
                NewCookie.Builder("access_token")
                    .value(loginResponse.accessToken)
                    .httpOnly(true)
                    .secure(true)
                    .maxAge(Duration.ofMinutes(30).seconds.toInt())
                    .build()
            )
            .cookie(
                NewCookie.Builder("refresh_token")
                    .value(loginResponse.refreshToken)
                    .httpOnly(true)
                    .secure(true)
                    .maxAge(Duration.ofDays(7).seconds.toInt())
                    .build()
            )
            .build()
    }
}