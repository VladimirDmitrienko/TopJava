package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {

    protected final Logger log = LoggerFactory.getLogger(MealRestController.class);

    private MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealTo> getAll() {
        int userId = SecurityUtil.authUserId();
        log.info("getAll for user {}", userId);
        return MealsUtil.getTos(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay());
    }

    public Meal get(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("get {} for user {}", id, userId);
        return service.get(id, userId);
    }

    public Meal create(Meal meal) {
        checkNew(meal);
        int userId = SecurityUtil.authUserId();
        log.info("create {} for user {}", meal.getId(), userId);
        return service.create(meal, userId);
    }

    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("delete {} for user {}", id, userId);
        service.delete(id, userId);
    }

    public void update(Meal meal, int id) {
        assureIdConsistent(meal, id);
        int userId = SecurityUtil.authUserId();
        log.info("update {} for user {}", meal.getId(), userId);
        service.update(meal, userId);
    }

    public List<MealTo> getBetween(LocalDate startDate, LocalDate endDate,
                                   LocalTime startTime, LocalTime endTime) {
        startDate = startDate == null ? LocalDate.MIN : startDate;
        endDate = endDate == null ? LocalDate.MAX : endDate;
        startTime = startTime == null ? LocalTime.MIN : startTime;
        endTime = endTime == null ? LocalTime.MAX : endTime;
        int userId = SecurityUtil.authUserId();
        log.info("getAll between dates: {} {} and time {} {} for user {}",
                startDate, endDate, startTime, endTime, userId);
        Collection<Meal> mealsFilteredByDate = service.getBetweenInclusive(userId, startDate, endDate);

        return MealsUtil.getFilteredTos(mealsFilteredByDate, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }
}