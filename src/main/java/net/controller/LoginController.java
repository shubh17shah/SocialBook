package net.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.helper.Keys;
import net.model.User;
import net.service.UserService;

@WebServlet("/login")
public class LoginController extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String emailOrUsername = request.getParameter("user-login-id");
            String password = request.getParameter("user-login-password");
            HttpSession session = request.getSession();

            User user = UserService.checkLogin(emailOrUsername, password);
            
            if (user != null) {
                session.setAttribute(Keys.USER, user);
                if (session.getAttribute(Keys.CURRENT_URL) == null) {
                    response.sendRedirect("feed");
                } else {
                    String curUrl = session.getAttribute(Keys.CURRENT_URL).toString();
                    session.setAttribute(Keys.CURRENT_URL, null);
                    response.sendRedirect(curUrl);
                }
                session.setAttribute(Keys.ERROR, "");
            } 
            else {
                session.setAttribute(Keys.ERROR, "Invalid login information");
                response.sendRedirect("index.jsp");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
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
