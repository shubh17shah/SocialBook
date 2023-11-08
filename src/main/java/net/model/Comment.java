package net.model;

public class Comment {
    private int id;
    private String content;
    private String creationDate;
    private User author;
    public Comment(int id, String content, String creationDate, User author) {
        this.id = id;
        this.content = content;
        this.creationDate = creationDate;
        this.author = author;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public String getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
    public User getAuthor() {
        return author;
    }
    public void setAuthor(User author) {
        this.author = author;
    }
}