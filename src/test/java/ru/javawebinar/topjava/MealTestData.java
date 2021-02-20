package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {

    private static final List<Meal> userMeals = Arrays.asList(
            new Meal(100_002, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак 1", 500),
            new Meal(100_003, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед 1", 1000),
            new Meal(100_004, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин 1", 500),
            new Meal(100_005, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение 1", 100),
            new Meal(100_006, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак 1", 1000),
            new Meal(100_007, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед 1", 500),
            new Meal(100_008, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин 1", 410)
    );

    private static final List<Meal> adminMeals = Arrays.asList(
            new Meal(100_009, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак 2", 500),
            new Meal(100_010, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед 2", 1000),
            new Meal(100_011, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин 2", 500),
            new Meal(100_012, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение 2", 100),
            new Meal(100_013, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак 2", 1000),
            new Meal(100_014, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед 2", 500),
            new Meal(100_015, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин 2", 410)
    );

    public static Meal getNew() {
        return new Meal(LocalDateTime.of(2018, Month.DECEMBER, 20, 11, 30),
                "new", 750);
    }

    public static Meal getUpdated() {
        return new Meal(100_005, LocalDateTime.of(2018, Month.NOVEMBER, 10, 14, 0),
                "updated", 1000);
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }

    public static List<Meal> getUserMeals() {
        return userMeals.stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    public static List<Meal> getAdminMeals() {
        return adminMeals.stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }
}