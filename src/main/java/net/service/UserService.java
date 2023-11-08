package net.service;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.helper.StringHelper;
import net.model.User;

public class UserService {
	public static boolean isDuplicateEmail(String email) throws SQLException {
		String select = "select id from tbl_user where email = ?";
		try (Connection c = DBService.openConnection(); PreparedStatement pStmt = c.prepareStatement(select);) {
			pStmt.setString(1, email);
			return pStmt.executeQuery().next();
		}
	}

	public static boolean isEmail(String email) throws SQLException {
		String select = "select id from tbl_user where email = ?";
		try (Connection c = DBService.openConnection(); PreparedStatement pStmt = c.prepareStatement(select);) {
			pStmt.setString(1, email);
			return pStmt.executeQuery().next();
		}
	}

	public static boolean isDuplicateUserName(String userName) throws SQLException {
		String select = "select id from tbl_user where user_name = ?";
		try (Connection c = DBService.openConnection(); PreparedStatement pStmt = c.prepareStatement(select);) {
			pStmt.setString(1, userName);
			return pStmt.executeQuery().next();
		}
	}

	public static int addNewUser(String firstName, String lastName, String userName, String email, String password,
			String birthday, String gender) throws SQLException {
		String insert = "Insert into tbl_user Values(null,?,?,?,?,?,?,?,null)";
		try (Connection c = DBService.openConnection(); PreparedStatement pStmt = c.prepareStatement(insert)) {
			pStmt.setString(1, firstName);
			pStmt.setString(2, lastName);
			pStmt.setString(3, userName);
			pStmt.setString(4, email);
			pStmt.setString(5, password);
			pStmt.setString(6, birthday);
			pStmt.setString(7, gender);
			return pStmt.executeUpdate();
		}
	}

	public static User getUser(String emailOrUsername, String password) throws SQLException {
		String select = "select * from tbl_user " + "where (email = ? or user_name = ?) and password = ?";
		try (Connection c = DBService.openConnection(); PreparedStatement pStmt = c.prepareStatement(select);) {

			pStmt.setString(1, emailOrUsername);
			pStmt.setString(2, emailOrUsername);
			pStmt.setString(3, password);
			return extractUser(pStmt.executeQuery());
		}
	}

	public static User getUser(String userName) throws SQLException {
		String select = "select * from tbl_user " + "where user_name = ?";
		try (Connection c = DBService.openConnection(); PreparedStatement pStmt = c.prepareStatement(select);) {

			pStmt.setString(1, userName);
			return extractUser(pStmt.executeQuery());
		}
	}



	public static User getUser(int id) throws SQLException {
		String select = "select * from tbl_user " + "where id = ?";
		try (Connection c = DBService.openConnection(); PreparedStatement pStmt = c.prepareStatement(select);) {

			pStmt.setInt(1, id);
			return extractUser(pStmt.executeQuery());
		}
	}

	public static User checkLogin(String emailOrUsername, String password) throws SQLException {
		return getUser(emailOrUsername, password);
	}

