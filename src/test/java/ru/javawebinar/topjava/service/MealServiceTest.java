package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.Util;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    private MealService mealService;

    public MealServiceTest() {
    }

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Test
    public void create() {
        Meal created = mealService.create(getNew(), UserTestData.USER_ID);
        Integer createdId = created.getId();
        Meal newMeal = getNew();
        newMeal.setId(createdId);
        assertMatch(created, newMeal);
        assertMatch(mealService.get(createdId, UserTestData.USER_ID), newMeal);
    }

    @Test
    public void duplicatedTimeCreate() {
        LocalDateTime dateTime = getUserMeals().get(0).getDateTime();
        Meal newMeal = getNew();
        newMeal.setDateTime(dateTime);
        assertThrows(DuplicateKeyException.class, () -> mealService.create(newMeal, UserTestData.USER_ID));
    }

    @Test
    public void get() {
        Meal meal = mealService.get(getUserMeals().get(0).getId(), UserTestData.USER_ID);
        assertMatch(meal, getUserMeals().get(0));
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () ->
                mealService.get(getUserMeals().get(0).getId(), UserTestData.ADMIN_ID));
    }

    @Test
    public void getBetweenInclusive() {
        LocalDate startDate = LocalDate.of(2019, Month.JANUARY, 30);
        LocalDate endDate = LocalDate.of(2020, Month.JANUARY, 30);
        List<Meal> userMeals = mealService.getBetweenInclusive(startDate, endDate, UserTestData.USER_ID);
        List<Meal> testMeals = getUserMeals().stream()
                .filter(meal -> Util.isBetweenHalfOpen(meal.getDate(), startDate, endDate.plus(1, ChronoUnit.DAYS)))
                .collect(Collectors.toList());
        assertMatch(userMeals, testMeals);
    }

    @Test
    public void getAll() {
        List<Meal> userMeals = mealService.getAll(UserTestData.USER_ID);
        List<Meal> adminMeals = mealService.getAll(UserTestData.ADMIN_ID);
        assertMatch(userMeals, getUserMeals());
        assertMatch(adminMeals, getAdminMeals());
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        mealService.update(updated, UserTestData.USER_ID);
        assertMatch(updated, mealService.get(updated.getId(), UserTestData.USER_ID));
    }

    @Test
    public void updateNotFound() {
        Meal updated = getUpdated();
        assertThrows(NotFoundException.class, () ->
                mealService.update(updated, UserTestData.ADMIN_ID));
    }

    @Test
    public void delete() {
        mealService.delete(getUserMeals().get(0).getId(), UserTestData.USER_ID);
        assertThrows(NotFoundException.class, () ->
                mealService.delete(getUserMeals().get(0).getId(), UserTestData.USER_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () ->
                mealService.delete(getUserMeals().get(0).getId(), UserTestData.ADMIN_ID));
    }
}