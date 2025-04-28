package diallo.controllers;

import diallo.entities.PostEntity;
import diallo.services.CreatePostRequest;
import diallo.services.PostNotFoundException;
import diallo.services.PostService;

import diallo.services.PostUpdatedRequest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;

import java.util.List;

@ApplicationScoped
@Path("/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostController {

    @Inject
    PostService postService;

    @GET
    public List<PostEntity> getAllPosts() {
        return postService.getAllPosts();
    }

    @POST
    @Path("/{userId}")
    public Response addPost(@PathParam("userId") long userId, CreatePostRequest post) {
        PostEntity addedPost = postService.addPost(userId, post);
        return Response.status(Response.Status.CREATED).entity("Post added with success: " + addedPost).build();
    }

    @GET
    @Path("/createdBy/{userId}")
    public Response getPostsByCreator(@PathParam("userId") long userId) {
        List<PostEntity> posts = postService.findByCreatedBy(userId);
        return Response.ok(posts).build();
    }

    @GET
    @Path("/{id}")
    public Response getPostById(@PathParam("id") String id) {
        PostEntity post = postService.getPostById(new ObjectId(id));
        return Response.ok(post).build();
    }

    @PUT
    @Path("/{id}")
    public Response UpdatePost(@PathParam("id") String id, PostUpdatedRequest updateRequest) {
        try {
            PostEntity updatedPost = postService.updatePost(new ObjectId(id), updateRequest);
            return Response.ok(updatedPost).build();
        } catch (PostNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deletePost(@PathParam("id") String id) {
        try {
            postService.deletePost(new ObjectId(id));
            return Response.ok("Post deleted with success").build();
        } catch (PostNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        }
    }
}
