package net.controller;

import net.helper.Keys;
import net.model.User;
import net.service.UserService;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/remove-friend")
public class RemoveFriendController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int friend = Integer.parseInt(request.getParameter("friend"));

            HttpSession sess = request.getSession();
            User me = (User) sess.getAttribute(Keys.USER);
            UserService.removeFriend(me.getId(), friend);
            response.getWriter().write("Friend removed successfully");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid friend ID");
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to remove friend");
            e.printStackTrace();
        }
    }
}

