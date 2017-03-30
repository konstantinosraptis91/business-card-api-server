package gr.bc.api.dao;

import gr.bc.api.model.Credentials;
import gr.bc.api.model.User;
import gr.bc.api.util.Constants;
import gr.bc.api.util.ExtractionBundle;
import gr.bc.api.util.MySQLHelper;
import gr.bc.api.util.TokenUtils;
import java.sql.ResultSet;
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
 * @author Konstantinos Raptis
 */
@Repository
@Qualifier("MySQLUser")
public class UserDaoMySQLImpl implements UserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDaoMySQLImpl.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public User saveUser(User user) throws DataAccessException {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName(MySQLHelper.USER_TABLE).usingGeneratedKeyColumns(MySQLHelper.USER_ID);
        String token = new TokenUtils().createToken(TokenUtils.TOKEN_SIZE);
        Map<String, Object> params = new HashMap<>();
        params.put(MySQLHelper.USER_EMAIL, user.getEmail());
        params.put(MySQLHelper.USER_PASSWORD, user.getPassword());
        params.put(MySQLHelper.USER_FIRSTNAME, user.getFirstName());
        params.put(MySQLHelper.USER_LASTNAME, user.getLastName());
        params.put(MySQLHelper.USER_TOKEN, token);
        params.put(MySQLHelper.USER_IMAGE_PATH, user.getFileName());
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params));
        user.setId(key.intValue());
        user.setToken(token);
        return user;
    }

    @Override
    public boolean updateUser(long id, User user) throws DataAccessException {
        ExtractionBundle bundle = extractNotNull(id, user);
        
        String updateQuery = " UPDATE "
                + MySQLHelper.USER_TABLE
                + " SET "
                + bundle.getAttributes()
                + " WHERE " + MySQLHelper.USER_ID + "=?";
        int rows = jdbcTemplate.update(updateQuery, bundle.getValues().toArray());

        return rows > 0;
    }

    @Override
    public boolean deleteUserById(long id) throws DataAccessException {
        // LOGGER.error("deleteUserById: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        String deleteQuery = "DELETE FROM " + MySQLHelper.USER_TABLE
                + " WHERE " + MySQLHelper.USER_ID + " = " + "?";
        int rows = jdbcTemplate.update(deleteQuery, new Object[]{id});
        return rows > 0;
    }

    @Override
    public User findByEmail(String email) throws DataAccessException {
        User user = (User) jdbcTemplate.queryForObject("SELECT * FROM "
                + MySQLHelper.USER_TABLE + " WHERE " + MySQLHelper.USER_EMAIL + " = " + "'" + email + "'",
                (rs, rowNum) -> {
                    return extractUser(rs);
                });
        return user;
    }

    @Override
    public User findById(long id) throws DataAccessException {
        User user = (User) jdbcTemplate.queryForObject("SELECT * FROM "
                + MySQLHelper.USER_TABLE + " WHERE " + MySQLHelper.USER_ID + " = " + "'" + id + "'",
                (rs, rowNum) -> {
                    return extractUser(rs);
                });
        return user;
    }

    @Override
    public String authenticate(Credentials credentials) throws DataAccessException {
        // LOGGER.error("authenticate: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        String token = new TokenUtils().createToken(TokenUtils.TOKEN_SIZE);

        String updateQuery = " UPDATE "
                + MySQLHelper.USER_TABLE
                + " SET "
                + MySQLHelper.USER_TOKEN + "=?,"
                + MySQLHelper.USER_LAST_UPDATED + "=?"
                + " WHERE "
                + MySQLHelper.USER_EMAIL + "=?"
                + " AND "
                + MySQLHelper.USER_PASSWORD + "=?";
        int rows = jdbcTemplate.update(updateQuery,
                new Object[]{
                    token,
                    new Date(),
                    credentials.getUsername(),
                    credentials.getPassword()
                });
        return rows > 0 ? token : null;
    }

    public static User extractUser(ResultSet rs) {
        User u = new User();
        try {
            u.setId(rs.getLong(MySQLHelper.USER_ID));
            u.setEmail(rs.getString(MySQLHelper.USER_EMAIL));
            u.setPassword(rs.getString(MySQLHelper.USER_PASSWORD));
            u.setFirstName(rs.getString(MySQLHelper.USER_FIRSTNAME));
            u.setLastName(rs.getString(MySQLHelper.USER_LASTNAME));
            u.setToken(rs.getString(MySQLHelper.USER_TOKEN));
            u.setCreatedAt(rs.getTimestamp(MySQLHelper.USER_CREATED_AT));
            u.setLastUpdated(rs.getTimestamp(MySQLHelper.USER_LAST_UPDATED));
        } catch (SQLException ex) {
            LOGGER.error("extractUser: " + ex.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return u;
    }
    
    public static ExtractionBundle extractNotNull(long id, User user) {
        StringBuilder builder = new StringBuilder();
        List<Object> notNullList = new ArrayList<>();
        
        if (user.getEmail() != null) {
            builder.append(MySQLHelper.USER_EMAIL + "=?,");
            notNullList.add(user.getEmail());
        }
        
        if (user.getPassword() != null) {
            builder.append(MySQLHelper.USER_PASSWORD + "=?,");
            notNullList.add(user.getPassword());
        }
        
        if (user.getFirstName() != null) {
            builder.append(MySQLHelper.USER_FIRSTNAME + "=?,");
            notNullList.add(user.getFirstName());
        }
        
        if (user.getLastName() != null) {
            builder.append(MySQLHelper.USER_LASTNAME + "=?,");
            notNullList.add(user.getLastName());
        }
        
        if (user.getToken() != null) {
            builder.append(MySQLHelper.USER_TOKEN + "=?,");
            notNullList.add(user.getToken());
        }
        
        // remove last comma
        String result = builder.toString().substring(0, builder.toString().length() - 1);
        // add id 
        notNullList.add(id);
        
        return new ExtractionBundle(result, notNullList);
    }
    
}
