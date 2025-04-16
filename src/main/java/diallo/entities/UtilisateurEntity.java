package diallo.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "compte", schema = "fredouil")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UtilisateurEntity extends PanacheEntityBase {
    @Id
    private Long id;
    private String pseudo;
    private String mail;

    @Column(name = "motpasse")
    private String motPasse;
    private String prenom;
    private String nom;
    private String avatar;

    @Column(name = "statut_connexion")
    private int statutConnexion;

    public UtilisateurEntity() {}

    public Long getId() {
        return id;
    }

    public int getStatutConnexion() {
        return statutConnexion;
    }

    public void setStatutConnexion(int statutConnexion) {
        this.statutConnexion = statutConnexion;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMail() {
        return mail;
    }

    public String getMotPasse() {
        return motPasse;
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

    @Override
    public String toString() {
        return "UtilisateurEntity{" +
                "id=" + id +
                ", pseudo='" + pseudo + '\'' +
                ", mail='" + mail + '\'' +
                ", motPasse='" + motPasse + '\'' +
                ", prenom='" + prenom + '\'' +
                ", nom='" + nom + '\'' +
                ", avatar='" + avatar + '\'' +
                ", statutConnexion=" + statutConnexion +
                '}';
    }
}
