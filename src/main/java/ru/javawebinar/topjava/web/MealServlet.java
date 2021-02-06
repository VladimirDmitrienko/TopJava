package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoImpl;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private static final Logger log = getLogger(MealServlet.class);
    private static final int CALORIES_LIMIT = 2000;
    private static final String CREATE_OR_UPDATE = "createOrUpdateMeal.jsp";
    private static final String LIST = "meals.jsp";

    private final MealDao mealDao = new MealDaoImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        if (action.equalsIgnoreCase("list")) {
            processListAction(request, response);
        }
        else if (action.equalsIgnoreCase("createOrUpdate")) {
            processCreateOrUpdateAction(request, response);
        }
        else if (action.equalsIgnoreCase("delete")) {
            processDeleteAction(request, response);
        }
    }

    private void processListAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("meals", getMealToList());
        log.debug("forward to meals");
        request.getRequestDispatcher(LIST).forward(request, response);
    }

    private void processDeleteAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        mealDao.delete(id);
        processListAction(request, response);
    }

    private void processCreateOrUpdateAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("id") == null) {
            log.debug("forward to create meal");
        }
        else {
            int id = Integer.parseInt(request.getParameter("id"));
            request.setAttribute("meal", mealDao.get(id));
            log.debug("forward to update meal");
        }
        request.getRequestDispatcher(CREATE_OR_UPDATE).forward(request, response);
    }

    private List<MealTo> getMealToList() {
        return MealsUtil.filter(mealDao.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_LIMIT);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        if (action.equalsIgnoreCase("createOrUpdate")) {
            int id = req.getParameter("id").isEmpty() ? -1 : Integer.parseInt(req.getParameter("id"));
            String description = req.getParameter("description");
            int calories = Integer.parseInt(req.getParameter("calories"));
            LocalDateTime dateTime = LocalDateTime.parse(req.getParameter("dateTime"));

            if (id == -1) {
                mealDao.create(dateTime, description, calories);
            }
            else {
                mealDao.update(id, dateTime, description, calories);
            }
        }
        processListAction(req, resp);
    }
}