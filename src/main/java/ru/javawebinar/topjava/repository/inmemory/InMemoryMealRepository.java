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
            repository.computeIfAbsent(userId, (id) ->
                    new ConcurrentHashMap<Integer, Meal>() {{ put(meal.getId(), meal);}})
                    .putIfAbsent(meal.getId(), meal);
            return meal;
        }
        return repository.get(userId).computeIfPresent(meal.getId(), (id, oldUser) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return repository.get(userId).remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        return repository.get(userId).get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        Map<Integer, Meal> meals = repository.get(userId);
        if (meals == null) {
            return filterByPredicate(Collections.emptyList(), meal -> true);
        }
        return filterByPredicate(meals.values(), meal -> true);
    }

    public List<Meal> getBetweenInclusive(int userId, LocalDate startDate, LocalDate endDate) {
        Map<Integer, Meal> meals = repository.get(userId);
        if (meals == null) {
            return filterByPredicate(Collections.emptyList(), meal -> true);
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