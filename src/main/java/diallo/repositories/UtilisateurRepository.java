package diallo.repositories;

import diallo.entities.UtilisateurEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class UtilisateurRepository implements PanacheRepository<UtilisateurEntity> {

    public UtilisateurEntity findByMail(String mail) {
        return find("mail", mail).firstResult();
    }

    public List<UtilisateurEntity> getAllUtilisateursConnected() {
        return find("statutConnexion", 1).list();
    }
}
