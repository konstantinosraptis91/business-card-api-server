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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    public int addUser(User user) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName(MySQLHelper.USER_TABLE).usingGeneratedKeyColumns(MySQLHelper.USER_ID);
        Map<String, Object> params = new HashMap<>();
        params.put(MySQLHelper.USER_EMAIL, user.getEmail());
        params.put(MySQLHelper.USER_PASSWORD, user.getPassword());
        params.put(MySQLHelper.USER_FIRSTNAME, user.getFirstName());
        params.put(MySQLHelper.USER_LASTNAME, user.getLastName());
        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params));
        LOGGER.info(user + " added successfully.", Constants.LOG_DATE_FORMAT.format(new Date()));
        return key.intValue();
    }

    @Override
    public User getUserByEmail(String email) {
        return null;
    }
    
}
