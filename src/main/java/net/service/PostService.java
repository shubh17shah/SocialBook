package net.service;

import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.text.SimpleDateFormat;

import net.helper.StringHelper;
import net.model.Comment;
import net.model.Post;
import net.model.User;

public class PostService {
	public static List<Comment> getCommentList(int postId) throws SQLException {
		List<Comment> commentList = new ArrayList<>();
		String select = "select * from tbl_comment where post_id = ? order by creation_date desc";
		try (Connection c = DBService.openConnection(); PreparedStatement pStmt = c.prepareStatement(select);) {
			pStmt.setInt(1, postId);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				String formattedDate = DBService.getFormattedDate(rs.getDate("creation_date"));
				User author = UserService.getUser(rs.getInt("user_id"));
				Comment comment = new Comment(rs.getInt("id"), rs.getString("content"), formattedDate, author);
				commentList.add(comment);
			}
		}
		return commentList;
	}

	public static List<Post> getPostList(int authorId) throws SQLException {
		List<Post> postList = new ArrayList<>();
		String select = "select * from tbl_post where user_id = ? or user_id in (select friend_to from tbl_friend where me = ?) order by creation_date desc";
		try (Connection c = DBService.openConnection(); PreparedStatement pStmt = c.prepareStatement(select);) {
			pStmt.setInt(1, authorId);
			pStmt.setInt(2, authorId);
			ResultSet rs = pStmt.executeQuery();
			while (rs.next()) {
				int postId = rs.getInt("id");
				String formattedDate = DBService.getFormattedDate(rs.getDate("creation_date"));
				List<Comment> comments = getCommentList(postId);
				User author = UserService.getUser(rs.getInt("user_id"));

				// Convert the image post from Base64
				String imagePostBase64 = StringHelper.convertImageBase64(rs.getBytes("image"));

				// Create a Post object with the converted image post
				Post post = new Post(postId, imagePostBase64, rs.getString("content"), formattedDate, author, comments);
				postList.add(post);
			}
		}
		return postList;
	}

	public static int addNewPost(int authorId, InputStream inputStream, String content) throws SQLException {
		String insert = "Insert into tbl_post Values(null,?,?,NOW(3),?)";
		try (Connection c = DBService.openConnection(); PreparedStatement pStmt = c.prepareStatement(insert)) {
			pStmt.setBlob(1, inputStream);
			pStmt.setString(2, content);
			pStmt.setInt(3, authorId);
			return pStmt.executeUpdate();
		}
	}

	public static String addNewComment(int authorId, int postId, String content) throws SQLException {
		String insert = "Insert into tbl_comment Values(null,?,NOW(3),?,?)";
		try (Connection c = DBService.openConnection();
				PreparedStatement pStmt = c.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS)) {
			pStmt.setString(1, content);
			pStmt.setInt(2, postId);
			pStmt.setInt(3, authorId);
			int rowsAffected = pStmt.executeUpdate();
			if (rowsAffected > 0) {
				ResultSet generatedKeys = pStmt.getGeneratedKeys();
				if (generatedKeys.next()) {
					int commentId = generatedKeys.getInt(1);
					// Assuming you have a method to retrieve the comment as HTML
					String commentHtml = getCommentHtml(commentId);
					return commentHtml;
				}
			}
		}
		return null; // Return null if the comment was not added or there was an error

	}

	public static String getCommentHtml(int commentId) {
	    String commentHtml = "";

	    try (Connection connection = DBService.openConnection();
	         PreparedStatement statement = connection.prepareStatement(
	             "SELECT c.content, u.user_name, u.avatar, c.creation_date " +
	             "FROM tbl_comment c " +
	             "JOIN tbl_user u ON c.user_id = u.id " +
	             "WHERE c.id = ?")) {
	        statement.setInt(1, commentId);
	        ResultSet resultSet = statement.executeQuery();
	        if (resultSet.next()) {
	            String content = resultSet.getString("content");
	            String userName = resultSet.getString("user_name");
	            Blob avatarBlob = resultSet.getBlob("avatar");
	            Date date = resultSet.getTimestamp("creation_date");
	            //String creationDate;
	            //SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy, hh:mm a", new Locale("en", "IN")); // Format for Indian time

	            //creationDate = sdf.format(date); // Format the Date object to a String
	            Date creationDate = resultSet.getTimestamp("creation_date");
	            Date now = new Date(); // Current time

	            long timeDifference = now.getTime() - creationDate.getTime();
	            long seconds = timeDifference / 1000;
	            long minutes = seconds / 60;
	            long hours = minutes / 60;
	            long days = hours / 24;

	            String timeAgo;
	            if (days > 0) {
	                timeAgo = days + " day(s) ago";
	            } else if (hours > 0) {
	                timeAgo = hours + " hour(s) ago";
	            } else if (minutes > 0) {
	                timeAgo = minutes + " minute(s) ago";
	            } else {
	                timeAgo = seconds + " second(s) ago";
	            }
	            // Convert the BLOB avatar data to base64
	            String avatarBase64 = StringHelper.convertBase64(avatarBlob != null ? avatarBlob.getBytes(1, (int) avatarBlob.length()) : null);

	            // Create the HTML structure with the comment details and the avatar image
	            commentHtml = "<div class='flex'>" +
	                    "<div class='w-10 h-10 rounded-full relative flex-shrink-0'>" +
	                    "<img src='data:image/png;base64," + avatarBase64 + "' alt='" + userName + "' class='absolute h-full rounded-full w-full'>" +
	                    "</div>" +
	                    "<div style='margin-top:10px; margin-left:10px'>" + userName + "</div>" +
	                    "<div>" +
	                    "<div class='text-gray-700 py-2 px-3 rounded-md bg-gray-100 relative lg:ml-5 ml-2 lg:mr-12 dark:bg-gray-800 dark:text-gray-100'>" +
	                    "<p class='leading-6'>" + content + "</p>" +
	                    "<div class='absolute w-3 h-3 top-3 -left-1 bg-gray-100 transform rotate-45 dark:bg-gray-800'></div>" +
	                    "</div>" +
	                    "<div class='text-sm flex items-center space-x-3 mt-2 ml-5'>" +
	                    	                    
	                    "<span>" + timeAgo + "</span>" +
	                    "</div>" +
	                    "</div>" +
	                    "</div>";
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return commentHtml;
	}
	
	
	public static int countUserPosts(int userId) throws SQLException {
        String select = "select count(*) as total from tbl_post where user_id=?";
        try (Connection c = DBService.openConnection();
                PreparedStatement ps = c.prepareStatement(select)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
        }
        return 0;
    }

	public static int countUserFriends(int userId) throws SQLException {
	    String select = "SELECT COUNT(*) AS total FROM tbl_friend WHERE me = ?";
	    try (Connection c = DBService.openConnection(); PreparedStatement ps = c.prepareStatement(select)) {
	        ps.setInt(1, userId);
	        try (ResultSet rs = ps.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt("total");
	            }
	        }
	    }
	    return 0;
	}

}