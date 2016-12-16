/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.dao.interfaces;

import gr.bc.api.entity.BusinessCard;
import gr.bc.api.entity.User;
import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
public interface IBusinessCardDao {
    
    // -------------------------------------------------------------------------
    // GET
    // -------------------------------------------------------------------------
    
    BusinessCard getBusinessCardByUserId(long userId);
    
    BusinessCard getBusinessCardById(long businessCardId);
    
    BusinessCard getBusinessCardByUserEmail(String email);
    
    List<BusinessCard> getBusinessCardByUserName(String firstName, String lastName);
    
    List<BusinessCard> getWalletByUserId(long userId);
    
    // -------------------------------------------------------------------------
    // CREATE
    // -------------------------------------------------------------------------
    
    BusinessCard createBusinessCard(BusinessCard businessCard);
    
    // -------------------------------------------------------------------------
    // ADD
    // -------------------------------------------------------------------------
    
    BusinessCard addBusinessCardToWallet(BusinessCard businessCard, User user);
    
    // -------------------------------------------------------------------------
    // UPDATE
    // -------------------------------------------------------------------------
    
    BusinessCard updateBusinessCard(long businessCardId, BusinessCard businessCard);
    
    // -------------------------------------------------------------------------
    // DELETE
    // -------------------------------------------------------------------------
    
    void deleteBusinessCard(long businessCardId);
    
}
