package net.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.helper.Keys;
import net.model.FriendRequest;
import net.model.FriendRequest;
import net.model.User;
import net.service.FriendService;
import net.service.UserService;

@WebServlet(urlPatterns = {"/friend-requests"})
public class FriendRequestController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            User me = (User) session.getAttribute(Keys.USER);

            // Retrieve pending friend requests for the user
            List<FriendRequest> pendingRequests = FriendService.getPendingFriendRequests(me.getId());

            // Set the pending friend requests in the request scope
            request.setAttribute("pendingRequests", pendingRequests);

            // Forward the user to the friend-requests.jsp page
            request.getRequestDispatcher("/WEB-INF/friend-requests.jsp").forward(request, response);
        } catch (SQLException ex) {
            // Handle the exception
            response.getWriter().write("Failed to retrieve friend requests: " + ex.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession sess = request.getSession();
            User me = (User) sess.getAttribute(Keys.USER);

            String friendIdStr = request.getParameter("friendId");
            if (friendIdStr != null) {
                int friendId = Integer.parseInt(friendIdStr);
                boolean requestSent = UserService.sendFriendRequest(me.getId(), friendId);
                if (requestSent) {
                    // Successfully sent a friend request
                    response.getWriter().write("Friend request sent successfully.");
                    response.setStatus(HttpServletResponse.SC_OK);
                } else {
                    // Failed to send a friend request
                    response.getWriter().write("Failed to send friend request.");
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            }
        } catch (SQLException ex) {
            // Handle the exception
            response.getWriter().write("Failed to send friend request: " + ex.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
