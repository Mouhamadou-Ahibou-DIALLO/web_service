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

//    @POST
//    @Path("/logout")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.TEXT_PLAIN)
//    public Response logout(@HeaderParam("Authorization") String sessionId) {
//        try {
//            utilisateurService.logout(sessionId);
//            return Response.ok("Déconnexion réussie").build();
//        } catch (UtilisateurNotFoundException e) {
//            return Response.status(Response.Status.BAD_REQUEST)
//                    .entity("Utilisateur non trouvé pour cette session").build();
//        }
//    }

//    @GET
//    @Path("/mySession")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response getSessionUser(@HeaderParam("Authorization") String sessionId) {
//        SessionEntity sessionEntity = utilisateurService.getUserBySession(sessionId);
//        if (sessionEntity != null) {
//            return Response.ok(sessionEntity).build();
//        } else {
//            return Response.status(Response.Status.UNAUTHORIZED)
//                    .entity("{\"error\":\"Session invalide ou expirée\"}")
//                    .type(MediaType.APPLICATION_JSON)
//                    .build();
//        }
//    }

    @GET
    @Path("/{mail}/{motPasse}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response testLogin(@PathParam(value = "mail") String mail, @PathParam(value = "motPasse") String motPasse) {
        try {
            Long idUser = utilisateurService.testLogin(mail, motPasse);
            return Response.ok(idUser).build();
        } catch (UtilisateurNotFoundException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Utilisateur non rencontré").build();
        } catch (WrongPasswordException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Mot de passe incorrect").build();
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

//  ahiboudiallo2018:hkoQPRPEd4n3hXpo@posts.eqvt28n.mongodb.net
//quarkus.mongodb.database=posts
