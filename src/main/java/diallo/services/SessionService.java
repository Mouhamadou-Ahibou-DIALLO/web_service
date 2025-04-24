package diallo.services;

import diallo.entities.SessionEntity;
import io.quarkus.mongodb.panache.PanacheMongoRepository;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Date;
import java.util.UUID;

@ApplicationScoped
public class SessionService implements PanacheMongoRepository<SessionEntity> {

    public SessionEntity createSession(Long userId, String mail, String nom, String prenom, String pseudo) {
        SessionEntity session = new SessionEntity();
        session.setSessionId(UUID.randomUUID().toString());
        session.setUserId(userId);
        session.setMail(mail);
        session.setNom(nom);
        session.setPrenom(prenom);
        session.setPseudo(pseudo);
        session.setStatutConnexion(1);

        session.setCreatedAt(new Date());
        session.setExpiredAt(new Date(System.currentTimeMillis() + 3600 * 1000));

        persist(session);
        return session;
    }

    public void removeSession(String sessionId) {
        delete("sessionId", sessionId);
    }

    public SessionEntity getSession(String sessionId) {
        return find("sessionId", sessionId).firstResult();
    }
}

