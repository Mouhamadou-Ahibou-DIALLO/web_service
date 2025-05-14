package diallo.services;

import diallo.entities.PostEntity;
import diallo.repositories.PostRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;

import java.util.Iterator;
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

        PostEntity.Comment comment = new PostEntity.Comment(
                createCommentRequest.text(),
                createCommentRequest.commentedBy(),
                createCommentRequest.date(),
                createCommentRequest.hour()
        );

        post.getComments().add(comment);
        postRepository.update(post);
        System.out.println("Comment added with success : " + comment);
        return comment;
    }

    public void deleteComment(ObjectId postId, ObjectId commentId) throws PostNotFoundException {
        PostEntity post = postRepository.findById(postId);
        if (post == null) {
            throw new PostNotFoundException(postId);
        }

        Iterator<PostEntity.Comment> iterator = post.getComments().iterator();
        boolean found = false;
        while (iterator.hasNext()) {
            PostEntity.Comment c = iterator.next();
            if (c._id.equals(commentId)) {
                iterator.remove();
                found = true;
                break;
            }
        }

        if (found) {
            postRepository.update(post);
            System.out.println("Comment deleted successfully.");
        } else {
            System.out.println("Comment not found.");
        }
    }

    public List<PostEntity> getByCommentedBy(Long commentedBy) {
        return postRepository.findByCommentedBy(commentedBy);
    }
}