	public static List<User> getFriendList(int me) throws SQLException {
		List<User> list = new ArrayList<>();
		String select = "select friend_to from tbl_friend where me = ?";
		try (Connection c = DBService.openConnection(); PreparedStatement pStmt = c.prepareStatement(select);) {

			pStmt.setInt(1, me);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				int friendId = rs.getInt("friend_to");
				User user = getUser(friendId);
				list.add(user);
			}
		}
		return list;
	}

	public static List<User> getSuggestedFriend(int me) throws SQLException {
	    List<User> list = new ArrayList<>();
	    String select = "SELECT * FROM tbl_user WHERE id != ? " +
	                    "AND id NOT IN (SELECT friend_to FROM tbl_friend WHERE me = ?) " +
	                    "AND id NOT IN (SELECT sender_id FROM tbl_friend_request WHERE receiver_id = ? AND status IN ('Pending', 'Accepted')) " +
	                    "AND id NOT IN (SELECT receiver_id FROM tbl_friend_request WHERE sender_id = ? AND status IN ('Pending', 'Accepted'))";
	    
	    try (Connection c = DBService.openConnection(); PreparedStatement ps = c.prepareStatement(select)) {
	        ps.setInt(1, me);
	        ps.setInt(2, me);
	        ps.setInt(3, me);
	        ps.setInt(4, me);
	        ResultSet rs = ps.executeQuery();
	        User user = null;
	        while ((user = extractUser(rs)) != null) {
	            list.add(user);
	        }
	    }
	    return list;
	}



	public static void addFriend(int me, int friend) throws SQLException {
		String insert = "Insert Into tbl_friend Values(null,?,?)";
		try (Connection c = DBService.openConnection(); PreparedStatement ps = c.prepareStatement(insert)) {
			ps.setInt(1, me);
			ps.setInt(2, friend);
			ps.executeUpdate();
		}
	}
	public static void removeFriendSuggestion(int me, int suggestionId) throws SQLException {
	    String deleteQuery = "DELETE FROM tbl_friend WHERE friend_to = ?";
	    try (Connection connection = DBService.openConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
	        preparedStatement.setInt(1, suggestionId);
	        
	    } 
	}
	
	public static int updateUserAvatar(InputStream inputStream, String username) throws SQLException {
		String insert = "Update tbl_user Set avatar = ? Where user_name = ? ";
		try (Connection c = DBService.openConnection()) {
			PreparedStatement pStmt = c.prepareStatement(insert);
			pStmt.setBlob(1, inputStream);
			pStmt.setString(2, username);
			return pStmt.executeUpdate();
		}
	}

	public static int updateUser(String firstName, String lastName, String newUserName, String gender,
			String currentUserName) throws SQLException {
		String insert = "Update tbl_user Set first_name = ?, last_name = ?, user_name = ?, gender = ? Where user_name = ?";
		try (Connection c = DBService.openConnection(); PreparedStatement pStmt = c.prepareStatement(insert)) {
			pStmt.setString(1, firstName);
			pStmt.setString(2, lastName);
			pStmt.setString(3, newUserName);
			pStmt.setString(4, gender);
			pStmt.setString(5, currentUserName);
			return pStmt.executeUpdate();
		}
	}
	
	public static boolean sendFriendRequest(int senderId, int receiverId) throws SQLException {
	    String insert = "INSERT INTO tbl_friend_request (sender_id, receiver_id, status) VALUES (?, ?, 'Pending')";
	    try (Connection c = DBService.openConnection();
	         PreparedStatement pStmt = c.prepareStatement(insert)) {
	        pStmt.setInt(1, senderId);
	        pStmt.setInt(2, receiverId);
	        return pStmt.executeUpdate() > 0;
	    }
	}
	public static int acceptFriendRequest(int requestId, int receiverId) throws SQLException {
	    String updateRequest = "UPDATE tbl_friend_request SET status = 'Accepted' WHERE id = ? AND status = 'Pending'";
	    String insertFriend = "INSERT INTO tbl_friend (me, friend_to) SELECT ?, ? FROM DUAL WHERE NOT EXISTS " +
	            "(SELECT 1 FROM tbl_friend WHERE (me = ? AND friend_to = ?))";

	    try (Connection c = DBService.openConnection()) {
	        int senderId = -1; // Initialize senderId to -1

	        // Update the friend request status
	        try (PreparedStatement pStmt1 = c.prepareStatement(updateRequest)) {
	            pStmt1.setInt(1, requestId);
	            int updatedRows = pStmt1.executeUpdate();

	            if (updatedRows > 0) {
	                // Retrieve the sender's ID
	                String selectSender = "SELECT sender_id FROM tbl_friend_request WHERE id = ?";
	                try (PreparedStatement pStmt = c.prepareStatement(selectSender)) {
	                    pStmt.setInt(1, requestId);
	                    ResultSet rs = pStmt.executeQuery();
	                    if (rs.next()) {
	                        senderId = rs.getInt("sender_id");
	                    }
	                }

	                // Add the mutual friendship if they are not already friends
	                try (PreparedStatement pStmt2 = c.prepareStatement(insertFriend)) {
	                    pStmt2.setInt(1, receiverId);
	                    pStmt2.setInt(2, senderId);
	                    pStmt2.setInt(3, receiverId);
	                    pStmt2.setInt(4, senderId);
	                    int rowsInserted = pStmt2.executeUpdate();

	                    if (rowsInserted > 0) {
	                        // Now, add the mutual friendship for the sender as well
	                        pStmt2.setInt(1, senderId);
	                        pStmt2.setInt(2, receiverId);
	                        pStmt2.setInt(3, senderId);
	                        pStmt2.setInt(4, receiverId);
	                        pStmt2.executeUpdate();
	                    }
	                }
	            }
	        }

	        return senderId;
	    }
	}






	private static int addFriendshipIfNotExists(Connection c, int userId1, int userId2) throws SQLException {
	    String checkFriendship = "SELECT 1 FROM tbl_friend WHERE ((me = ? AND friend_to = ?) OR (me = ? AND friend_to = ?))";
	    String insertFriendship = "INSERT INTO tbl_friend (me, friend_to) VALUES (?, ?)";

	    try (PreparedStatement checkStmt = c.prepareStatement(checkFriendship)) {
	        checkStmt.setInt(1, userId1);
	        checkStmt.setInt(2, userId2);
	        checkStmt.setInt(3, userId2);
	        checkStmt.setInt(4, userId1);

	        ResultSet resultSet = checkStmt.executeQuery();

	        if (!resultSet.next()) {
	            try (PreparedStatement insertStmt = c.prepareStatement(insertFriendship)) {
	                insertStmt.setInt(1, userId1);
	                insertStmt.setInt(2, userId2);
	                return insertStmt.executeUpdate();
	            }
	        }
	    }

	    return 0;
	}

		
	public static boolean rejectFriendRequest(int requestId, int receiverId) throws SQLException {
	    String updateRequest = "UPDATE tbl_friend_request SET status = 'Rejected' WHERE id = ? AND status = 'Pending'";
	    
	    try (Connection c = DBService.openConnection();
	         PreparedStatement pStmt = c.prepareStatement(updateRequest)) {
	        pStmt.setInt(1, requestId);
	        int updatedRows = pStmt.executeUpdate();
	        
	        if (updatedRows > 0) {
	            return true; // Successfully rejected the friend request
	        } else {
	            return false; // Friend request not found or already rejected
	        }
	    }
	}
	
	public static void removeFriend(int me, int friend) throws SQLException {
	    String deleteFriendQuery = "DELETE FROM tbl_friend WHERE (me = ? AND friend_to = ?) OR (me = ? AND friend_to = ?)";
	    String deleteFriendRequestQuery = "DELETE FROM tbl_friend_request WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?)";
	    
	    try (Connection c = DBService.openConnection();
	         PreparedStatement friendStmt = c.prepareStatement(deleteFriendQuery);
	         PreparedStatement friendRequestStmt = c.prepareStatement(deleteFriendRequestQuery)) {
	        friendStmt.setInt(1, me);
	        friendStmt.setInt(2, friend);
	        friendStmt.setInt(3, friend);
	        friendStmt.setInt(4, me);
	        
	        friendRequestStmt.setInt(1, me);
	        friendRequestStmt.setInt(2, friend);
	        friendRequestStmt.setInt(3, friend);
	        friendRequestStmt.setInt(4, me);
	        
	        c.setAutoCommit(false); // Start a transaction
	        try {
	            friendStmt.executeUpdate(); // Delete friend relationship
	            friendRequestStmt.executeUpdate(); // Delete friend request
	            
	            c.commit(); // Commit the transaction
	        } catch (SQLException e) {
	            c.rollback(); // Rollback the transaction if an error occurs
	            throw e;
	        }
	    }
	}



	
	public static int changePassword(String userName, String password) throws SQLException {
	    String update = "UPDATE tbl_user SET password = ? WHERE user_name = ?";
	    try (Connection c = DBService.openConnection(); PreparedStatement pStmt = c.prepareStatement(update)) {
	        pStmt.setString(1, password);
	        pStmt.setString(2, userName);
	        return pStmt.executeUpdate();
	    }
	}
	
	public static List<User> searchFriend(String keyword) throws SQLException {
        List<User> list = new ArrayList<>();
        String select = "Select * From tbl_user "
                + "Where first_name like ? OR last_name like ? OR user_name like ?";
        try (Connection c = DBService.openConnection();
             PreparedStatement ps = c.prepareStatement(select)){
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ps.setString(3, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            User user;
            while ((user = extractUser(rs)) != null) {
                list.add(user);
            }
        } 
        return list;
    }

	private static User extractUser(ResultSet rs) throws SQLException {
		User user = null;
		if (rs.next()) {
			String avatar = StringHelper.convertBase64(rs.getBytes("avatar"));
			user = new User(rs.getInt("id"), rs.getString("first_name"), rs.getString("last_name"),
					rs.getString("user_name"), rs.getString("email"), rs.getString("password"),
					rs.getString("birthday"), rs.getString("gender"), avatar);
		}
		return user;
	}
}