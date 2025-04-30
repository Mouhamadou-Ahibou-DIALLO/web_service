package diallo.controllers;

import diallo.entities.SessionEntity;
import diallo.entities.UtilisateurEntity;
import diallo.services.LoginRequest;
import diallo.services.UtilisateurNotFoundException;
import diallo.services.UtilisateurService;
import diallo.services.WrongPasswordException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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

    @GET
    @Path("/{id}")
    public UtilisateurEntity getUtilisateurById(@PathParam("id") long id) {
        return utilisateurService.getUtilisateurById(id);
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authentifier(LoginRequest loginRequest) {
        try {
            SessionEntity session = utilisateurService.authentifier(loginRequest);
            return Response.ok(session).build();
        } catch (UtilisateurNotFoundException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Utilisateur non trouvé").build();
        } catch (WrongPasswordException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Mot de passe incorrect").build();
        }
    }

    @POST
    @Path("/logout/{sessionId}")
    public Response logout(@PathParam("sessionId") String sessionId) {
        try {
            utilisateurService.logout(sessionId);
            return Response.ok("Déconnecté avec succès").build();
        } catch (UtilisateurNotFoundException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Session invalide").build();
        }
    }

    @GET
    @Path("/session/{sessionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserBySession(@PathParam("sessionId") String sessionId) {
        UtilisateurEntity utilisateur = utilisateurService.getUserBySession(sessionId);
        if (utilisateur == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Utilisateur non trouvé pour cette session").build();
        }
        return Response.ok(utilisateur).build();
    }

}
