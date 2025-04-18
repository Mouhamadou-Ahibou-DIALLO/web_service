package diallo.controllers;

import diallo.entities.PostEntity;
import diallo.services.PostService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import java.util.List;

@ApplicationScoped
@Path("/posts")
public class PostController {

    @Inject
    PostService postService;

    @GET
    public List<PostEntity> getAllPosts() {
        System.out.println("liste des posts");
        return postService.getAllPosts();
    }
}
