/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.dao;

import gr.bc.api.dao.interfaces.IBusinessCardDao;
import gr.bc.api.entity.BusinessCard;
import gr.bc.api.entity.User;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Konstantinos Raptis
 */
@Repository
@Qualifier("MySQLBusinessCard")
public class MySQLBusinessCardDao implements IBusinessCardDao {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(MySQLUserDao.class);
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    // -------------------------------------------------------------------------
    // GET BUSINESS CARD/S
    // -------------------------------------------------------------------------
    
    @Override
    public BusinessCard getBusinessCardByUserId(long userId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
