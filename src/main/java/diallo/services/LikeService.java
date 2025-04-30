package diallo.services;

import diallo.entities.PostEntity;
import diallo.repositories.PostRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.util.List;

@ApplicationScoped
public class LikeService {

    @Inject
    PostRepository postRepository;

    public void likePost(ObjectId id, Long userId) throws PostNotFoundException {
        PostEntity post = postRepository.findById(id);
        if (post == null) {
            throw new PostNotFoundException(id);
        }
        System.out.println("Get post in like method : " + post + " et son id: " + id);

        List<Integer> likedBy = post.getLikedBy();
        if (!likedBy.contains(userId.intValue())) {
            likedBy.add(userId.intValue());
            System.out.println("post liked with success : " + post);
        }

        post.setLikes(post.getLikes() + 1);
        post.setLikedBy(likedBy);
        postRepository.update(post);
    }

    public void unlikePost(ObjectId id, Long userId) throws PostNotFoundException {
        PostEntity post = postRepository.findById(id);
        if (post == null) {
            throw new PostNotFoundException(id);
        }
        System.out.println("Get post in unlike method : " + post + " et son id: " + id);

        List<Integer> likedBy = post.getLikedBy();
        if (likedBy.contains(userId.intValue())) {
            likedBy.remove(userId.intValue());
            System.out.println("post unliked with success : " + post);
        }

        post.setLikes(post.getLikes() - 1);
        post.setLikedBy(likedBy);
        postRepository.update(post);
    }

    public List<PostEntity> getLikedPosts(Long userId) {
        return postRepository.findByLikedBy(userId);
    }
}
