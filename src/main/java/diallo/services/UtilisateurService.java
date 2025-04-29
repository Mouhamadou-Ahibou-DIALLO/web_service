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

    public SessionEntity authentifier(LoginRequest loginRequest)
            throws UtilisateurNotFoundException, WrongPasswordException {

        UtilisateurEntity utilisateurEntity = utilisateurRepository.findByMail(loginRequest.mail());
        if (utilisateurEntity == null) {
            System.out.println("false");
            throw new UtilisateurNotFoundException(loginRequest.mail());
        }

        String motPasseHache = HashUtil.sha1(loginRequest.motPasse());
        if (!utilisateurEntity.getMotPasse().equals(motPasseHache)) {
            throw new WrongPasswordException();
        }

        utilisateurEntity.setStatutConnexion(1);
        utilisateurRepository.persist(utilisateurEntity);
        System.out.println("authentication success");

        return sessionService.createSession(utilisateurEntity.getId(),
                utilisateurEntity.getMail(), utilisateurEntity.getNom(),
                utilisateurEntity.getPrenom(), utilisateurEntity.getPseudo());
    }

    public void logout(String sessionId) throws UtilisateurNotFoundException {
        SessionEntity session = sessionService.getSession(sessionId);
        if (session == null) throw new UtilisateurNotFoundException("Session invalide");

        UtilisateurEntity utilisateur = utilisateurRepository.findById(session.getUserId());
        if (utilisateur != null) {
            utilisateur.setStatutConnexion(0);
            utilisateurRepository.persist(utilisateur);
            System.out.println("Logout success");
        }

        sessionService.removeSession(sessionId);
    }

    public UtilisateurEntity getUserBySession(String sessionId) {
        SessionEntity session = sessionService.getSession(sessionId);
        if (session == null) return null;

        System.out.println("get user by session success : " + session);
        return utilisateurRepository.findById(session.getUserId());
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
