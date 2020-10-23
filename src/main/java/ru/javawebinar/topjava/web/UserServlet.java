package ru.javawebinar.topjava.web;

import org.slf4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.web.SecurityUtil.setAuthUserId;

public class UserServlet extends HttpServlet {
    private static final Logger log = getLogger(UserServlet.class);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = getUserId(request);
        log.info("setAuthUserId {}", userId);
        setAuthUserId(userId);
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forward to users");
        request.getRequestDispatcher("/users.jsp").forward(request, response);
    }

    private int getUserId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("userId"));
        return Integer.parseInt(paramId);
    }
}
