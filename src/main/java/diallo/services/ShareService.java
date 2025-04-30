package diallo.services;

import diallo.entities.PostEntity;
import diallo.repositories.PostRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.util.List;

@ApplicationScoped
public class ShareService {

    @Inject
    PostRepository postRepository;

    public PostEntity sharePost(ObjectId id, Long userId) throws PostNotFoundException {
        PostEntity post = postRepository.findById(id);
        if (post == null) {
            throw new PostNotFoundException(id);
        }

        System.out.println("Get post in share method : " + post + " et son id: " + id);

        List<Integer> sharedBy = post.getSharedBy();
        if (!sharedBy.contains(userId.intValue())) {
            sharedBy.add(userId.intValue());
            System.out.println("post shared with success : " + post);
        }

        post.setSharedBy(sharedBy);
        postRepository.update(post);
        return post;
    }

    public void unsharePost(ObjectId id, Long userId) throws PostNotFoundException {
        PostEntity post = postRepository.findById(id);
        if (post == null) {
            throw new PostNotFoundException(id);
        }

        List<Integer> sharedBy = post.getSharedBy();
        if (sharedBy.contains(userId.intValue())) {
            sharedBy.remove(userId.intValue());
            System.out.println("post unshared with success : " + post);
        }

        post.setSharedBy(sharedBy);
        postRepository.update(post);
    }

    public List<PostEntity> getSharedPosts(Long userId) {
        return postRepository.findBySharedBy(userId);
    }
}
