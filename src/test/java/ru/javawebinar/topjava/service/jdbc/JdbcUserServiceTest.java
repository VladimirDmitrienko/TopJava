package ru.javawebinar.topjava.service.jdbc;

import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;
import ru.javawebinar.topjava.util.Profiles;


@ActiveProfiles(profiles = Profiles.JDBC)
public class JdbcUserServiceTest extends AbstractUserServiceTest {
}