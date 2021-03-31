package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.javawebinar.topjava.TestUtil;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

class MealRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MealRestController.REST_URL + '/';

    @Autowired
    private MealService mealService;

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(mvcResult -> Assertions.assertIterableEquals(
                        mealTos, TestUtil.readListFromJsonMvcResult(mvcResult, MealTo.class))
                );
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
        MEAL_MATCHER.assertMatch(mealService.getAll(USER_ID), mealsWithoutDeleted);
    }

    @Test
    void update() throws Exception {
        Meal updated = getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + updated.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        MEAL_MATCHER.assertMatch(mealService.get(MEAL1_ID, USER_ID), updated);
    }

    @Test
    void create() throws Exception {
        Meal expected = getNew();
        expected.setId(ADMIN_MEAL_ID + 2);

        perform(MockMvcRequestBuilders.post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(getNew())))
                .andExpect(status().isCreated())
                .andExpect(mvcResult -> MEAL_MATCHER.assertMatch(
                        expected, TestUtil.readFromJsonMvcResult(mvcResult, Meal.class))
                );

        Meal actual = mealService.get(ADMIN_MEAL_ID + 2, UserTestData.USER_ID);
        MEAL_MATCHER.assertMatch(actual, expected);
    }

    @Test
    void getBetween() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "filter")
                .param("startDate", "2020-01-30")
                .param("endDate", "2020-01-31")
                .param("startTime", "10:00")
                .param("endTime", "20:00"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(mvcResult -> Assertions.assertIterableEquals(
                        List.of(mealTos.get(1), mealTos.get(2), mealTos.get(5), mealTos.get(6)),
                        TestUtil.readListFromJsonMvcResult(mvcResult, MealTo.class))
                );
    }

//    HW7 2.2 implementation test
//    @Test
//    void getBetween() throws Exception {
//        perform(MockMvcRequestBuilders.get(REST_URL + "filter/")
//                .param("start", "2020-01-30T10:00")
//                .param("end", "2020-01-31T20:00"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(mvcResult -> Assertions.assertIterableEquals(
//                        List.of(mealTos.get(1), mealTos.get(2), mealTos.get(5), mealTos.get(6)),
//                        TestUtil.readListFromJsonMvcResult(mvcResult, MealTo.class))
//                );
//    }

    @Test
    void getBetweenWithNull() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "filter")
                .param("endDate", "")
                .param("startTime", ""))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(mvcResult -> Assertions.assertIterableEquals(
                        mealTos,
                        TestUtil.readListFromJsonMvcResult(mvcResult, MealTo.class))
                );
    }
}