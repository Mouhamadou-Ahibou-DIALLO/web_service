package diallo.services;

import diallo.entities.PostEntity;

import java.util.List;

public record CreatePostRequest(String title, String date, String hour, String body, PostEntity.Image image,
                                List<String> hashtags) {}