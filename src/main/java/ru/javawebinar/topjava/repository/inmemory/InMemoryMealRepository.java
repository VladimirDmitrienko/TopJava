package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {

    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.firstUserMeals.forEach(meal -> save(meal, 1));
        MealsUtil.secondUserMeals.forEach(meal -> save(meal, 2));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.merge(userId, new ConcurrentHashMap<Integer, Meal>() {{ put(meal.getId(), meal);}},
                    (oldMap, newMap) -> {
                oldMap.putAll(newMap);
                return oldMap; });
            return meal;
        }
        AtomicReference<Meal> computedMeal = new AtomicReference<>();
        repository.computeIfPresent(userId, (id, map) -> {
            computedMeal.set(map.computeIfPresent(meal.getId(), (mealId, oldMeal) -> meal));
            return map;
        });
        return computedMeal.get();
    }

    @Override
    public boolean delete(int id, int userId) {
        return repository.getOrDefault(userId, Collections.emptyMap()).remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        return repository.getOrDefault(userId, Collections.emptyMap()).get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        Collection<Meal> meals = repository.getOrDefault(userId, Collections.emptyMap()).values();
        if (meals.isEmpty()) {
            return new ArrayList<>(meals);
        }
        return filterByPredicate(meals, meal -> true);
    }

    public List<Meal> getBetweenInclusive(int userId, LocalDate startDate, LocalDate endDate) {
        Collection<Meal> meals = repository.getOrDefault(userId, Collections.emptyMap()).values();
        if (meals.isEmpty()) {
            return new ArrayList<>(meals);
        }
        return filterByPredicate(meals, meal -> DateTimeUtil.isBetweenInclusive(meal.getDate(), startDate, endDate));
    }

    private List<Meal> filterByPredicate(Collection<Meal> meals, Predicate<Meal> filter) {
        return meals.stream()
                .filter(filter)
                .sorted(Comparator.comparing(Meal::getDateTime, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }
}