package org.ptss.support.identityservice.filters

import io.quarkus.runtime.annotations.RegisterForReflection
import jakarta.enterprise.context.ApplicationScoped
import jakarta.ws.rs.container.ContainerRequestContext
import jakarta.ws.rs.container.ContainerRequestFilter
import jakarta.ws.rs.core.HttpHeaders
import jakarta.ws.rs.core.Response
import org.slf4j.LoggerFactory

@ApplicationScoped
@RegisterForReflection
class CookieAuthenticationFilter : ContainerRequestFilter  {

    private val logger = LoggerFactory.getLogger(CookieAuthenticationFilter::class.java)

    override fun filter(requestContext: ContainerRequestContext) {
        // Skip authentication for login endpoint
        if (requestContext.uriInfo.path.contains("/auth/login")) {
            return
        }

        val cookieHeader = requestContext.getHeaderString(HttpHeaders.COOKIE)
        val accessToken = extractTokenFromCookie(cookieHeader)

        if (accessToken.isNullOrBlank()) {
            logger.warn("No access token found in cookies")
            requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                    .entity("No access token provided")
                    .build()
            )
            return
        }

        // Here you would typically validate the token with your identity service
        // For now, we'll use a simple mock validation
        val isValidToken = accessToken.startsWith("mock_access_token_")

        if (!isValidToken) {
            logger.warn("Invalid access token")
            requestContext.abortWith(
                Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid access token")
                    .build()
            )
        }
    }

    private fun extractTokenFromCookie(cookieHeader: String?): String? {
        cookieHeader?.split(";")?.forEach { cookie ->
            val trimmedCookie = cookie.trim()
            if (trimmedCookie.startsWith("access_token=")) {
                return trimmedCookie.substringAfter("access_token=")
            }
        }
        return null
    }
}