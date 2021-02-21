package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {

    public static final int MEAL_ID_START = UserTestData.ADMIN_ID + 1;
    public static final int NON_EXISTENT_MEAL_ID = 100;

    public static final Meal userMeal1 = new Meal(MEAL_ID_START, LocalDateTime.of(2020,
            Month.JANUARY, 30, 10, 0), "Завтрак 1", 500);
    public static final Meal userMeal2 = new Meal(MEAL_ID_START + 1, LocalDateTime.of(2020,
            Month.JANUARY, 30, 13, 0), "Обед 1", 1000);
    public static final Meal userMeal3 = new Meal(MEAL_ID_START + 2, LocalDateTime.of(2020,
            Month.JANUARY, 30, 20, 0), "Ужин 1", 500);
    public static final Meal userMeal4 = new Meal(MEAL_ID_START + 3, LocalDateTime.of(2020,
            Month.JANUARY, 31, 0, 0), "Еда на граничное значение 1", 100);
    public static final Meal userMeal5 = new Meal(MEAL_ID_START + 4, LocalDateTime.of(2020,
            Month.JANUARY, 31, 10, 0), "Завтрак 1", 1000);
    public static final Meal userMeal6 = new Meal(MEAL_ID_START + 5, LocalDateTime.of(2020,
            Month.JANUARY, 31, 13, 0), "Обед 1", 500);
    public static final Meal userMeal7 = new Meal(MEAL_ID_START + 6, LocalDateTime.of(2020,
            Month.JANUARY, 31, 20, 0), "Ужин 1", 410);

    public static final Meal adminMeal1 = new Meal(MEAL_ID_START + 7, LocalDateTime.of(2020, Month.JANUARY,
            30, 10, 0), "Завтрак 2", 500);
    public static final Meal adminMeal2 = new Meal(MEAL_ID_START + 8, LocalDateTime.of(2020, Month.JANUARY,
            30, 13, 0), "Обед 2", 1000);
    public static final Meal adminMeal3 = new Meal(MEAL_ID_START + 9, LocalDateTime.of(2020, Month.JANUARY,
            30, 20, 0), "Ужин 2", 500);
    public static final Meal adminMeal4 = new Meal(MEAL_ID_START + 10, LocalDateTime.of(2020, Month.JANUARY,
            31, 0, 0), "Еда на граничное значение 2", 100);
    public static final Meal adminMeal5 = new Meal(MEAL_ID_START + 11, LocalDateTime.of(2020, Month.JANUARY,
            31, 10, 0), "Завтрак 2", 1000);
    public static final Meal adminMeal6 = new Meal(MEAL_ID_START + 12, LocalDateTime.of(2020, Month.JANUARY,
            31, 13, 0), "Обед 2", 500);
    public static final Meal adminMeal7 = new Meal(MEAL_ID_START + 13, LocalDateTime.of(2020, Month.JANUARY,
            31, 20, 0), "Ужин 2", 410);

    public static Meal getNew() {
        return new Meal(LocalDateTime.of(2018, Month.DECEMBER, 20, 11, 30),
                "new", 750);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(userMeal1);
        updated.setDateTime(LocalDateTime.of(2018, Month.NOVEMBER, 10, 14, 0));
        updated.setDescription("updated");
        updated.setCalories(1250);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static List<Meal> getUserMeals() {
        return Arrays.asList(
                userMeal7, userMeal6, userMeal5, userMeal4, userMeal3, userMeal2, userMeal1
        );
    }

    public static List<Meal> getAdminMeals() {
        return Arrays.asList(
                adminMeal7, adminMeal6, adminMeal5, adminMeal4, adminMeal3, adminMeal2, adminMeal1
        );
    }
}