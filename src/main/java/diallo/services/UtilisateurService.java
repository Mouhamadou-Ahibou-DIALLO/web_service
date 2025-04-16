package diallo.services;

import diallo.entities.SessionEntity;
import diallo.entities.UtilisateurEntity;
import diallo.utils.HashUtil;
import diallo.repositories.UtilisateurRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UtilisateurService {

    @Inject
    UtilisateurRepository utilisateurRepository;

    @Transactional
    public SessionEntity authentifier(LoginRequest loginRequest)
            throws UtilisateurNotFoundException, WrongPasswordException {

        UtilisateurEntity utilisateurEntity = utilisateurRepository.findByMail(loginRequest.getMail());
        if (utilisateurEntity == null) {
            throw new UtilisateurNotFoundException(loginRequest.getMail());
        }

        String motPasseHache = HashUtil.sha1(loginRequest.getMotPasse());
        if (!utilisateurEntity.getMotPasse().equals(motPasseHache)) {
            throw new WrongPasswordException();
        }

        utilisateurEntity.setStatutConnexion(1);
        utilisateurRepository.persist(utilisateurEntity);

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

        sessionEntity.persist();

        return sessionEntity;
    }

    public void logout(String sessionId) throws UtilisateurNotFoundException {
        SessionEntity sessionEntity = SessionEntity.find("sessionId", sessionId).firstResult();
        if (sessionEntity == null) return;

        UtilisateurEntity utilisateurEntity = utilisateurRepository.findById(sessionEntity.getUserId());
        if (utilisateurEntity == null) {
            throw new UtilisateurNotFoundException(sessionEntity.getUserId());
        }

        utilisateurEntity.setStatutConnexion(0);
        utilisateurRepository.persist(utilisateurEntity);

        sessionEntity.delete();
    }


    public List<UtilisateurEntity> getAllUtilisateursConnected() {
        System.out.println("All utilisateurs connected");
        return utilisateurRepository.getAllUtilisateursConnected();
    }

    public List<UtilisateurEntity> getAllUtilisateurs() {
        System.out.println("List des utilisateurs");
        return utilisateurRepository.listAll();
    }

    public String updateAvatar(Long id, String avatar) throws UtilisateurNotFoundException {
        UtilisateurEntity utilisateurEntity = utilisateurRepository.findById(id);
        if (utilisateurEntity == null) {
            throw new UtilisateurNotFoundException(id);
        }

        utilisateurEntity.setAvatar(avatar);
        utilisateurRepository.persist(utilisateurEntity);
        System.out.println("Avatar mis à jour");
        return utilisateurEntity.getAvatar();
    }
}
