package net.controller;

import java.io.IOException;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.helper.Keys;
import net.model.User;
import net.service.UserService;

@WebServlet(urlPatterns = {"/friend-request-actions"})
public class FriendRequestActionsController extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            User me = (User) session.getAttribute(Keys.USER);

            String action = request.getParameter("action");
            int requestId = Integer.parseInt(request.getParameter("requestId"));

            if ("accept".equals(action)) {
                // Accept friend request
            	int senderId = UserService.acceptFriendRequest(requestId, me.getId());

            	if (senderId != -1) {
            	    // Successfully accepted the friend request and added to tbl_friend
            	    response.sendRedirect(request.getContextPath() + "/friend-requests");
            	} else {
            	    // Friend request not found or already accepted
            	    response.getWriter().write("Friend request not found or already accepted.");
            	    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            	}
            } else if ("reject".equals(action)) {
                // Reject friend request
                boolean requestRejected = UserService.rejectFriendRequest(requestId, me.getId());

                if (requestRejected) {
                    // Successfully rejected the friend request
                    response.sendRedirect(request.getContextPath() + "/friend-requests");
                } else {
                    // Friend request not found or already rejected
                    response.getWriter().write("Friend request not found or already rejected.");
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            }
        } catch (SQLException ex) {
            // Handle the exception
            response.getWriter().write("Failed to process friend request: " + ex.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
