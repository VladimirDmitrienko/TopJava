package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles(profiles = {"hsqldb", "datajpa", "dev"})
public class MealServiceJpaTest extends AbstractMealServiceTest {
}