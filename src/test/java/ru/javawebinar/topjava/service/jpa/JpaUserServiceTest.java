package ru.javawebinar.topjava.service.jpa;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;
import ru.javawebinar.topjava.util.Profiles;


@ActiveProfiles(profiles = Profiles.JPA)
public class JpaUserServiceTest extends AbstractUserServiceTest {
}