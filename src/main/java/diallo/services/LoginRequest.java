package diallo.services;

public class LoginRequest {
    private String mail;
    private String motPasse;

    public LoginRequest(String mail, String motPasse) {
        this.mail = mail;
        this.motPasse = motPasse;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getMotPasse() {
        return motPasse;
    }

    public void setMotPasse(String motPasse) {
        this.motPasse = motPasse;
    }
}

