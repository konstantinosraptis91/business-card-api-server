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
    
    BusinessCard saveBusinessCard(BusinessCard businessCard);
    
    BusinessCard findById(long businessCardId);
    
    BusinessCard findByUserId(long userId);
    
    BusinessCard findByUserEmail(String email);
    
    List<BusinessCard> findCardByUserName(String firstName, String lastName);
    
    List<BusinessCard> findCardByProfessionId(long professionId);
           
    boolean updateBusinessCard(BusinessCard businessCard);
        
    boolean deleteBusinessCardById(long id);
    
    boolean isBusinessCardExist(long id);
    
}
