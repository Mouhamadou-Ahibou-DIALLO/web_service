package diallo.services;

public class WrongPasswordException extends Exception {
    public WrongPasswordException() {
        super("Mot de passe incorrect");
    }
}
