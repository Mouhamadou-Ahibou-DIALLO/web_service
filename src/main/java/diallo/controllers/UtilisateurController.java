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
            Long idUser = utilisateurService.authentifier(loginRequest);
            return Response.ok(idUser).build();
        } catch (UtilisateurNotFoundException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Utilisateur non trouvé").build();
        } catch (WrongPasswordException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Mot de passe incorrect").build();
        }
    }
}
