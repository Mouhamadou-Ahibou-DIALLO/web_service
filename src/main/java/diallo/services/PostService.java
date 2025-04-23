package diallo.services;

import diallo.entities.PostEntity;
import diallo.repositories.PostRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

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
}
