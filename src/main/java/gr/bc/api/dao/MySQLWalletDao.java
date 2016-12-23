/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.dao;

import gr.bc.api.dao.interfaces.IWalletDao;
import gr.bc.api.entity.BusinessCard;
import gr.bc.api.util.Constants;
import gr.bc.api.util.MySQLHelper;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Konstantinos Raptis
 */
@Repository
@Qualifier("MySQLWallet")
public class MySQLWalletDao implements IWalletDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(MySQLWalletDao.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean saveBusinessCardToWallet(long userId, long businessCardId) {
        long rows = 0;
        try {
            String insertQuery = " INSERT INTO "
                    + MySQLHelper.USER_BUSINESS_CARD_TABLE
                    + " ("
                    + MySQLHelper.USER_ID + ","
                    + MySQLHelper.BUSINESS_CARD_ID + ")"
                    + " VALUES " + "(?,?)";
            rows = jdbcTemplate.update(insertQuery,
                    new Object[]{
                        userId,
                        businessCardId
                    });
        } catch (DataAccessException e) {
            LOGGER.error("saveBusinessCardToWallet: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return rows != 0;
    }

    @Override
    public List<BusinessCard> findAllBusinessCardInWalletByUserId(long id) {
        String selectQuery = "SELECT "
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_ID + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.PROFESSION_ID + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.TEMPLATE_ID + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_TITLE + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_DESCRIPTION + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_PHONE_NUMBER1 + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_PHONE_NUMBER2 + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_LINKEDIN + ","
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_WEBSITE + ","       
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_IS_PUBLIC                                         
                + " FROM " + MySQLHelper.BUSINESS_CARD_TABLE
                + " INNER JOIN " + MySQLHelper.USER_BUSINESS_CARD_TABLE
                + " ON "
                + MySQLHelper.BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_ID
                + "="
                + MySQLHelper.USER_BUSINESS_CARD_TABLE + "." + MySQLHelper.BUSINESS_CARD_ID
                + " WHERE "
                + MySQLHelper.USER_BUSINESS_CARD_TABLE + "." + MySQLHelper.USER_ID + "=" + "'" + id + "'";
        List<BusinessCard> bcs = new ArrayList<>();
        try {
            bcs = jdbcTemplate.query(selectQuery, (rs, rowNum) -> {
                BusinessCard bc = new BusinessCard();
                bc.setId(rs.getLong(MySQLHelper.BUSINESS_CARD_ID));
                bc.setProfessionId(rs.getLong(MySQLHelper.PROFESSION_ID));
                bc.setTemplateId(rs.getLong(MySQLHelper.TEMPLATE_ID));
                bc.setTitle(rs.getString(MySQLHelper.BUSINESS_CARD_TITLE));
                bc.setDescription(rs.getString(MySQLHelper.BUSINESS_CARD_DESCRIPTION));
                bc.setPhoneNumber1(rs.getString(MySQLHelper.BUSINESS_CARD_PHONE_NUMBER1));
                bc.setPhoneNumber2(rs.getString(MySQLHelper.BUSINESS_CARD_PHONE_NUMBER2));
                bc.setLinkedIn(rs.getString(MySQLHelper.BUSINESS_CARD_LINKEDIN));
                bc.setWebsite(rs.getString(MySQLHelper.BUSINESS_CARD_WEBSITE));
                bc.setIsPublic(rs.getBoolean(MySQLHelper.BUSINESS_CARD_IS_PUBLIC));
                return bc;
            });
        } catch (DataAccessException e) {
            LOGGER.error("findAllBusinessCardInWalletByUserId: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return bcs;        
    }

    @Override
    public boolean deleteBusinessCardFromWallet(long userId, long businessCardId) {
        Integer rows = null;
        try {
            String deleteQuery = "DELETE FROM " + MySQLHelper.USER_BUSINESS_CARD_TABLE
                    + " WHERE " + MySQLHelper.USER_ID + " = " + "?"
                    + " AND "
                    + MySQLHelper.BUSINESS_CARD_ID + " = " + "?";
            rows = jdbcTemplate.update(deleteQuery, new Object[]{userId, businessCardId});
        } catch (DataAccessException e) {
            LOGGER.error("deleteBusinessCardFromWallet: " + e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return rows != null && rows > 0;
    }
   
    @Override
    public boolean isBusinessCardExistInWallet(long userId, long businessCardId) {
        Integer result = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM "
                + MySQLHelper.USER_BUSINESS_CARD_TABLE + " WHERE "
                + MySQLHelper.USER_ID + " = " + "?"
                + " AND "
                + MySQLHelper.BUSINESS_CARD_ID + " = " + "?", 
                Integer.class, userId, businessCardId);
        return result != null && result > 0;
    }
   
}
