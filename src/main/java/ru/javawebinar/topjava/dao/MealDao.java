package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface MealDao {

    void create(LocalDateTime dateTime, String description, int calories);

    List<Meal> getAll();

    Meal get(int id);

    void update(int id, LocalDateTime dateTime, String description, int calories);

    void delete(int id);
}