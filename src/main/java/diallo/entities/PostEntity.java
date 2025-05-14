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

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getHour() {
        return hour;
    }

    public Long getCreatedBy() {
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

    private String title;
    private String date;
    private String hour;
    private Long createdBy;
    private String body;
    private int likes;

    private List<Integer> likedBy = new ArrayList<>();
    private List<String> hashtags = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();
    private Image image;
    private List<Integer> sharedBy = new ArrayList<>();

    public PostEntity() {}

    public void setLikedBy(List<Integer> likedBy) {
        this.likedBy = likedBy;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setCreatedBy(Long createdBy) {
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

    @Override
    public String toString() {
        return "PostEntity{" +
                "date='" + date + '\'' +
                ", hour='" + hour + '\'' +
                ", createdBy=" + createdBy +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", likes=" + likes +
                ", likedBy=" + likedBy +
                ", hashtags=" + hashtags +
                ", comments=" + comments +
                ", image=" + image +
                ", sharedBy=" + sharedBy +
                '}';
    }

    public static class Comment {
        public ObjectId _id;
        public String text;
        public int commentedBy;
        public String date;
        public String hour;

        public Comment() {}

        public Comment(String text, int commentedBy, String date, String hour) {
            _id = new ObjectId();
            this.text = text;
            this.commentedBy = commentedBy;
            this.date = date;
            this.hour = hour;
        }
    }

    public static class Image {
        public String url;
        public String title;
    }
}
