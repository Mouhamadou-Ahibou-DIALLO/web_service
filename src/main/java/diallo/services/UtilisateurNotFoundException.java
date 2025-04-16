package diallo.services;

public class UtilisateurNotFoundException extends RuntimeException {
    public UtilisateurNotFoundException(String mail) {
        super("Utilisateur " + mail + " introuvable");
    }

    public UtilisateurNotFoundException(Long id) {
        super("Utilisateur " + id + " introuvable");
    }
}
