package diallo.services;

import diallo.entities.PostEntity;
import diallo.repositories.PostRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class TriService {

    @Inject
    PostRepository postRepository;

    public List<PostEntity> getMostRecentPosts() {
        return postRepository.listAll().stream()
                .sorted((p1, p2) -> p2.getDate().compareTo(p1.getDate()))
                .toList();
    }

    public List<PostEntity> getMostLikedPosts() {
        return postRepository.listAll().stream()
                .sorted((p1, p2) -> p2.getLikedBy().size() - p1.getLikedBy().size())
                .toList();
    }

    public List<PostEntity> getMostCommentedPosts() {
        return postRepository.listAll().stream()
                .sorted((p1, p2) -> p2.getComments().size() - p1.getComments().size())
                .toList();
    }

    public List<PostEntity> getMostSharedPosts() {
        return postRepository.listAll().stream()
                .sorted((p1, p2) -> p2.getSharedBy().size() - p1.getSharedBy().size())
                .toList();
    }

    public List<PostEntity> searchPosts(String keyword) {
        return postRepository.searchByKeyword(keyword);
    }
}

