package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;
import static ru.javawebinar.topjava.web.SecurityUtil.setAuthUserId;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealRestController controller;
    private ConfigurableApplicationContext appCtx;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        controller = appCtx.getBean(MealRestController.class);
    }

    @Override
    public void destroy() {
        appCtx.close();
        super.destroy();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")),
                authUserId());

        if (meal.isNew()) {
            log.info("Create {}", meal);
            controller.create(meal);
        } else {
            log.info("Update {} with id {}", meal, id);
            controller.update(meal, getId(request));
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "auth":
                int userId = getUserId(request);
                log.info("setAuthUserId {}", userId);
                setAuthUserId(userId);
                response.sendRedirect("meals");
                break;
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                controller.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, authUserId()) :
                        controller.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                List<MealTo> meals;
                String startDateFromRequest = request.getParameter("startDate");
                String endDateFromRequest = request.getParameter("endDate");
                String startTimeFromRequest = request.getParameter("startTime");
                String endTimeFromRequest = request.getParameter("endTime");
                if (!isExist(startDateFromRequest) && !isExist(endDateFromRequest) && !isExist(startTimeFromRequest) && !isExist(endTimeFromRequest)) {
                    log.info("getAll");
                    meals = MealsUtil.getTos(controller.getAll(), MealsUtil.DEFAULT_CALORIES_PER_DAY);
                } else {
                    log.info("getAllFiltered startDate {} endDate {} startTime {} endTime {}", startDateFromRequest, endDateFromRequest, startTimeFromRequest, endTimeFromRequest);
                    LocalDate startDate = startDateFromRequest.isEmpty() ? LocalDate.MIN : LocalDate.parse(startDateFromRequest);
                    LocalDate endDate = endDateFromRequest.isEmpty() ? LocalDate.MAX : LocalDate.parse(endDateFromRequest);
                    LocalTime startTime = startTimeFromRequest.isEmpty() ? LocalTime.MIN : LocalTime.parse(startTimeFromRequest);
                    LocalTime endTime = endTimeFromRequest.isEmpty() ? LocalTime.MAX : LocalTime.parse(endTimeFromRequest);
                    meals = MealsUtil.getFilteredTos(controller.getAll(), MealsUtil.DEFAULT_CALORIES_PER_DAY, startDate, endDate, startTime, endTime);
                    request.setAttribute("startDate", startDate);
                    request.setAttribute("endDate", endDate);
                    request.setAttribute("startTime", startTime);
                    request.setAttribute("endTime", endTime);
                }
                request.setAttribute("meals", meals);
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    private int getUserId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("userId"));
        return Integer.parseInt(paramId);
    }

    private boolean isExist(String parameter) {
        return parameter != null && !parameter.isEmpty();
    }
}
