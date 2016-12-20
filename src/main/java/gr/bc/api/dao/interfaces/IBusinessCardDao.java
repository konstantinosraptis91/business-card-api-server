/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.dao.interfaces;

import gr.bc.api.entity.BusinessCard;
import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
public interface IBusinessCardDao {
                
    BusinessCard getBusinessCardById(long businessCardId);
    
    BusinessCard getBusinessCardByUserId(long userId);
    
    BusinessCard getBusinessCardByUserEmail(String email);
    
    List<BusinessCard> getBusinessCardByUserName(String firstName, String lastName);
        
    BusinessCard createBusinessCard(BusinessCard businessCard);
        
    BusinessCard updateBusinessCard(long id, BusinessCard businessCard);
        
    void deleteBusinessCard(long id);
    
    boolean isBusinessCardExist(long id);

}
