package diallo.repositories;

import diallo.entities.PostEntity;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class PostRepository implements PanacheMongoRepository<PostEntity> {

    public List<PostEntity> findByCreatdBy(int createdBy) {
        return find("createdBy", createdBy).list();
    }
}
