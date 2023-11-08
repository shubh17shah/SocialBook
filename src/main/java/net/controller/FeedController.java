package net.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.helper.Keys;
import net.model.Post;
import net.model.User;
import net.service.PostService;
import net.service.UserService;

@WebServlet("/feed")
public class FeedController extends HttpServlet {
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			// Load posts
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute(Keys.USER);
			List<Post> postList = PostService.getPostList(user.getId());
			session.setAttribute(Keys.POST_LIST, postList);

			RequestDispatcher dis = request.getRequestDispatcher("WEB-INF/feed.jsp");
			dis.forward(request, response);
		} catch (SQLException ex) {
			Logger.getLogger(FeedController.class.getName()).log(Level.SEVERE, null, ex);
		}
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