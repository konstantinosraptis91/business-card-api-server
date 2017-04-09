package gr.bc.api.dao;

import gr.bc.api.model.BusinessCard;
import java.util.List;
import org.springframework.dao.DataAccessException;

/**
 *
 * @author Konstantinos Raptis
 */
public interface BusinessCardDao {
    
    BusinessCard saveBusinessCard(BusinessCard businessCard) throws DataAccessException;
    
    BusinessCard findById(long businessCardId) throws DataAccessException;
    
    List<BusinessCard> findByUserId(long userId) throws DataAccessException;
    
    List<BusinessCard> findByUserEmail(String email) throws DataAccessException;
           
    boolean updateBusinessCard(long id, BusinessCard businessCard) throws DataAccessException;
        
    boolean deleteBusinessCardById(long id) throws DataAccessException;
              
}