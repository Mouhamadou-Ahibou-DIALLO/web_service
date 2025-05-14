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

        List<Integer> sharedBy = post.getSharedBy();
        int uid = userId.intValue();

        if (!sharedBy.contains(uid)) {
            sharedBy.add(uid);
            post.setSharedBy(sharedBy);
            postRepository.update(post);
            System.out.println("Post shared with success");
        }

        return post;
    }

    public void unsharePost(ObjectId id, Long userId) throws PostNotFoundException {
        PostEntity post = postRepository.findById(id);
        if (post == null) {
            throw new PostNotFoundException(id);
        }

        List<Integer> sharedBy = post.getSharedBy();
        int uid = userId.intValue();

        if (sharedBy.contains(uid)) {
            sharedBy.remove((Integer) uid);
            post.setSharedBy(sharedBy);
            postRepository.update(post);
            System.out.println("Post unshared with success");
        }
    }

    public List<PostEntity> getSharedPosts(Long userId) {
        return postRepository.findBySharedBy(userId);
    }
}
