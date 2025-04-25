package diallo.services;

import diallo.entities.PostEntity;
import diallo.repositories.PostRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.util.List;

@ApplicationScoped
public class PostService {

    @Inject
    PostRepository postRepository;

    public List<PostEntity> getAllPosts() {
        List<PostEntity> posts = postRepository.listAll();
        System.out.println("Liste des posts: " + posts);
        return posts;
    }

    public PostEntity addPost(PostEntity post) {
        System.out.println("post added : " + post);
        postRepository.persist(post);
        return post;
    }

    public List<PostEntity> findByCreatedBy(int createdBy) {
        return postRepository.findByCreatdBy(createdBy);
    }

    public PostEntity updatePost(String id, PostEntity updatedPost) {
        PostEntity post = postRepository.findById(new ObjectId(id));
        if (post == null) {
            return null;
        }
        post.setBody(updatedPost.getBody());
        post.setDate(updatedPost.getDate());
        post.setHour(updatedPost.getHour());
        post.setHashtags(updatedPost.getHashtags());
        post.setImage(updatedPost.getImage());

        System.out.println("post updated with success: " + post);
        postRepository.persist(post);
        return post;
    }

    public void deletePost(String id) {
        PostEntity post = postRepository.findById(new ObjectId(id));
        if (post != null) {
            System.out.println("Post deleted with success: " + post);
            postRepository.delete(post);
        }
    }

    public PostEntity findById(String id) {
        return postRepository.findById(new ObjectId(id));
    }
}
