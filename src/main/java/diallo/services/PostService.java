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

    public PostEntity getPostById(ObjectId id) {
        PostEntity post = postRepository.findById(id);
        System.out.println("Get post by id : " + post + " et son id: " + id);
        return post;
    }

    public PostEntity addPost(Long createdBy, CreatePostRequest createPostRequest) {
        PostEntity post = new PostEntity();

        post.setBody(createPostRequest.body());
        post.setDate(createPostRequest.date());
        post.setHour(createPostRequest.hour());
        post.setHashtags(createPostRequest.hashtags());
        post.setImage(createPostRequest.image());
        post.setTitle(createPostRequest.title());
        post.setCreatedBy(createdBy);
        post.setLikes(0);

        postRepository.persist(post);
        System.out.println("post added with success : " + post);
        return post;
    }

    public List<PostEntity> findByCreatedBy(Long createdBy) {
        return postRepository.findByCreatedBy(createdBy);
    }

    public PostEntity updatePost(ObjectId id, PostUpdatedRequest updateRequest) throws PostNotFoundException {
        PostEntity post = postRepository.findById(id);
        if (post == null) {
            throw new PostNotFoundException(id);
        }
        System.out.println("Get post in update method : " + post + " et son id: " + id);

        post.setTitle(updateRequest.title());
        post.setBody(updateRequest.body());
        post.setDate(updateRequest.date());
        post.setHour(updateRequest.hour());
        post.setImage(updateRequest.image());
        post.setHashtags(updateRequest.hashtags());

        postRepository.update(post);
        System.out.println("post updated with success : " + post);
        return post;
    }

    public void deletePost(ObjectId id) throws PostNotFoundException {
        PostEntity post = postRepository.findById(id);
        if (post == null) {
            throw new PostNotFoundException(id);
        }
        System.out.println("Get post in delete method : " + post + " et son id: " + id);

        System.out.println("post deleted with success : " + post);
        postRepository.delete(post);
    }
}
