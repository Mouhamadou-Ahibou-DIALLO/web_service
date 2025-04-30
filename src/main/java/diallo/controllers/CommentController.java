package diallo.controllers;

import diallo.entities.PostEntity;
import diallo.services.CommentsService;
import diallo.services.CreateCommentRequest;
import diallo.services.PostNotFoundException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;

import java.util.List;

@ApplicationScoped
@Path("/comments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CommentController {

    @Inject
    CommentsService commentsService;

    @GET
    @Path("/{commentedBy}")
    public Response getCommentsByCommentedBy(@PathParam("commentedBy") Long commentedBy) {
        List<PostEntity> getCommentsByCommentedBy = commentsService.getByCommentedBy(commentedBy);
        return Response.ok(getCommentsByCommentedBy).build();
    }

    @POST
    @Path("/add/{id}")
    public Response addComment(CreateCommentRequest createCommentRequest, @PathParam("id") String id) {
        try {
            PostEntity.Comment comment = commentsService.addComment(createCommentRequest, new ObjectId(id));
            return Response.ok(comment).build();
        } catch (PostNotFoundException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/delete/{id}/{commentId}")
    public Response deleteComment(@PathParam("id") String id, @PathParam("commentId") String commentId) {
        try {
            commentsService.deleteComment(new ObjectId(id), new ObjectId(commentId));
            return Response.ok().build();
        } catch (PostNotFoundException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
