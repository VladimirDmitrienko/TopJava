package ru.javawebinar.topjava.repository.jpa;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public Meal save(Meal meal, int userId) {
        User userRef = em.getReference(User.class, userId);
        if (userRef == null) {
            return null;
        }
        meal.setUser(userRef);
        if (meal.isNew()) {
            em.persist(meal);
            return meal;
        }
        else {
            Meal ref = em.find(Meal.class, meal.getId());
            if (ref.getUser().id() != userId) {
                return null;
            }
            return em.merge(meal);
        }
    }

    @Transactional
    @Override
    public boolean delete(int id, int userId) {
        return em.createNamedQuery(Meal.DELETE)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        Meal ref = em.find(Meal.class, id);
        if (ref == null || ref.getUser().id() != userId) {
            return null;
        }
        return ref;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createNamedQuery(Meal.ALL, Meal.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return em.createNamedQuery(Meal.ALL_BETWEEN_HALF_OPEN, Meal.class)
                .setParameter("userId", userId)
                .setParameter("startTime", startDateTime)
                .setParameter("endTime", endDateTime)
                .getResultList();
    }
}