package diallo.controllers;

import diallo.entities.PostEntity;
import diallo.services.LikeService;
import diallo.services.PostNotFoundException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;

import java.util.List;

@ApplicationScoped
@Path("/likes")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class LikeController {

    @Inject
    LikeService likeService;

    @POST
    @Path("/like/{postId}/{userId}")
    public Response likePost(@PathParam("postId") String postId, @PathParam("userId") Long userId) {
        try {
            likeService.likePost(new ObjectId(postId), userId);
            return Response.ok().build();
        } catch (PostNotFoundException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @POST
    @Path("/unlike/{postId}/{userId}")
    public Response unlikePost(@PathParam("postId") String postId, @PathParam("userId") Long userId) {
        try {
            likeService.unlikePost(new ObjectId(postId), userId);
            return Response.ok().build();
        } catch (PostNotFoundException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/liked/{userId}")
    public Response getLikedPosts(@PathParam("userId") Long userId) {
        List<PostEntity> likedPosts = likeService.getLikedPosts(userId);
        return Response.ok(likedPosts).build();
    }
}
