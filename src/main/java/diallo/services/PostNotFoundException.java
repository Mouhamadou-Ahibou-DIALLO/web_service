package diallo.services;

import org.bson.types.ObjectId;

public class PostNotFoundException extends Exception {

    public PostNotFoundException(ObjectId _id) {
        super("Post " + _id + " introuvable");
    }
}
