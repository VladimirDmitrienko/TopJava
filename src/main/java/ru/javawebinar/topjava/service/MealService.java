package ru.javawebinar.topjava.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Profile;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.atStartOfDayOrMin;
import static ru.javawebinar.topjava.util.DateTimeUtil.atStartOfNextDayOrMax;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
@Profile("dev")
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    @CacheEvict(value = "meals", allEntries = true)
    public Meal create(Meal meal, int userId) {
        Assert.notNull(meal, "meal must not be null");
        return repository.save(meal, userId);
    }

    public Meal get(int id, int userId) {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    @Cacheable("meals")
    public List<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    public List<Meal> getBetweenInclusive(@Nullable LocalDate startDate, @Nullable LocalDate endDate, int userId) {
        return repository.getBetweenHalfOpen(atStartOfDayOrMin(startDate), atStartOfNextDayOrMax(endDate), userId);
    }

    @CacheEvict(value = "meals", allEntries = true)
    public void update(Meal meal, int userId) {
        Assert.notNull(meal, "meal must not be null");
        checkNotFoundWithId(repository.save(meal, userId), meal.id());
    }

    @CacheEvict(value = "meals", allEntries = true)
    public void delete(int id, int userId) {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }
}