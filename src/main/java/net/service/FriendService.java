package net.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import net.model.FriendRequest;
import net.model.FriendRequest;
import net.model.User;

public class FriendService {

    public static List<FriendRequest> getPendingFriendRequests(int userId) throws SQLException {
        List<FriendRequest> pendingRequests = new ArrayList<>();
        String select = "SELECT fr.id AS request_id, u.id AS sender_id, u.first_name AS sender_first_name, u.last_name AS sender_last_name, u.user_name AS sender_user_name, fr.status " +
                        "FROM tbl_friend_request fr " +
                        "INNER JOIN tbl_user u ON fr.sender_id = u.id " +
                        "WHERE fr.receiver_id = ? AND fr.status = 'Pending'";
        
        try (Connection c = DBService.openConnection(); 
             PreparedStatement pStmt = c.prepareStatement(select)) {
            pStmt.setInt(1, userId);
            ResultSet rs = pStmt.executeQuery();
            
            while (rs.next()) {
                int requestId = rs.getInt("request_id");
                int senderId = rs.getInt("sender_id");
                String senderFirstName = rs.getString("sender_first_name");
                String senderLastName = rs.getString("sender_last_name");
                String senderUserName = rs.getString("sender_user_name");
                String status = rs.getString("status");
                
                User sender = new User(senderId, senderFirstName, senderLastName, senderUserName);
                FriendRequest request = new FriendRequest(requestId, sender, null, status);
                pendingRequests.add(request);
            }
        }
        
        return pendingRequests;
    }
    
    // Implement methods for accepting and rejecting friend requests if needed
}
