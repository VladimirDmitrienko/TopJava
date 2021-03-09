package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;
import ru.javawebinar.topjava.util.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.UserTestData.*;


@ActiveProfiles(profiles = Profiles.DATAJPA)
public class DataJpaUserServiceTest extends AbstractUserServiceTest {

    @Test
    public void getWithMeals() {
        User actual = service.getWithMeals(ADMIN_ID);
        List<Meal> actualMeals = actual.getMeals();
        USER_MATCHER.assertMatch(actual, admin);
        MealTestData.MEAL_MATCHER.assertMatch(actualMeals, MealTestData.adminMeal2, MealTestData.adminMeal1);
    }

    @Test
    public void getWithNoMeals() {
        int newId = service.create(getNew()).id();
        User user = service.getWithMeals(newId);
        MealTestData.MEAL_MATCHER.assertMatch(user.getMeals(), Collections.emptyList());
    }

    @Test
    public void getWithMealsNotFound() {
        assertThrows(NotFoundException.class, () -> service.getWithMeals(NOT_FOUND));
    }
}