package diallo.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.quarkus.mongodb.panache.PanacheMongoEntityBase;
import io.quarkus.mongodb.panache.common.MongoEntity;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.util.List;

@MongoEntity(collection = "CERISoNet", database = "db-CERI")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostEntity extends PanacheMongoEntityBase {

    @BsonId
    public int _id;
    public String date;
    public String hour;
    public int createdBy;
    public String body;
    public int likes;
    public List<Integer> likedBy = new java.util.ArrayList<>();
    public List<String> hashtags = new java.util.ArrayList<>();
    public List<Comment> comments = new java.util.ArrayList<>();
    public List<Image> images = new java.util.ArrayList<>();
    public List<Integer> sharedBy = new java.util.ArrayList<>();

    public PostEntity() {}

    public PostEntity(int _id, String date, String hour, int createdBy, String body,
                      int likes, List<Integer> likedBy, List<String> hashtags,
                      List<Comment> comments, List<Image> images, List<Integer> sharedBy) {
        this._id = _id;
        this.date = date;
        this.hour = hour;
        this.createdBy = createdBy;
        this.body = body;
        this.likes = likes;
        this.likedBy = likedBy;
        this.hashtags = hashtags;
        this.comments = comments;
        this.images = images;
        this.sharedBy = sharedBy;
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
        @BsonProperty("_id")
        @BsonId
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
