package gr.bc.api.dao;

import gr.bc.api.model.BusinessCard;
import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
public interface BusinessCardDao {
    
    BusinessCard saveBusinessCard(BusinessCard businessCard);
    
    BusinessCard findById(long businessCardId);
    
    BusinessCard findByUserId(long userId);
    
    BusinessCard findByUserEmail(String email);
    
    List<BusinessCard> findByUserFullName(String firstName, String lastName);
    
    List<BusinessCard> findByUserFirstName(String firstName);
    
    List<BusinessCard> findByUserLastName(String lastName);
    
    List<BusinessCard> findByProfessionId(long professionId);
           
    boolean updateBusinessCard(BusinessCard businessCard);
        
    boolean deleteBusinessCardById(long id);
    
    boolean deleteBusinessCardByUserId(long id);
    
    boolean isBusinessCardExist(long id);
    
    boolean isBusinessCardExistByUserId(long id);
    
    boolean isBusinessCardExistByUserEmail(String email);
    
    boolean isBusinessCardBelongToUserById(long businessCardId, long userId);
    
}
