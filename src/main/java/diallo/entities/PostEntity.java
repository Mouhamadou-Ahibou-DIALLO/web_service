package diallo.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.types.ObjectId;

import java.util.List;

@MongoEntity(collection = "CERISoNet", database = "db-CERI")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostEntity extends PanacheMongoEntity {

    public ObjectId _id;
    public String date;
    public String hour;
    public int createdBy;
    public String body;
    public int likes;
    public List<Integer> likedBy;
    public List<String> hashtags;
    public List<Comment> comments;
    public List<Image> images;
    public List<Integer> sharedBy;

    public PostEntity() {
        this._id = new ObjectId();
    }

    public PostEntity(String date, String hour, int createdBy, String body) {
        this._id = new ObjectId();
        this.date = date;
        this.hour = hour;
        this.createdBy = createdBy;
        this.body = body;
        this.likes = 0;
        this.likedBy = new java.util.ArrayList<>();
        this.hashtags = new java.util.ArrayList<>();
        this.comments = new java.util.ArrayList<>();
        this.images = new java.util.ArrayList<>();
        this.sharedBy = new java.util.ArrayList<>();
    }

    @Override
    public String toString() {
        return "PostEntity{" +
                "_id=" + _id +
                ", date='" + date + '\'' +
                ", hour='" + hour + '\'' +
                ", createdBy=" + createdBy +
                ", body='" + body + '\'' +
                ", likes=" + likes +
                ", likedBy=" + likedBy +
                ", hashtags=" + hashtags +
                ", comments=" + comments +
                ", images=" + images +
                ", sharedBy=" + sharedBy +
                '}';
    }

    public static class Comment {
        public ObjectId _id;
        public String text;
        public int commentedBy;
        public String date;
        public String hour;

        public Comment() {
            this._id = new ObjectId();
        }

        public Comment(String text, int commentedBy, String date, String hour) {
            this._id = new ObjectId();
            this.text = text;
            this.commentedBy = commentedBy;
            this.date = date;
            this.hour = hour;
        }

        @Override
        public String toString() {
            return "Comment{" +
                    "_id=" + _id +
                    ", text='" + text + '\'' +
                    ", commentedBy=" + commentedBy +
                    ", date='" + date + '\'' +
                    ", hour='" + hour + '\'' +
                    '}';
        }
    }

    public static class Image {
        public String url;
        public String title;

        public Image() {}

        public Image(String url, String title) {
            this.url = url;
            this.title = title;
        }

        @Override
        public String toString() {
            return "Image{" +
                    "url='" + url + '\'' +
                    ", title='" + title + '\'' +
                    '}';
        }
    }
}
