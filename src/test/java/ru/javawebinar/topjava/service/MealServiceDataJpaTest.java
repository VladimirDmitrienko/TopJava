package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.model.User;

import static ru.javawebinar.topjava.MealTestData.adminMeal1;
import static ru.javawebinar.topjava.UserTestData.USER_MATCHER;
import static ru.javawebinar.topjava.UserTestData.admin;


@ActiveProfiles(profiles = {"hsqldb", "datajpa", "dev"})
public class MealServiceDataJpaTest extends AbstractMealServiceTest {

    @Test
    public void getWithUser() {
        User actual = service.getWithUser(adminMeal1.id(), admin.id()).getUser();
        USER_MATCHER.assertMatch(actual, admin);
    }
}