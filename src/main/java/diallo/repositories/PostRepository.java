package diallo.repositories;

import diallo.entities.PostEntity;
import io.quarkus.mongodb.panache.PanacheMongoRepository;
import org.bson.types.ObjectId;

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

    public List<PostEntity> findBySharedBy(Long userId) {
        return listAll().stream()
                .filter(p -> p.getSharedBy() != null && p.getSharedBy().contains(userId.intValue()))
                .collect(Collectors.toList());
    }

    public List<PostEntity> findByCommentedBy(Long userId) {
        return listAll().stream()
                .filter(p -> p.getComments() != null &&
                        p.getComments().stream().anyMatch(c -> c.commentedBy == userId))
                .collect(Collectors.toList());
    }

    public List<PostEntity> searchByKeyword(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return listAll().stream()
                .filter(p -> p.getTitle().toLowerCase().contains(lowerKeyword)
                        || p.getBody().toLowerCase().contains(lowerKeyword)
                        || p.getHashtags().stream().anyMatch(h -> h.toLowerCase().contains(lowerKeyword)))
                .collect(Collectors.toList());
    }

    public List<Long> getUserIdsWhoLiked(String postId) {
        PostEntity post = findById(new ObjectId(postId));
        return post != null ? post.getLikedBy().stream().map(Integer::longValue).toList() : List.of();
    }
}
