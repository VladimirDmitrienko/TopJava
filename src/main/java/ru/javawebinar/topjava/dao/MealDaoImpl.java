package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MealDaoImpl implements MealDao {

    private static final AtomicInteger mealCount = new AtomicInteger();
    private static final Map<Integer, Meal> mealMap = new ConcurrentHashMap<>();

    static {
        int id = mealCount.getAndIncrement();
        mealMap.put(id,  new Meal(id, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        id = mealCount.getAndIncrement();
        mealMap.put(id,  new Meal(id, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        id = mealCount.getAndIncrement();
        mealMap.put(id,  new Meal(id, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        id = mealCount.getAndIncrement();
        mealMap.put(id,  new Meal(id, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        id = mealCount.getAndIncrement();
        mealMap.put(id,  new Meal(id, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        id = mealCount.getAndIncrement();
        mealMap.put(id,  new Meal(id, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        id = mealCount.getAndIncrement();
        mealMap.put(id,  new Meal(id, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    public void create(LocalDateTime dateTime, String description, int calories) {
        int id = mealCount.getAndIncrement();
        mealMap.put(id, new Meal(id, dateTime, description, calories));
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(mealMap.values());
    }

    @Override
    public Meal get(int id) {
        return mealMap.get(id);
    }

    @Override
    public void update(int id, LocalDateTime dateTime, String description, int calories) {
        mealMap.put(id, new Meal(id, dateTime, description, calories));
    }

    @Override
    public void delete(int id) {
        mealMap.remove(id);
    }
}