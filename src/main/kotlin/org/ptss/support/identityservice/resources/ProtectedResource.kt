package org.ptss.support.identityservice.resources

import jakarta.inject.Inject
import jakarta.ws.rs.CookieParam
import jakarta.ws.rs.GET
import jakarta.ws.rs.HeaderParam
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.Response
import org.ptss.support.identityservice.services.TokenValidationService

@Path("/api/protected")
class ProtectedResource @Inject constructor(
    private val tokenService: TokenValidationService
) {
    @GET
    fun getProtectedData(
        @HeaderParam("Authorization") authorization: String,
        @CookieParam("refresh_token") refreshToken: String?
    ): Response {
        val accessToken = authorization.removePrefix("Bearer ")
        val result = tokenService.validateTokens(accessToken, refreshToken)

        return when {
            !result.valid -> {
                println("Invalid token: ${result.error}") // Log the specific error
                Response.status(Response.Status.UNAUTHORIZED)
                    .entity(result.error ?: "Unauthorized")
                    .build()
            }
            result.roles.isNullOrEmpty() || !result.roles.contains("admin") -> {
                Response.status(Response.Status.FORBIDDEN)
                    .entity("Access denied: Insufficient permissions")
                    .build()
            }
            else -> {
                // If tokens were refreshed, include them in the response headers
                Response.ok("Protected data")
                    .apply {
                        if (accessToken != result.accessToken) {
                            result.accessToken?.let { header("New-Access-Token", it) }
                            result.refreshToken?.let { header("New-Refresh-Token", it) }
                        }
                    }
                    .build()
            }
        }
    }
}