package diallo.services;

public record CreateCommentRequest(String text, int commentedBy, String date, String hour) {}
