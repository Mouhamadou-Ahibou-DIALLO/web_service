package diallo.services;

import diallo.entities.SessionEntity;
import diallo.entities.UtilisateurEntity;
import diallo.utils.HashUtil;
import diallo.repositories.UtilisateurRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class UtilisateurService {

    @Inject
    UtilisateurRepository utilisateurRepository;

    @Inject
    SessionService sessionService;

    public Long authentifier(LoginRequest loginRequest)
            throws UtilisateurNotFoundException, WrongPasswordException {

        UtilisateurEntity utilisateurEntity = utilisateurRepository.findByMail(loginRequest.getMail());
        if (utilisateurEntity == null) {
            System.out.println("false");
            throw new UtilisateurNotFoundException(loginRequest.getMail());
        }

        String motPasseHache = HashUtil.sha1(loginRequest.getMotPasse());
        if (!utilisateurEntity.getMotPasse().equals(motPasseHache)) {
            throw new WrongPasswordException();
        }

        utilisateurEntity.setStatutConnexion(1);
        utilisateurRepository.persist(utilisateurEntity);
        System.out.println("authentication success");

        return utilisateurEntity.getId();
    }

    public void logout(String sessionId) throws UtilisateurNotFoundException {
        SessionEntity sessionEntity = sessionService.getSession(sessionId);

        UtilisateurEntity utilisateurEntity = utilisateurRepository.findById(sessionEntity.getUserId());
        if (utilisateurEntity == null) {
            throw new UtilisateurNotFoundException(sessionEntity.getUserId());
        }

        utilisateurEntity.setStatutConnexion(0);
        utilisateurRepository.persist(utilisateurEntity);
        System.out.println("logout success");

        sessionService.removeSession(sessionId);
    }

    public SessionEntity getUserBySession(String sessionId) {
        return sessionService.getSession(sessionId);
    }

    public List<UtilisateurEntity> getAllUtilisateursConnected() {
        System.out.println("All utilisateurs connected");
        return utilisateurRepository.getAllUtilisateursConnected();
    }

    public List<UtilisateurEntity> getAllUtilisateurs() {
        System.out.println("List des utilisateurs");
        return utilisateurRepository.listAll();
    }
}
