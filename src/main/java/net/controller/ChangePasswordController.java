package net.controller;

import java.io.IOException;
import java.sql.SQLException;
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
import net.helper.Validator;
import net.model.User;
import net.service.UserService;

@WebServlet("/changepassword")
public class ChangePasswordController extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.setAttribute(Keys.ERROR, "");
        session.setAttribute(Keys.SUCCESS, "");

        String action = request.getParameter("action");

        if (action != null && action.equals("change-password")) {
            User user = (User) session.getAttribute(Keys.USER);
            String currentPassword = request.getParameter("current-password");
            String newPassword = request.getParameter("new-password");
            String confirmNewPassword = request.getParameter("confirm-new-password");

            // Check if the current password matches the database password
            if (!Validator.checkPassword(currentPassword) || !currentPassword.equals(user.getPassword())) {
                session.setAttribute(Keys.ERROR, "Current password is incorrect.");
            } else if (!Validator.checkPassword(newPassword)) {
                session.setAttribute(Keys.ERROR, "New password should contain valid characters.");
            } else if (newPassword.equals(currentPassword)) {
                session.setAttribute(Keys.ERROR, "New password should not be current password");
            }else if (!newPassword.equals(confirmNewPassword)) {
                session.setAttribute(Keys.ERROR, "New passwords do not match.");
            } else {
                try {
                    if (UserService.changePassword(user.getuserName(), newPassword) == Validator.SUCCESS) {
//                        session.setAttribute(Keys.SUCCESS, "Password has been changed successfully.");
                    	session.invalidate();
                    	response.sendRedirect("index.jsp");
                    	return;
                    } else {
                        session.setAttribute(Keys.ERROR, "Something went wrong. Please try again.");
                    }
                } catch (SQLException ex) {
                    Logger.getLogger(ChangePasswordController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        RequestDispatcher dis = request.getRequestDispatcher("WEB-INF/changepassword.jsp");
        dis.forward(request, response);
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
