package org.ptss.support.security


import io.quarkus.security.UnauthorizedException
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.container.ResourceInfo
import jakarta.ws.rs.core.Context
import jakarta.ws.rs.core.HttpHeaders
import jakarta.ws.rs.core.NewCookie
import jakarta.ws.rs.ext.Provider
import org.eclipse.microprofile.config.inject.ConfigProperty


@Provider
@ApplicationScoped
class AuthenticationFilter @Inject constructor(
    @Context private val resourceInfo: ResourceInfo,
    private val identityServiceClient: IdentityServiceClient,
    private val jwtValidator: JwtValidator,
    @ConfigProperty(name = "app.security.access-token-cookie-name")
    private val accessTokenCookieName: String,
    @ConfigProperty(name = "app.security.refresh-token-cookie-name")
    private val refreshTokenCookieName: String
) : ContainerRequestFilter {

    companion object {
        private const val AUTHENTICATION_FAILED_MESSAGE = "Authentication failed"
    }

    override fun filter(requestContext: ContainerRequestContext) {
        val annotation = getAuthenticationAnnotation(resourceInfo) ?: return

        val accessToken = requestContext.cookies[accessTokenCookieName]?.value
        val refreshToken = requestContext.cookies[refreshTokenCookieName]?.value

        // Validate refresh token
        if (!jwtValidator.isTokenValidAndNotBlank(refreshToken)) {
            throw UnauthorizedException(AUTHENTICATION_FAILED_MESSAGE)
        }

        // Determine token to use
        val tokenToUse = when {
            jwtValidator.isTokenValidAndNotBlank(accessToken) -> accessToken!!
            else -> refreshAccessToken(requestContext, refreshToken!!)
        }

        // Check role authorization
        if (!jwtValidator.hasRequiredRole(tokenToUse, annotation.roles.toSet())) {
            throw UnauthorizedException(AUTHENTICATION_FAILED_MESSAGE)
        }
    }

    private fun refreshAccessToken(
        requestContext: ContainerRequestContext,
        refreshToken: String
    ): String {
        return try {
            val newToken = identityServiceClient.refreshAccessToken(refreshToken)
            val newAccessTokenCookie = createNewAccessTokenCookie(newToken)
            requestContext.headers.add(HttpHeaders.SET_COOKIE, newAccessTokenCookie.toString())
            newToken
        } catch (e: Exception) {
            throw UnauthorizedException(AUTHENTICATION_FAILED_MESSAGE)
        }
    }

    private fun createNewAccessTokenCookie(newAccessToken: String): NewCookie =
        NewCookie.Builder(accessTokenCookieName)
            .value(newAccessToken)
            .path("/")
            .httpOnly(true)
            .secure(true)
            .build()

    private fun getAuthenticationAnnotation(resourceInfo: ResourceInfo): Authentication? =
        resourceInfo.resourceMethod?.getAnnotation(Authentication::class.java)
            ?: resourceInfo.resourceClass?.getAnnotation(Authentication::class.java)
}
