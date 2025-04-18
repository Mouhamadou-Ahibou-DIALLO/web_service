package diallo.repositories;

import diallo.entities.PostEntity;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PostRepository implements PanacheMongoRepository<PostEntity> {

    public PostEntity findPostById(String id) {
        return find("id", id).firstResult();
    }
}
