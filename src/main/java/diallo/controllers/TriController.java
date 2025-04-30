package diallo.controllers;

import diallo.entities.PostEntity;
import diallo.services.TriService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@ApplicationScoped
@Path("/tri")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TriController {

    @Inject
    TriService triService;

    @GET
    @Path("/search")
    public Response searchPosts(@QueryParam("keyword") String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Mot-clé requis").build();
        }
        List<PostEntity> posts = triService.searchPosts(keyword);
        return Response.ok(posts).build();
    }

    @GET
    @Path("/recent")
    public Response getMostRecentPosts() {
        List<PostEntity> posts = triService.getMostRecentPosts();
        return Response.ok(posts).build();
    }

    @GET
    @Path("/liked")
    public Response getMostLikedPosts() {
        List<PostEntity> posts = triService.getMostLikedPosts();
        return Response.ok(posts).build();
    }

    @GET
    @Path("/commented")
    public Response getMostCommentedPosts() {
        List<PostEntity> posts = triService.getMostCommentedPosts();
        return Response.ok(posts).build();
    }

    @GET
    @Path("/shared")
    public Response getMostSharedPosts() {
        List<PostEntity> posts = triService.getMostSharedPosts();
        return Response.ok(posts).build();
    }
}
