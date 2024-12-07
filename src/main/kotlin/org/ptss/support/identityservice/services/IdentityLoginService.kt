package org.ptss.support.identityservice.services

import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.WebApplicationException
import jakarta.ws.rs.core.Response
import org.ptss.support.identityservice.dtos.LoginResponseDTO
import org.ptss.support.identityservice.dtos.PasswordLoginRequestDTO
import org.ptss.support.identityservice.interfaces.IIdentityLoginService

@ApplicationScoped
class IdentityLoginService : IIdentityLoginService {
    override fun login(request: PasswordLoginRequestDTO): LoginResponseDTO {
        // Simple mock logic - in real implementation, this would involve actual authentication
        return when {
            request.email == "john.doe@example.com" && request.password == "password123" ->
                LoginResponseDTO(
                    accessToken = "mock_access_token_${System.currentTimeMillis()}",
                    refreshToken = "mock_refresh_token_${System.currentTimeMillis()}"
                )
            else -> throw WebApplicationException(Response.status(Response.Status.UNAUTHORIZED).build())
        }
    }
}