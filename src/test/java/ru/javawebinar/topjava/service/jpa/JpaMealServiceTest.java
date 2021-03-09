package ru.javawebinar.topjava.service.jpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;
import ru.javawebinar.topjava.util.Profiles;


@ActiveProfiles(profiles = Profiles.JPA)
public class JpaMealServiceTest extends AbstractMealServiceTest {
}