/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.dao;

import gr.bc.api.dao.interfaces.IUserRatingDao;
import gr.bc.api.entity.BusinessCard;
import gr.bc.api.entity.User;
import gr.bc.api.entity.UserRating;
import gr.bc.api.util.Constants;
import gr.bc.api.util.MySQLHelper;
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
@Qualifier("MySQLUserRating")
public class MySQLUserRatingDao implements IUserRatingDao {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MySQLUserRatingDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Override
    public UserRating saveUserRating(UserRating userRating) {
        try {
            SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            jdbcInsert.withTableName(MySQLHelper.USER_RATING_TABLE).usingGeneratedKeyColumns(MySQLHelper.USER_RATING_ID);
            Map<String, Object> params = new HashMap<>();
            params.put(MySQLHelper.USER_ID, userRating.getUserId());
            params.put(MySQLHelper.BUSINESS_CARD_ID, userRating.getBusinessCardId());
            params.put(MySQLHelper.USER_RATING_STARS, userRating.getStars());
            params.put(MySQLHelper.USER_RATING_TITLE, userRating.getTitle());
            params.put(MySQLHelper.USER_RATING_DESCRIPTION, userRating.getDescription());
            Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(params));
            userRating.setId(key.intValue());
            return userRating;
        } catch (Exception e) {
            LOGGER.error("saveUserRating: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return new UserRating();
    }

    @Override
    public List<UserRating> findByBusinessCardId(long id) {
        List<UserRating> urs = new ArrayList<>();
        try {
            urs = jdbcTemplate.query("SELECT * FROM "
                    + MySQLHelper.USER_RATING_TABLE + " WHERE " + MySQLHelper.BUSINESS_CARD_ID + " = " + "'" + id + "'",
                    (rs, rowNum) -> {
                        UserRating ur = new UserRating();
                        ur.setId(rs.getLong(MySQLHelper.USER_RATING_ID));
                        ur.setUserId(rs.getLong(MySQLHelper.USER_ID));
                        ur.setBusinessCardId(rs.getLong(MySQLHelper.BUSINESS_CARD_ID));
                        
                        return ur;
                    });
        } catch (DataAccessException e) {
            LOGGER.error("findByBusinessCardId: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return urs;
    }

    @Override
    public List<UserRating> findByUserId(long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updateUserRating(UserRating userRating) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deleteUserRatingByUserId(long id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
