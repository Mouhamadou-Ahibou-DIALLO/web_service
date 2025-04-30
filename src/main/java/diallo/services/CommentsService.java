package diallo.services;

import diallo.entities.PostEntity;
import diallo.repositories.PostRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.util.List;

@ApplicationScoped
public class CommentsService {

    @Inject
    PostRepository postRepository;

    public PostEntity.Comment addComment(CreateCommentRequest createCommentRequest, ObjectId id) throws PostNotFoundException {
        PostEntity post = postRepository.findById(id);
        if (post == null) {
            throw new PostNotFoundException(id);
        }

        PostEntity.Comment comment = new PostEntity.Comment(createCommentRequest.text(), createCommentRequest.commentedBy(), createCommentRequest.date(), createCommentRequest.hour());
        post.getComments().add(comment);

        postRepository.update(post);
        System.out.println("Comment added with success : " + comment);
        return comment;
    }

    public void deleteComment(ObjectId id, ObjectId commentId) throws PostNotFoundException {
        PostEntity post = postRepository.findById(id);
        if (post == null) {
            throw new PostNotFoundException(id);
        }

        PostEntity.Comment comment = post.getComments().stream().filter(c -> c._id.equals(commentId)).findFirst().orElse(null);
        post.getComments().remove(comment);

        postRepository.update(post);
        System.out.println("Comment deleted with success : " + comment);
    }

    public List<PostEntity> getByCommentedBy(Long commentedBy) {
        return postRepository.findByCommentedBy(commentedBy);
    }
}
