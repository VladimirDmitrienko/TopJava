package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private static final Logger log = getLogger(MealServlet.class);
    private static final int CALORIES_LIMIT = 2000;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("forward to meals");

        List<MealTo> mealToList = MealsUtil.filterMeals(MealsUtil.getMealList(),
                LocalTime.MIN, LocalTime.MAX, CALORIES_LIMIT);

        request.setAttribute("meals", mealToList);
        request.getRequestDispatcher("meals.jsp").forward(request, response);
    }
}