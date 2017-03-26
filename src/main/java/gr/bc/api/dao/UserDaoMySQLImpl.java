package gr.bc.api.dao;

import gr.bc.api.model.Credentials;
import gr.bc.api.model.User;
import gr.bc.api.util.Constants;
import gr.bc.api.util.MySQLHelper;
import gr.bc.api.util.TokenUtils;
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
 * @author Konstantinos Raptis
 */
@Repository
@Qualifier("MySQLUser")
public class UserDaoMySQLImpl implements UserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoMySQLImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public User saveUser(User user) {
        try {
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            jdbcInsert.withTableName(MySQLHelper.USER_TABLE).usingGeneratedKeyColumns(MySQLHelper.USER_ID);
            Map<String, Object> params = new HashMap<>();
            params.put(MySQLHelper.USER_EMAIL, user.getEmail());
            params.put(MySQLHelper.USER_PASSWORD, user.getPassword());
            params.put(MySQLHelper.USER_FIRSTNAME, user.getFirstName());
            params.put(MySQLHelper.USER_LASTNAME, user.getLastName());
            params.put(MySQLHelper.USER_LAST_UPDATED, user.getLastUpdated());
            params.put(MySQLHelper.USER_CREATED_AT, user.getCreatedAt());
            params.put(MySQLHelper.USER_TOKEN, user.getToken());
            params.put(MySQLHelper.USER_IMAGE_PATH, user.getFileName());
            Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params));
            user.setId(key.intValue());
            return user;
        } catch (Exception e) {
            LOGGER.error("saveUser: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return new User();
    }

    @Override
    public boolean updateUser(User user) {
        Integer rows = null;
        try {
            String updateQuery = " UPDATE "
                    + MySQLHelper.USER_TABLE
                    + " SET "
                    + MySQLHelper.USER_EMAIL + "=?,"
                    + MySQLHelper.USER_PASSWORD + "=?,"
                    + MySQLHelper.USER_FIRSTNAME + "=?,"
                    + MySQLHelper.USER_LASTNAME + "=?,"
                    + MySQLHelper.USER_TOKEN + "=?"
                    + " WHERE " + MySQLHelper.USER_ID + "=?";
            rows = jdbcTemplate.update(updateQuery,
                    new Object[]{
                        user.getEmail(),
                        user.getPassword(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getToken(),
                        user.getId()
                    });
        } catch (DataAccessException e) {
            LOGGER.error("updateUser: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return rows != null && rows > 0;
    }

    @Override
    public boolean deleteUserById(long id) {
        Integer rows = null;
        try {
            String deleteQuery = "DELETE FROM " + MySQLHelper.USER_TABLE
                    + " WHERE " + MySQLHelper.USER_ID + " = " + "?";
            rows = jdbcTemplate.update(deleteQuery, new Object[]{id});
        } catch (DataAccessException e) {
            LOGGER.error("deleteUserById: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return rows != null && rows > 0;
    }

    @Override
    public User findByEmail(String email) {
        User user = new User();
        try {
            user = (User) jdbcTemplate.queryForObject("SELECT * FROM "
                    + MySQLHelper.USER_TABLE + " WHERE " + MySQLHelper.USER_EMAIL + " = " + "'" + email + "'",
                    (rs, rowNum) -> {
                        User u = new User();
                        u.setId(rs.getLong(MySQLHelper.USER_ID));
                        u.setBusinessCardId(rs.getLong(MySQLHelper.BUSINESS_CARD_ID));
                        u.setEmail(rs.getString(MySQLHelper.USER_EMAIL));
                        u.setPassword(rs.getString(MySQLHelper.USER_PASSWORD));
                        u.setFirstName(rs.getString(MySQLHelper.USER_FIRSTNAME));
                        u.setLastName(rs.getString(MySQLHelper.USER_LASTNAME));
                        u.setToken(rs.getString(MySQLHelper.USER_TOKEN));
                        u.setCreatedAt(rs.getDate(MySQLHelper.USER_CREATED_AT));
                        u.setLastUpdated(rs.getDate(MySQLHelper.USER_LAST_UPDATED));
                        return u;
                    });
        } catch (DataAccessException e) {
            LOGGER.error("findByEmail: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return user;
    }

    @Override
    public User findById(long id) {
        User user = new User();
        try {
            user = (User) jdbcTemplate.queryForObject("SELECT * FROM "
                    + MySQLHelper.USER_TABLE + " WHERE " + MySQLHelper.USER_ID + " = " + "'" + id + "'",
                    (rs, rowNum) -> {
                        User u = new User();
                        u.setId(rs.getLong(MySQLHelper.USER_ID));
                        u.setBusinessCardId(rs.getLong(MySQLHelper.BUSINESS_CARD_ID));
                        u.setEmail(rs.getString(MySQLHelper.USER_EMAIL));
                        u.setPassword(rs.getString(MySQLHelper.USER_PASSWORD));
                        u.setFirstName(rs.getString(MySQLHelper.USER_FIRSTNAME));
                        u.setLastName(rs.getString(MySQLHelper.USER_LASTNAME));
                        u.setToken(rs.getString(MySQLHelper.USER_TOKEN));
                        u.setCreatedAt(rs.getDate(MySQLHelper.USER_CREATED_AT));
                        u.setLastUpdated(rs.getDate(MySQLHelper.USER_LAST_UPDATED));
                        return u;
                    });
        } catch (DataAccessException e) {
            LOGGER.error("findById: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return user;
    }
    
    @Override
    public User findByToken(String token) {
        User user = new User();
        try {
            user = (User) jdbcTemplate.queryForObject("SELECT * FROM "
                    + MySQLHelper.USER_TABLE + " WHERE " + MySQLHelper.USER_TOKEN + " = " + "'" + token + "'",
                    (rs, rowNum) -> {
                        User u = new User();
                        u.setId(rs.getLong(MySQLHelper.USER_ID));
                        u.setBusinessCardId(rs.getLong(MySQLHelper.BUSINESS_CARD_ID));
                        u.setEmail(rs.getString(MySQLHelper.USER_EMAIL));
                        u.setPassword(rs.getString(MySQLHelper.USER_PASSWORD));
                        u.setFirstName(rs.getString(MySQLHelper.USER_FIRSTNAME));
                        u.setLastName(rs.getString(MySQLHelper.USER_LASTNAME));
                        u.setToken(rs.getString(MySQLHelper.USER_TOKEN));
                        u.setCreatedAt(rs.getDate(MySQLHelper.USER_CREATED_AT));
                        u.setLastUpdated(rs.getDate(MySQLHelper.USER_LAST_UPDATED));
                        return u;
                    });
        } catch (DataAccessException e) {
            LOGGER.error("findByToken: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return user;
    }    
    
    @Override
    public List<User> findByFullName(String firstName, String lastName) {
        String selectQuery = "SELECT * FROM "
                + MySQLHelper.USER_TABLE + " WHERE " + MySQLHelper.USER_FIRSTNAME + " = " + "'" + firstName + "'"
                + " AND " + MySQLHelper.USER_LASTNAME + " = " + "'" + lastName + "'";
        List<User> users = new ArrayList<>();
        try {
            users = jdbcTemplate.query(selectQuery, (rs, rowNum) -> {
                User user = new User();
                user.setId(rs.getLong(MySQLHelper.USER_ID));
                user.setBusinessCardId(rs.getLong(MySQLHelper.BUSINESS_CARD_ID));
                user.setEmail(rs.getString(MySQLHelper.USER_EMAIL));
                user.setPassword(rs.getString(MySQLHelper.USER_PASSWORD));
                user.setFirstName(rs.getString(MySQLHelper.USER_FIRSTNAME));
                user.setLastName(rs.getString(MySQLHelper.USER_LASTNAME));
                user.setToken(rs.getString(MySQLHelper.USER_TOKEN));
                user.setCreatedAt(rs.getDate(MySQLHelper.USER_CREATED_AT));
                user.setLastUpdated(rs.getDate(MySQLHelper.USER_LAST_UPDATED));
                return user;
            });
        } catch (DataAccessException e) {
            LOGGER.error("findByFullName: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return users;
    }

    @Override
    public List<User> findByFirstName(String firstName) {
        String selectQuery = "SELECT * FROM "
                + MySQLHelper.USER_TABLE + " WHERE " + MySQLHelper.USER_FIRSTNAME + " = " + "'" + firstName + "'";
        List<User> users = new ArrayList<>();
        try {
            users = jdbcTemplate.query(selectQuery, (rs, rowNum) -> {
                User user = new User();
                user.setId(rs.getLong(MySQLHelper.USER_ID));
                user.setBusinessCardId(rs.getLong(MySQLHelper.BUSINESS_CARD_ID));
                user.setEmail(rs.getString(MySQLHelper.USER_EMAIL));
                user.setPassword(rs.getString(MySQLHelper.USER_PASSWORD));
                user.setFirstName(rs.getString(MySQLHelper.USER_FIRSTNAME));
                user.setLastName(rs.getString(MySQLHelper.USER_LASTNAME));
                user.setToken(rs.getString(MySQLHelper.USER_TOKEN));
                user.setCreatedAt(rs.getDate(MySQLHelper.USER_CREATED_AT));
                user.setLastUpdated(rs.getDate(MySQLHelper.USER_LAST_UPDATED));
                return user;
            });
        } catch (DataAccessException e) {
            LOGGER.error("findByFirstName: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return users;
    }

    @Override
    public List<User> findByLastName(String lastName) {
        String selectQuery = "SELECT * FROM "
                + MySQLHelper.USER_TABLE + " WHERE " + MySQLHelper.USER_LASTNAME + " = " + "'" + lastName + "'";
        List<User> users = new ArrayList<>();
        try {
            users = jdbcTemplate.query(selectQuery, (rs, rowNum) -> {
                User user = new User();
                user.setId(rs.getLong(MySQLHelper.USER_ID));
                user.setBusinessCardId(rs.getLong(MySQLHelper.BUSINESS_CARD_ID));
                user.setEmail(rs.getString(MySQLHelper.USER_EMAIL));
                user.setPassword(rs.getString(MySQLHelper.USER_PASSWORD));
                user.setFirstName(rs.getString(MySQLHelper.USER_FIRSTNAME));
                user.setLastName(rs.getString(MySQLHelper.USER_LASTNAME));
                user.setToken(rs.getString(MySQLHelper.USER_TOKEN));
                user.setCreatedAt(rs.getDate(MySQLHelper.USER_CREATED_AT));
                user.setLastUpdated(rs.getDate(MySQLHelper.USER_LAST_UPDATED));
                return user;
            });
        } catch (DataAccessException e) {
            LOGGER.error("findByLastName: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return users;
    }
        
    // Check if user by given id exists
    @Override
    public boolean isUserExistById(long id) {
        Integer result = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM "
                + MySQLHelper.USER_TABLE + " WHERE "
                + MySQLHelper.USER_ID + " = " + "?", Integer.class, id);
        return result != null && result > 0;
    }

    // Check if user by given email exists
    @Override
    public boolean isUserExistByEmail(String email) {
        Integer result = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM "
                + MySQLHelper.USER_TABLE + " WHERE "
                + MySQLHelper.USER_EMAIL + " = " + "?", Integer.class, email);
        return result != null && result > 0;
    }

    @Override
    public boolean isUserExistByToken(String token) {
        Integer result = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM "
                + MySQLHelper.USER_TABLE + " WHERE "
                + MySQLHelper.USER_TOKEN + " = " + "?"
                , Integer.class, token);
        return result != null && result > 0;
    }

    @Override
    public boolean isUserExistByCredentials(Credentials credentials) {
        Integer result = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM "
                + MySQLHelper.USER_TABLE + " WHERE "
                + MySQLHelper.USER_EMAIL + " = " + "?" + " AND "
                + MySQLHelper.USER_PASSWORD + " = " + "?"
                , Integer.class, credentials.getUsername(), credentials.getPassword());
        return result != null && result > 0;
    }

    @Override
    public String authenticate(Credentials credentials) {
        Integer rows = null;
        String token = new TokenUtils().createToken(TokenUtils.TOKEN_SIZE);
        try {
            String updateQuery = " UPDATE "
                    + MySQLHelper.USER_TABLE
                    + " SET "
                    + MySQLHelper.USER_TOKEN + "=?,"
                    + MySQLHelper.USER_LAST_UPDATED + "=?"
                    + " WHERE " 
                    + MySQLHelper.USER_EMAIL + "=?" 
                    + " AND "
                    + MySQLHelper.USER_PASSWORD + "=?";
            rows = jdbcTemplate.update(updateQuery,
                    new Object[]{
                        token,
                        new Date(),
                        credentials.getUsername(),
                        credentials.getPassword()
                    });
        } catch (DataAccessException e) {
            LOGGER.error("updateUser: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return rows != null && rows > 0 ? token : null;
    }

}
