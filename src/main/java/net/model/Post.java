package net.model;
import java.util.List;
public class Post {
    private int id;
    private String imagePost;
    private String content;
    private String creationDate;
    private User author;
    private List<Comment> commentList;
    public Post(int id, String imagePost, String content, String creationDate, User author, List<Comment> commentList) {
        this.id = id;
        this.imagePost = imagePost;
        this.content = content;
        this.creationDate = creationDate;
        this.author = author;
        this.commentList = commentList;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getImagePost() {
        return imagePost;
    }
    public void setImagePost(String imagePost) {
        this.imagePost = imagePost;
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
    public List<Comment> getCommentList() {
        return commentList;
    }
    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }
}