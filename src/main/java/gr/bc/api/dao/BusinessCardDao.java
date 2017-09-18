package gr.bc.api.dao;

import gr.bc.api.model.BusinessCard;
import gr.bc.api.model.response.BusinessCardResponse;
import java.util.List;
import org.springframework.dao.DataAccessException;

/**
 *
 * @author Konstantinos Raptis
 */
public interface BusinessCardDao {
    
    long saveBusinessCard(BusinessCard businessCard) throws DataAccessException;
            
    boolean updateBusinessCard(long id, BusinessCard businessCard) throws DataAccessException;
        
    boolean deleteBusinessCardById(long id) throws DataAccessException;
    
    // v1 returns BusinessCard object 
    
    BusinessCard findById(long businessCardId) throws DataAccessException;
    
    List<BusinessCard> findById(List<Long> cardIdList) throws DataAccessException;
    
    List<BusinessCard> findByUserId(long userId) throws DataAccessException;
       
    List<BusinessCard> findByUserEmail(String email) throws DataAccessException;
         
    List<BusinessCard> findByUserName(String firstName, String lastName) throws DataAccessException;
    
    // v2 returns BusinessCardResponse object

    BusinessCardResponse findByIdV2(long businessCardId) throws DataAccessException;
    
    List<BusinessCardResponse> findByIdV2(List<Long> cardIdList) throws DataAccessException;
    
    List<BusinessCardResponse> findByUserIdV2(long userId) throws DataAccessException;
       
    List<BusinessCardResponse> findByUserEmailV2(String email) throws DataAccessException;
    
    List<BusinessCardResponse> findByUserNameV2(String firstName, String lastName) throws DataAccessException;
    
}
