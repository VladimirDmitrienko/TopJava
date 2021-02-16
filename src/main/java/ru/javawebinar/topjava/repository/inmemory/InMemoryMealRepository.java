package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {

    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepository.class);

    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.firstUserMeals.forEach(meal -> save(meal, 1));
        MealsUtil.secondUserMeals.forEach(meal -> save(meal, 2));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.info("save {}", meal);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            Map<Integer, Meal> mealMap = repository.computeIfAbsent(userId, id -> new ConcurrentHashMap<>());
            mealMap.put(meal.getId(), meal);
            return meal;
        }
        Map<Integer, Meal> mealMap = repository.get(userId);
        if (mealMap == null) {
            return null;
        }
        // handle case: update but not present in storage
        return mealMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {}", id);
        Map<Integer, Meal> mealMap = repository.get(userId);
        if (mealMap == null) {
            return false;
        }
        return mealMap.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {}", id);
        Map<Integer, Meal> mealMap = repository.get(userId);
        if (mealMap == null) {
            return null;
        }
        return mealMap.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        log.info("getAll");
        Map<Integer, Meal> meals = repository.get(userId);
        if (meals == null) {
            return Collections.emptyList();
        }
        return filterByPredicate(meals.values(), meal -> true);
    }

    public List<Meal> getBetweenInclusive(int userId, LocalDate startDate, LocalDate endDate) {
        log.info("getAll between dates: {} {}", startDate, endDate);
        Map<Integer, Meal> meals = repository.get(userId);
        if (meals == null) {
            return Collections.emptyList();
        }
        return filterByPredicate(meals.values(), meal -> DateTimeUtil.isBetweenInclusive(meal.getDate(), startDate, endDate));
    }

    private List<Meal> filterByPredicate(Collection<Meal> meals, Predicate<Meal> filter) {
        return meals.stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }
}