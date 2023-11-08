package net.controller;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import net.helper.Keys;
import net.model.User;
import net.service.PostService;

@WebServlet(urlPatterns = { "/posts" })
@MultipartConfig(maxFileSize = 16177215) // Handle file uploads up to 16MB
public class PostController extends HttpServlet {
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<String, String> options = new HashMap<>();
		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(Keys.USER);
		InputStream inputStream = null;

		String action = request.getParameter("action");
		try {
			if (action != null && action.equals("add-post")) {
				// Retrieve the image file part
				Part imagePart = request.getPart("image-post");
				if (imagePart != null) {
					try {
						// get input stream of the uploaded file
						inputStream = imagePart.getInputStream();
						// Retrieve the text content of the post
						String postContent = request.getParameter("post-content");

						// Call a method in PostService to handle the image post
						PostService.addNewPost(user.getId(), inputStream, postContent);

					} finally {
						// Cleanup: Close the input stream and delete the temporary file
						if (inputStream != null) {
							inputStream.close();
						}
						imagePart.delete();
					}
				} else {
					session.setAttribute(Keys.ERROR, "Cannot upload your image post. Try again later!");
				}
			} else if (action != null && action.equals("add-comment")) {
				int postId = Integer.parseInt(request.getParameter("post-id"));
	            String commentContent = request.getParameter("comment-content");
	            String commentHtml = PostService.addNewComment(user.getId(), postId, commentContent); // Assuming this method returns HTML for the new comment
	            options.put("commentHtml", commentHtml); // Add the comment HTML to the response options
	            options.put("result", "OK");
	           }
			options.put("result", "OK");
		} catch (SQLException ex) {
			options.put("result", "FAILED");
			Logger.getLogger(PostController.class.getName()).log(Level.SEVERE, null, ex);
		}

		String json = new Gson().toJson(options);
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(json);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		processRequest(request, response);
	}
}
