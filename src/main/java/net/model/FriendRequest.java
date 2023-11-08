package net.model;

public class FriendRequest {
    private int id;
    private User sender;
    private User recipient;
    private String status;

    public FriendRequest(int id, User sender, User recipient, String status) {
        this.id = id;
        this.sender = sender;
        this.recipient = recipient;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public User getSender() {
        return sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public String getStatus() {
        return status;
    }
}
