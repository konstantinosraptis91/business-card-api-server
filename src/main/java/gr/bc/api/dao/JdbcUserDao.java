package gr.bc.api.dao;

import gr.bc.api.model.authentication.Credentials;
import gr.bc.api.model.User;
import gr.bc.api.util.ExtractionBundle;
import gr.bc.api.util.TokenUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Konstantinos Raptis
 */
@Repository
@Qualifier("MySQLUser")
public class JdbcUserDao extends JdbcDao implements UserDao {
        
    protected static final String TABLE_USER = "user";
    protected static final String EMAIL = "email";
    protected static final String PASSWORD = "password";
    protected static final String FIRSTNAME = "firstname";
    protected static final String LASTNAME = "lastname";
    protected static final String TOKEN = "token";
    protected static final String IMAGE_FILENAME = "image_filename";
        
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public User saveUser(User user) throws DataAccessException {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName(TABLE_USER).usingGeneratedKeyColumns(ID);
        String token = new TokenUtils().createToken(TokenUtils.TOKEN_SIZE);
        Map<String, Object> params = new HashMap<>();
        params.put(EMAIL, user.getEmail());
        params.put(PASSWORD, user.getPassword());
        params.put(FIRSTNAME, user.getFirstName());
        params.put(LASTNAME, user.getLastName());
        params.put(TOKEN, token);
        params.put(IMAGE_FILENAME, user.getFileName());
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params));
        user.setId(key.longValue());
        user.setToken(token);
        return user;
    }

    @Override
    public boolean updateUser(long id, User user) throws DataAccessException {
        ExtractionBundle bundle = extractNotNull(id, user);
        
        String updateQuery = " UPDATE " + TABLE_USER
                + " SET " + bundle.getAttributes()
                + " WHERE " + ID + "=?";
        
        int rows = jdbcTemplate.update(updateQuery, bundle.getValues().toArray());
        return rows > 0;
    }

    @Override
    public boolean deleteUserById(long id) throws DataAccessException {
        
        String deleteQuery = "DELETE FROM " + TABLE_USER
                + " WHERE " + ID + " = " + "?";
        
        int rows = jdbcTemplate.update(deleteQuery, new Object[]{id});
        return rows > 0;
    }

    @Override
    public User findByEmail(String email) throws DataAccessException {
        
        String selectQuery = "SELECT * FROM " + TABLE_USER 
                + " WHERE " + EMAIL + " = " + "'" + email + "'";
        
        User user = jdbcTemplate.queryForObject(selectQuery, new JdbcUserDao.UserMapper());
        return user;
    }

    @Override
    public User findById(long id) throws DataAccessException {
        
        String selectQuery = "SELECT * FROM " + TABLE_USER
                + " WHERE " + ID + " = " + "'" + id + "'";
        
        User user = jdbcTemplate.queryForObject(selectQuery, new JdbcUserDao.UserMapper());
        return user;
    }

    @Override
    public String authenticate(Credentials credentials) throws DataAccessException {
        String token = new TokenUtils().createToken(TokenUtils.TOKEN_SIZE);

        String updateQuery = " UPDATE " + TABLE_USER
                + " SET " + TOKEN + "=?"
                + " WHERE " + EMAIL + "=?"
                + " AND " + PASSWORD + "=?";
        
        int rows = jdbcTemplate.update(updateQuery, new Object[]{token, credentials.getUsername(), credentials.getPassword()});
        return rows > 0 ? token : null;
    }
    
    public static ExtractionBundle extractNotNull(long id, User user) {
        StringBuilder builder = new StringBuilder();
        List<Object> notNullList = new ArrayList<>();
        
        if (user.getEmail() != null) {
            builder.append(EMAIL + "=?,");
            notNullList.add(user.getEmail());
        }
        
        if (user.getPassword() != null) {
            builder.append(PASSWORD + "=?,");
            notNullList.add(user.getPassword());
        }
        
        if (user.getFirstName() != null) {
            builder.append(FIRSTNAME + "=?,");
            notNullList.add(user.getFirstName());
        }
        
        if (user.getLastName() != null) {
            builder.append(LASTNAME + "=?,");
            notNullList.add(user.getLastName());
        }
        
        if (user.getToken() != null) {
            builder.append(TOKEN + "=?,");
            notNullList.add(user.getToken());
        }
        
        // remove last comma
        String result = builder.toString().substring(0, builder.toString().length() - 1);
        // add id 
        notNullList.add(id);
        
        return new ExtractionBundle(result, notNullList);
    }
    
    public static final class UserMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User u = new User();
            u.setId(rs.getLong(ID));
            u.setEmail(rs.getString(EMAIL));
            u.setPassword(rs.getString(PASSWORD));
            u.setFirstName(rs.getString(FIRSTNAME));
            u.setLastName(rs.getString(LASTNAME));
            u.setToken(rs.getString(TOKEN));
            u.setCreatedAt(rs.getTimestamp(CREATED_AT));
            u.setLastUpdated(rs.getTimestamp(LAST_UPDATED));
            return u;
        }
        
    }
    
}
