package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.storage.MealStorage;
import ru.javawebinar.topjava.storage.MealStorageInMemory;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.*;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);

    private MealStorage mealStorage = new MealStorageInMemory();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String idFromRequest = request.getParameter("id");
        String dateTimeFromRequest = request.getParameter("dateTime");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeFromRequest, TimeUtil.ISO_DATE_TIME_FORMATTER);
        String description = request.getParameter("description");
        String caloriesFromRequest = request.getParameter("calories");
        int calories = Integer.parseInt(caloriesFromRequest);

        Meal meal;
        if (idFromRequest.isEmpty()) {
            meal = new Meal(MealsUtil.incrementAndGetCounter(), dateTime, description, calories);
        } else {
            Integer id = Integer.parseInt(idFromRequest);
            meal = new Meal(id, dateTime, description, calories);
        }
        log.debug("save {}", meal);
        mealStorage.save(meal);

        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        List<MealTo> mealsTo = filteredByStreams(MealsUtil.getMeals(), LocalTime.MIN, LocalTime.MAX, 2000);

        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("mealsTo", mealsTo);
            log.debug("forward to meals");
            request.getRequestDispatcher("meals.jsp").forward(request, response);
            return;
        }

        String idFromRequest = request.getParameter("id");
        Integer id = null;
        if (idFromRequest != null) {
            id = Integer.parseInt(idFromRequest);
        }

        Meal meal = null;
        switch (action) {
            case "delete":
                log.debug("delete meal with id={}", id);
                mealStorage.delete(id);
                response.sendRedirect("meals");
                return;
            case "add":
                log.debug("add new meal");
                meal = new Meal(null, LocalDateTime.now(), "Description", 100);
                break;
            case "edit":
                log.debug("edit meal with id={}", id);
                meal = mealStorage.get(id);
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("meal", meal);
        request.setAttribute("action", action);
        request.getRequestDispatcher("edit.jsp").forward(request, response);
    }
}
