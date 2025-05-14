package diallo.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

import java.util.Date;

@MongoEntity(collection = "sessionAhibou", database = "db-CERI")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SessionEntity extends PanacheMongoEntity {

    private String sessionId;
    private Long userId;

    public String getMail() {
        return mail;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getPseudo() {
        return pseudo;
    }

    public int getStatutConnexion() {
        return statutConnexion;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getExpiredAt() {
        return expiredAt;
    }

    private String mail;
    private String nom;
    private String prenom;
    private String pseudo;
    private int statutConnexion;

    private Date createdAt;
    private Date expiredAt;

    public SessionEntity() {}

    public SessionEntity(String sessionId, Long userId, String mail, String nom, String prenom,
                         String pseudo, int statutConnexion, Date createdAt, Date expiredAt) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.mail = mail;
        this.nom = nom;
        this.prenom = prenom;
        this.pseudo = pseudo;
        this.statutConnexion = statutConnexion;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
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

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public void setStatutConnexion(int statutConnexion) {
        this.statutConnexion = statutConnexion;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public void setExpiredAt(Date expiredAt) {
        this.expiredAt = expiredAt;
    }
}