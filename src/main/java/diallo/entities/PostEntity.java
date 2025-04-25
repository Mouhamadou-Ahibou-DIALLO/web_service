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

    public void setLikedBy(List<Integer> likedBy) {
        this.likedBy = likedBy;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public void setHashtags(List<String> hashtags) {
        this.hashtags = hashtags;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setSharedBy(List<Integer> sharedBy) {
        this.sharedBy = sharedBy;
    }

    public String getDate() {
        return date;
    }

    public String getHour() {
        return hour;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public String getBody() {
        return body;
    }

    public int getLikes() {
        return likes;
    }

    public List<Integer> getLikedBy() {
        return likedBy;
    }

    public List<String> getHashtags() {
        return hashtags;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public Image getImage() {
        return image;
    }

    public List<Integer> getSharedBy() {
        return sharedBy;
    }

    @Override
    public String toString() {
        return "PostEntity{" +
                "date='" + date + '\'' +
                ", hour='" + hour + '\'' +
                ", createdBy=" + createdBy +
                ", body='" + body + '\'' +
                ", likes=" + likes +
                ", likedBy=" + likedBy +
                ", hashtags=" + hashtags +
                ", comments=" + comments +
                ", image=" + image +
                ", sharedBy=" + sharedBy +
                '}';
    }

    public PostEntity(String date, String hour, String body, int createdBy, int likes, List<Integer> likedBy, List<String> hashtags, List<Comment> comments, Image image, List<Integer> sharedBy) {
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
