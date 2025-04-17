package diallo.services;

import diallo.entities.SessionEntity;
import diallo.entities.UtilisateurEntity;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@ApplicationScoped
public class SessionService {

    private final Map<String, SessionEntity> sessionStore = new ConcurrentHashMap<>();

    public SessionEntity createSession(UtilisateurEntity utilisateurEntity) {
        SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setUserId(utilisateurEntity.getId());
        sessionEntity.setMail(utilisateurEntity.getMail());
        sessionEntity.setNom(utilisateurEntity.getNom());
        sessionEntity.setPrenom(utilisateurEntity.getPrenom());
        sessionEntity.setAvatar(utilisateurEntity.getAvatar());
        sessionEntity.setPseudo(utilisateurEntity.getPseudo());
        sessionEntity.setStatutConnexion(utilisateurEntity.getStatutConnexion());
        sessionEntity.setCreatedAt(Instant.now());
        sessionEntity.setExpiredAt(Instant.now().plusSeconds(86400));
        sessionEntity.setSessionId(UUID.randomUUID().toString());

        sessionStore.put(sessionEntity.getSessionId(), sessionEntity);
        return sessionEntity;
    }

    public SessionEntity getSession(String sessionId) {
        return sessionStore.get(sessionId);
    }

    public void removeSession(String sessionId) {
        sessionStore.remove(sessionId);
    }
}
