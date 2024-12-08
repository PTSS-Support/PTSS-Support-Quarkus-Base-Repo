package org.ptss.support.identityservice.resources

import jakarta.annotation.security.RolesAllowed
import jakarta.inject.Inject
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import jakarta.ws.rs.core.Response
import org.ptss.support.identityservice.services.TokenValidationService

@Path("/api")
class AdminResource @Inject constructor(
    private val tokenService: TokenValidationService
) {
    @RolesAllowed("admin")
    @GET
    @Path("/admin")
    fun adminOnlyEndpoint(): Response {
        return Response.ok("Admin-only content").build()
    }

    @RolesAllowed("manager")
    @GET
    @Path("/manager")
    fun managerOnlyEndpoint(): Response {
        return Response.ok("Manager-only content").build()
    }
}
