package diallo.repositories;

import diallo.entities.PostEntity;
import io.quarkus.mongodb.panache.PanacheMongoRepository;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class PostRepository implements PanacheMongoRepository<PostEntity> {

    public List<PostEntity> findByCreatedBy(Long createdBy) {
        return find("createdBy", createdBy).list();
    }

    public List<PostEntity> findByLikedBy(Long likedBy) {
        return find("likedBy", likedBy).list();
    }

    public List<PostEntity> findBySharedBy(Long sharedBy) {
        return find("sharedBy", sharedBy).list();
    }

    public List<PostEntity> findByCommentedBy(Long commentedBy) {
        return find("commentedBy", commentedBy).list();
    }

    public List<PostEntity> searchByKeyword(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return listAll().stream()
                .filter(p -> p.getTitle().toLowerCase().contains(lowerKeyword)
                        || p.getBody().toLowerCase().contains(lowerKeyword)
                        || p.getHashtags().stream().anyMatch(h -> h.toLowerCase().contains(lowerKeyword)))
                .collect(Collectors.toList());
    }
}
