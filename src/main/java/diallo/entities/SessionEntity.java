package diallo.entities;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

import java.time.Instant;

@MongoEntity(collection = "sessionUsers", database = "posts")
public class SessionEntity extends PanacheMongoEntity {

    private String sessionId;
    private Long userId;
    private String mail;
    private String nom;
    private String prenom;
    private String avatar;
    private String pseudo;
    private int statutConnexion;
    private Instant createdAt;
    private Instant expiredAt;

    public SessionEntity() {}

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long id) {
        this.userId = id;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setStatutConnexion(int statutConnexion) {
        this.statutConnexion = statutConnexion;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Instant expiredAt) {
        this.expiredAt = expiredAt;
    }
}

/*

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


 */
