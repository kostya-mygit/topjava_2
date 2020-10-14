package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.MealStorage;
import ru.javawebinar.topjava.storage.MealStorageInMemory;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

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
            meal = new Meal(dateTime, description, calories);
        } else {
            meal = new Meal(getId(request), dateTime, description, calories);
        }
        log.debug(meal.isNew() ? "add {}" : "update {}", meal);
        mealStorage.save(meal);

        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        if (action == null) {
            log.debug("getAll");
            request.setAttribute("mealsTo", filteredByStreams(mealStorage.getAll(), LocalTime.MIN, LocalTime.MAX, DEFAULT_CALORIES_PER_DAY));
            request.getRequestDispatcher("/meals.jsp").forward(request, response);
            return;
        }

        Meal meal = null;
        switch (action) {
            case "delete":
                int id = getId(request);
                log.debug("delete meal with id={}", id);
                mealStorage.delete(id);
                response.sendRedirect("meals");
                return;
            case "add":
                meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 100);
                break;
            case "edit":
                meal = mealStorage.get(getId(request));
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("meal", meal);
        request.setAttribute("action", action);
        request.getRequestDispatcher("/edit.jsp").forward(request, response);
    }

    private int getId(HttpServletRequest request) {
        String idFromRequest = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(idFromRequest);
    }
}
