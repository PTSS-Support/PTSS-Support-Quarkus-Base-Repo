package org.ptss.support.security

import jakarta.enterprise.context.ApplicationScoped


@ApplicationScoped
class IdentityServiceClient {
    fun getAccessToken(): String {
        // Retrieve access token from identity service
        return "valid_access_token"
    }

    fun getRefreshToken(): String {
        // Retrieve refresh token from identity service
        return "valid_refresh_token"
    }
}