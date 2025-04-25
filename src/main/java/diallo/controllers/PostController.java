package diallo.controllers;

import diallo.entities.PostEntity;
import diallo.services.PostService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
    public Response addPost(PostEntity post) {
        PostEntity addedPost = postService.addPost(post);
        return Response.status(Response.Status.CREATED).entity("Post added with success: " + addedPost).build();
    }

    @GET
    @Path("/createdBy/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPostsByCreator(@PathParam("userId") long userId) {
        List<PostEntity> posts = postService.findByCreatedBy((int) userId);
        return Response.ok(posts).build();
    }
}
