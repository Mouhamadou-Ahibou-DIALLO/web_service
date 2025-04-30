package diallo.controllers;

import diallo.entities.PostEntity;
import diallo.services.PostNotFoundException;
import diallo.services.ShareService;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.bson.types.ObjectId;

import java.util.List;

@ApplicationScoped
@Path("/share")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShareController {

    @Inject
    ShareService shareService;

    @GET
    public Response getSharedPosts(Long userId) {
        List<PostEntity> sharedPosts = shareService.getSharedPosts(userId);
        return Response.ok(sharedPosts).build();
    }

    @POST
    @Path("/{postId}/{userId}")
    public Response sharePost(@PathParam("postId") String postId, @PathParam("userId") Long userId) {
        try {
            PostEntity sharedPost = shareService.sharePost(new ObjectId(postId), userId);
            return Response.ok(sharedPost).build();
        } catch (PostNotFoundException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{postId}/{userId}")
    public Response unsharePost(@PathParam("postId") String postId, @PathParam("userId") Long userId) {
        try {
            shareService.unsharePost(new ObjectId(postId), userId);
            return Response.ok().build();
        } catch (PostNotFoundException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }
}
