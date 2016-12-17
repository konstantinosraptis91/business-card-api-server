/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.dao;

import gr.bc.api.dao.interfaces.IBusinessCardDao;
import gr.bc.api.entity.BusinessCard;
import gr.bc.api.entity.User;
import gr.bc.api.util.Constants;
import gr.bc.api.util.MySQLHelper;
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
@Qualifier("MySQLBusinessCard")
public class MySQLBusinessCardDao implements IBusinessCardDao {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MySQLBusinessCardDao.class);
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    // -------------------------------------------------------------------------
    // GET BUSINESS CARD/S
    // -------------------------------------------------------------------------
    
    @Override
    public BusinessCard getBusinessCardByUserId(long userId) {
        BusinessCard businessCard = new BusinessCard();
        try {
            businessCard = (BusinessCard) jdbcTemplate.queryForObject("SELECT * FROM "
                    + MySQLHelper.BUSINESS_CARD_TABLE + " WHERE " + MySQLHelper.USER_ID + " = " + "'" + userId + "'",
                    (rs, rowNum) -> {
                        BusinessCard bc = new BusinessCard();
                        bc.setId(rs.getLong(MySQLHelper.BUSINESS_CARD_ID));
                        bc.setProfessionId(rs.getLong(MySQLHelper.PROFESSION_ID));
                        bc.setTemplateId(rs.getLong(MySQLHelper.TEMPLATE_ID));
                        bc.setUserId(rs.getLong(MySQLHelper.USER_ID));
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
            LOGGER.error(e.getMessage(), Constants.LOG_DATE_FORMAT.format(new Date()));
        }
        return businessCard;
    }

    @Override
    public BusinessCard getBusinessCardById(long businessCardId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BusinessCard getBusinessCardByUserEmail(String email) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BusinessCard> getBusinessCardByUserName(String firstName, String lastName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<BusinessCard> getWalletByUserId(long userId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // -------------------------------------------------------------------------
    // CREATE BUSINESS CARD
    // -------------------------------------------------------------------------
    
    @Override
    public BusinessCard createBusinessCard(BusinessCard businessCard) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // -------------------------------------------------------------------------
    // ADD BUSINESS CARD
    // -------------------------------------------------------------------------
    
    @Override
    public BusinessCard addBusinessCardToWallet(BusinessCard businessCard, User user) {
        return null;
    }
    
    // -------------------------------------------------------------------------
    // UPDATE BUSINESS CARD
    // -------------------------------------------------------------------------
    
    @Override
    public BusinessCard updateBusinessCard(long businessCardId, BusinessCard businessCard) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // -------------------------------------------------------------------------
    // DELETE BUSINESS CARD
    // -------------------------------------------------------------------------
    
    @Override
    public void deleteBusinessCard(long businessCardId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
        
}
