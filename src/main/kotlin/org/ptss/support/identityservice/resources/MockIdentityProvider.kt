package org.ptss.support.identityservice.resources

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.ws.rs.Consumes
import jakarta.ws.rs.FormParam
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import jakarta.ws.rs.Produces
import jakarta.ws.rs.core.MediaType
import jakarta.ws.rs.core.Response
import java.time.Instant
import java.util.Base64

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
class MockIdentityProvider {

    @POST
    @Path("/refresh")
    fun refreshToken(@FormParam("refreshToken") refreshToken: String): Response {
        if (refreshToken == "valid-refresh-token") {
            val newAccessToken = generateToken("user", listOf("admin", "manager"), Instant.now().plusSeconds(3600).epochSecond)
            val newRefreshToken = generateToken("user", listOf(), Instant.now().plusSeconds(7200).epochSecond)

            return Response.ok(
                mapOf(
                    "accessToken" to newAccessToken,
                    "refreshToken" to newRefreshToken
                )
            ).build()
        }
        return Response.status(401).entity("Invalid refresh token").build()
    }

    private fun generateToken(subject: String, roles: List<String>, expiration: Long): String {
        val token = mapOf(
            "sub" to subject,
            "roles" to roles,
            "exp" to expiration
        )
        val json = ObjectMapper().writeValueAsString(token) // Convert to valid JSON
        return Base64.getEncoder().encodeToString(json.toByteArray()) // Encode JSON to Base64
    }
}