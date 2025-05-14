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

        List<Integer> likedBy = post.getLikedBy();
        int uid = userId.intValue();

        if (!likedBy.contains(uid)) {
            likedBy.add(uid);
            post.setLikes(post.getLikes() + 1);
            post.setLikedBy(likedBy);
            postRepository.update(post);
            System.out.println("Post liked with success");
        }
    }

    public void unlikePost(ObjectId id, Long userId) throws PostNotFoundException {
        PostEntity post = postRepository.findById(id);
        if (post == null) {
            throw new PostNotFoundException(id);
        }

        List<Integer> likedBy = post.getLikedBy();
        int uid = userId.intValue();

        if (likedBy.contains(uid)) {
            likedBy.remove((Integer) uid);
            post.setLikes(Math.max(0, post.getLikes() - 1));
            post.setLikedBy(likedBy);
            postRepository.update(post);
            System.out.println("Post unliked with success");
        }
    }

    public List<PostEntity> getLikedPosts(Long userId) {
        return postRepository.findByLikedBy(userId);
    }

    public List<Long> getUserIdsWhoLiked(String postId) {
        return postRepository.getUserIdsWhoLiked(postId);
    }
}
