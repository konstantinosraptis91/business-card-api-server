/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.dao;

import gr.bc.api.dao.interfaces.IUserDao;
import gr.bc.api.entity.User;
import gr.bc.api.util.Constants;
import gr.bc.api.util.MySQLHelper;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 *
 * @author konstantinos
 */
@Repository
@Qualifier("MySQLUser")
public class MySQLUserDao implements IUserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(MySQLUserDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public User addUser(User user) {
        try {
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            jdbcInsert.withTableName(MySQLHelper.USER_TABLE).usingGeneratedKeyColumns(MySQLHelper.USER_ID);
            Map<String, Object> params = new HashMap<>();
            params.put(MySQLHelper.USER_EMAIL, user.getEmail());
            params.put(MySQLHelper.USER_PASSWORD, user.getPassword());
            params.put(MySQLHelper.USER_FIRSTNAME, user.getFirstName());
            params.put(MySQLHelper.USER_LASTNAME, user.getLastName());
            Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params));
            user.setId(key.intValue());
            return user;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return new User();
    }

    @Override
    public User updateUser(long id, User user) {
        try {
            String updateQuery = " UPDATE "
                    + MySQLHelper.USER_TABLE
                    + " SET "
                    + MySQLHelper.USER_EMAIL + "=?,"
                    + MySQLHelper.USER_PASSWORD + "=?,"
                    + MySQLHelper.USER_FIRSTNAME + "=?,"
                    + MySQLHelper.USER_LASTNAME + "=?"
                    + " WHERE " + MySQLHelper.USER_ID + "=?";
            jdbcTemplate.update(updateQuery,
                    new Object[]{
                        user.getEmail(),
                        user.getPassword(),
                        user.getFirstName(),
                        user.getLastName(),
                        id
                    });
            user.setId(id);
            return user;
        } catch (DataAccessException e) {
            LOGGER.error(e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return new User();
    }

    @Override
    public void deleteUser(long id) {
        String deleteQuery = "DELETE FROM " + MySQLHelper.USER_TABLE
                + " WHERE " + MySQLHelper.USER_ID + " = " + "?";
        jdbcTemplate.update(deleteQuery, new Object[]{id});
    }

    @Override
    public User getUserByEmail(String email) {
        User user = new User();
        try {
            user = (User) jdbcTemplate.queryForObject("SELECT * FROM "
                    + MySQLHelper.USER_TABLE + " WHERE " + MySQLHelper.USER_EMAIL + " = " + "'" + email + "'",
                    (rs, rowNum) -> {
                        User u = new User();
                        u.setId(rs.getLong(MySQLHelper.USER_ID));
                        u.setEmail(rs.getString(MySQLHelper.USER_EMAIL));
                        u.setPassword(rs.getString(MySQLHelper.USER_PASSWORD));
                        u.setFirstName(rs.getString(MySQLHelper.USER_FIRSTNAME));
                        u.setLastName(rs.getString(MySQLHelper.USER_LASTNAME));
                        return u;
                    });
        } catch (DataAccessException e) {
            LOGGER.error(e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return user;
    }

    @Override
    public User getUserById(long id) {
        User user = new User();
        try {
            user = (User) jdbcTemplate.queryForObject("SELECT * FROM "
                    + MySQLHelper.USER_TABLE + " WHERE " + MySQLHelper.USER_ID + " = " + "'" + id + "'",
                    (rs, rowNum) -> {
                        User u = new User();
                        u.setId(rs.getLong(MySQLHelper.USER_ID));
                        u.setEmail(rs.getString(MySQLHelper.USER_EMAIL));
                        u.setPassword(rs.getString(MySQLHelper.USER_PASSWORD));
                        u.setFirstName(rs.getString(MySQLHelper.USER_FIRSTNAME));
                        u.setLastName(rs.getString(MySQLHelper.USER_LASTNAME));
                        return u;
                    });
        } catch (DataAccessException e) {
            LOGGER.error(e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return user;
    }

    @Override
    public List<User> getUsersByName(String firstName, String lastName) {
        String selectQuery;
        if (firstName == null) {
            selectQuery = "SELECT * FROM "
                    + MySQLHelper.USER_TABLE + " WHERE " + MySQLHelper.USER_LASTNAME + " = " + "'" + lastName + "'";
        } else if (lastName == null) {
            selectQuery = "SELECT * FROM "
                    + MySQLHelper.USER_TABLE + " WHERE " + MySQLHelper.USER_FIRSTNAME + " = " + "'" + firstName + "'";
        } else {
            selectQuery = "SELECT * FROM "
                    + MySQLHelper.USER_TABLE + " WHERE " + MySQLHelper.USER_FIRSTNAME + " = " + "'" + firstName + "'"
                    + " AND " + MySQLHelper.USER_LASTNAME + " = " + "'" + lastName + "'";
        }
        List<User> users = new ArrayList<>();
        try {
            users = jdbcTemplate.query(selectQuery, (rs, rowNum) -> {
                User user = new User();
                user.setId(rs.getLong(MySQLHelper.USER_ID));
                user.setEmail(rs.getString(MySQLHelper.USER_EMAIL));
                user.setPassword(rs.getString(MySQLHelper.USER_PASSWORD));
                user.setFirstName(rs.getString(MySQLHelper.USER_FIRSTNAME));
                user.setLastName(rs.getString(MySQLHelper.USER_LASTNAME));
                return user;
            });
        } catch (DataAccessException e) {
            LOGGER.error(e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return users;
    }

}
