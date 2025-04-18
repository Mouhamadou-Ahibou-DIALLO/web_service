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
        return postRepository.listAll();
    }
}
