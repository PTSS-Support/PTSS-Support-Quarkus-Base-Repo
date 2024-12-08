package org.ptss.support.identityservice.services

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Inject
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.jboss.resteasy.reactive.client.api.WebClientApplicationException
import org.ptss.support.identityservice.clients.IdentityServiceClient
import org.ptss.support.identityservice.models.ValidationResult
import java.time.Instant
import java.util.Base64


@ApplicationScoped
class TokenValidationService @Inject constructor(
    @RestClient private val identityClient: IdentityServiceClient
) {
    fun validateTokens(accessToken: String, refreshToken: String?): ValidationResult {
        return try {
            // Decode the mock token
            val jwtPayload = decodeMockToken(accessToken)
            println("Decoded JWT Payload: $jwtPayload") // Debug log

            // Check expiration
            val expirationTime = (jwtPayload["exp"] as? Number)?.toLong()
                ?: return ValidationResult.invalid("Invalid token structure: Missing or invalid 'exp' field")

            if (isTokenExpired(expirationTime)) {
                refreshToken?.let {
                    try {
                        // Attempt to refresh tokens via mock identity service
                        val newTokens = identityClient.refreshTokens(refreshToken)
                        println("New Tokens: $newTokens") // Debug log

                        return ValidationResult(
                            valid = true,
                            accessToken = newTokens.accessToken,
                            refreshToken = newTokens.refreshToken,
                            roles = (jwtPayload["roles"] as? List<*>)
                                ?.mapNotNull { it as? String }
                                ?.toSet() ?: emptySet()
                        )
                    } catch (e: WebClientApplicationException) {
                        println("Refresh token error - Status: ${e.response.status}, Body: ${e.response.readEntity(String::class.java)}") // Log status and response body
                        return ValidationResult.invalid("Refresh token failed: ${e.message}")
                    } catch (e: Exception) {
                        println("Unexpected error during token refresh: ${e.message}") // Log unexpected error
                        return ValidationResult.invalid("Token refresh failed: ${e.message}")
                    }
                }

                // If no refresh token is available and the access token is expired
                return ValidationResult.invalid("Access token expired and no valid refresh token")
            }

            // Token is valid
            ValidationResult(
                valid = true,
                accessToken = accessToken,
                refreshToken = refreshToken,
                roles = (jwtPayload["roles"] as? List<*>)
                    ?.mapNotNull { it as? String }
                    ?.toSet() ?: emptySet()
            )
        } catch (e: Exception) {
            println("Token validation exception: ${e.message}") // Detailed error logging
            ValidationResult.invalid("Token validation failed: ${e.message}")
        }
    }

    private fun decodeMockToken(token: String): Map<String, Any> {
        val decoded = String(Base64.getDecoder().decode(token))
        println("Decoded token JSON: $decoded") // Log the decoded JSON

        return try {
            ObjectMapper().readValue(decoded, object : TypeReference<Map<String, Any>>() {})
        } catch (e: Exception) {
            throw IllegalArgumentException("Failed to decode token JSON: ${e.message}. Decoded token: $decoded")
        }
    }

    private fun isTokenExpired(expiration: Long): Boolean {
        return Instant.now().isAfter(Instant.ofEpochSecond(expiration))
    }
}