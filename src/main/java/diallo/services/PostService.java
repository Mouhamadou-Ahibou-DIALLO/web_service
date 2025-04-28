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

    public PostEntity addPost(Long createdBy, CreatePostRequest createPostRequest) {
        PostEntity post = new PostEntity();

        post.setBody(createPostRequest.body());
        post.setDate(createPostRequest.date());
        post.setHour(createPostRequest.hour());
        post.setHashtags(createPostRequest.hashtags());
        post.setImage(createPostRequest.image());
        post.setTitle(createPostRequest.title());
        post.setCreatedBy(createdBy);

        postRepository.persist(post);
        System.out.println("post added with success : " + post);
        return post;
    }

    public List<PostEntity> findByCreatedBy(Long createdBy) {
        return postRepository.findByCreatedBy(createdBy);
    }

    public PostEntity updatePost(ObjectId _id, PostUpdatedRequest updateRequest) throws PostNotFoundException {
        PostEntity post = postRepository.findById(_id);
        if (post == null) {
            throw new PostNotFoundException(_id);
        }

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

    public void deletePost(ObjectId _id) throws PostNotFoundException {
        PostEntity post = postRepository.findById(_id);
        if (post == null) {
            throw new PostNotFoundException(_id);
        }

        System.out.println("post deleted with success : " + post);
        postRepository.delete(post);
    }


}
