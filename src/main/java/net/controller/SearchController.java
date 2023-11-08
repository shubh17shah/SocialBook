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

import com.google.gson.Gson;

import net.model.User;
import net.service.UserService;

@WebServlet(urlPatterns = {"/search"})
public class SearchController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	try {
            String keyword = request.getParameter("search-friend");
            List<User> results = UserService.searchFriend(keyword);

            String action = request.getParameter("action");

            if (action != null && action.equals("ajax-search")) {
                // For AJAX search, return results as JSON
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");

                // Convert the list of User objects to JSON
                String json = new Gson().toJson(results);

                response.getWriter().write(json);
            } else {
                // For regular search, forward to a JSP page
                request.setAttribute("results", results);
                RequestDispatcher dis = request.getRequestDispatcher("search.jsp");
                dis.forward(request, response);
            }
        } catch (SQLException ex) {
            Logger.getLogger(SearchController.class.getName()).log(Level.SEVERE, null, ex);
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
