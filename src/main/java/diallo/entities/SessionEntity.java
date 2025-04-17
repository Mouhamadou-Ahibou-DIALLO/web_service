package diallo.entities;

import java.time.Instant;

public class SessionEntity  {

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
