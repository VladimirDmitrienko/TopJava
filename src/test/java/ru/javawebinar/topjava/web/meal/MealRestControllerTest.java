package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_CALORIES_PER_DAY;
import static ru.javawebinar.topjava.util.MealsUtil.getTos;

class MealRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MealRestController.REST_URL + '/';

    @Autowired
    MealService mealService;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_TO_MATCHER.contentJson(mealTos));
    }

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + meal1.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(meal1));
    }

    @Test
    void delete() throws Exception {
        List<Meal> mealsWithoutDeleted = new ArrayList<>(meals);
        mealsWithoutDeleted.remove(meal1);
        perform(MockMvcRequestBuilders.delete(REST_URL + meal1.getId()))
                .andExpect(status().isNoContent());
        MEAL_MATCHER.assertMatch(mealService.getAll(UserTestData.USER_ID), mealsWithoutDeleted);
    }

    @Test
    void update() throws Exception {
        Meal updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + updated.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isOk());

        MEAL_MATCHER.assertMatch(mealService.get(MEAL1_ID, SecurityUtil.authUserId()), updated);
    }

    @Test
    void create() throws Exception {
        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getNew())))
                .andExpect(status().isCreated());

        Meal actual = mealService.get(ADMIN_MEAL_ID + 2, SecurityUtil.authUserId());
        Meal expected = getNew();
        expected.setId(ADMIN_MEAL_ID + 2);

        MEAL_MATCHER.assertMatch(actual, expected);
    }

    @Test
    void getBetween() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "filter/")
                .param("startDate", "2020-01-30")
                .param("endDate", "2020-01-30")
                .param("startTime", "10:00")
                .param("endTime", "20:00"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_TO_MATCHER.contentJson(getTos(List.of(meal2, meal1), DEFAULT_CALORIES_PER_DAY)));
    }

//    HW7 2.2 implementation test
//    @Test
//    void getBetween() throws Exception {
//        perform(MockMvcRequestBuilders.get(REST_URL + "filter/")
//                .param("start", "2020-01-30T10:00")
//                .param("end", "2020-01-30T20:00"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(MEAL_TO_MATCHER.contentJson(getTos(List.of(meal2, meal1), DEFAULT_CALORIES_PER_DAY)));
//    }

    @Test
    void getBetweenWithNull() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "filter/")
                .param("startDate", "")
                .param("endDate", "")
                .param("startTime", "")
                .param("endTime", ""))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_TO_MATCHER.contentJson(mealTos));
    }
}