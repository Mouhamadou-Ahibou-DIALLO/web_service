package diallo.controllers;

import diallo.entities.SessionEntity;
import diallo.entities.UtilisateurEntity;
import diallo.services.LoginRequest;
import diallo.services.UtilisateurNotFoundException;
import diallo.services.UtilisateurService;
import diallo.services.WrongPasswordException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.time.Instant;
import java.util.List;

@ApplicationScoped
@Path("/utilisateur")
@Produces("application/json")
@Consumes("application/json")
public class UtilisateurController {

    @Inject
    UtilisateurService utilisateurService;

    @GET
    public List<UtilisateurEntity> getAllUtilisateurs() {
        return utilisateurService.getAllUtilisateurs();
    }

    @GET
    @Path("/getAllconnected")
    public List<UtilisateurEntity> getAllUtilisateursConnected() {
        return utilisateurService.getAllUtilisateursConnected();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authentifier(LoginRequest loginRequest) {
        try {
            SessionEntity sessionEntity = utilisateurService.authentifier(loginRequest);
            return Response.ok(sessionEntity).build();
        } catch (UtilisateurNotFoundException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Utilisateur non trouvé").build();
        } catch (WrongPasswordException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Mot de passe incorrect").build();
        }
    }

    @POST
    @Path("/logout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response logout(@QueryParam("sessionId") String sessionId) {
        try {
            utilisateurService.logout(sessionId);
            return Response.ok("Déconnexion réussie").build();
        } catch (UtilisateurNotFoundException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Utilisateur non trouvé pour cette session").build();
        }
    }

    @GET
    @Path("/session")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSessionUser(@QueryParam("sessionId") String sessionId) {
        SessionEntity sessionEntity = SessionEntity.find("sessionId", sessionId).firstResult();
        if (sessionEntity != null && sessionEntity.getExpiredAt().isAfter(Instant.now())) {
            return Response.ok(sessionEntity).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Session invalide ou expirée").build();
        }
    }


    @PUT
    @Path("/avatar")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateAvatar(Long id, String avatar) {
        try {
            String updatedAvatar = utilisateurService.updateAvatar(id, avatar);
            return Response.ok(updatedAvatar).build();
        } catch (UtilisateurNotFoundException e) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }
}
