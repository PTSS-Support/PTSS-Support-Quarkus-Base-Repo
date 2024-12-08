package org.ptss.support.identityservice.dtos

import com.fasterxml.jackson.annotation.JsonProperty

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String
)
