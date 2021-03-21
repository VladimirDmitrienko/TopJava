package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.AbstractBaseEntity;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        Set<ConstraintViolation<User>> violationSet = validator.validate(user);
        if (!violationSet.isEmpty()) {
            StringBuilder violationsBuilder = new StringBuilder();
            for (var v : violationSet) {
                violationsBuilder.append(String.format("Message: %s,  invalid value: %s", v.getMessage(), v.getInvalidValue()));
            }
            throw new RuntimeException(violationsBuilder.toString());
        }
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            insertRoles(user);
        } else {
            if ((namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) != 0)) {
                List<Role> newRoles = new ArrayList<>(user.getRoles());
                List<Role> currentRoles = getRoles(user);
                if (!newRoles.equals(currentRoles)) {
                    deleteRoles(user);
                    insertRoles(user);
                }
                return user;
            }
            return null;
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        User user = DataAccessUtils.singleResult(users);
        if (user != null) {
            user.setRoles(getRoles(user));
        }
        return user;
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        User user = DataAccessUtils.singleResult(users);
        user.setRoles(new HashSet<>(getRoles(user)));
        return user;    }


    @Override
    public List<User> getAll() {
        Map<Integer, User> userMap = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER)
                .stream()
                .collect(Collectors.toMap(User::getId, user -> user));

        jdbcTemplate.query("SELECT * FROM user_roles", resultSet -> {
            int id = resultSet.getInt("user_id");
            Role role = Role.valueOf(resultSet.getString("role"));
            userMap.get(id).addRole(role);
        });
        return new ArrayList<>(userMap.values());
    }

    public List<Role> getRoles(User user) {
        return jdbcTemplate.queryForList("SELECT role FROM user_roles WHERE user_id=?", Role.class, user.getId());
    }

    public void deleteRoles(User user) {
        jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
    }

    public void insertRoles(User user) {
        int id = user.getId();
        List<Role> roles = new ArrayList<>(user.getRoles());
        jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?, ?)",

                new BatchPreparedStatementSetter() {

            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, id);
                ps.setString(2, roles.get(i).name());
            }

            public int getBatchSize() {
                return roles.size();
            }
        });
    }
}