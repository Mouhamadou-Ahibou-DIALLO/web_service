package diallo.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.quarkus.mongodb.panache.common.MongoEntity;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

@MongoEntity(collection = "postsAhibou", database = "db-CERI")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostEntity extends PanacheMongoEntity {

    public String date;
    public String hour;
    public int createdBy;
    public String body;
    public int likes;
    public List<Integer> likedBy = new ArrayList<>();
    public List<String> hashtags = new ArrayList<>();
    public List<Comment> comments = new ArrayList<>();
    public Image image;
    public List<Integer> sharedBy = new ArrayList<>();

    public PostEntity() {}

    public PostEntity(String date, String hour,String body, int createdBy, int likes, List<Integer> likedBy, List<String> hashtags, List<Comment> comments, Image image, List<Integer> sharedBy) {
        this.date = date;
        this.hour = hour;
        this.body = body;
        this.createdBy = createdBy;
        this.likes = likes;
        this.likedBy = likedBy;
        this.hashtags = hashtags;
        this.comments = comments;
        this.image = image;
        this.sharedBy = sharedBy;
    }

    public static class Comment {
        public ObjectId _id = new ObjectId();
        public String text;
        public int commentedBy;
        public String date;
        public String hour;
    }

    public static class Image {
        public String url;
        public String title;
    }
}
