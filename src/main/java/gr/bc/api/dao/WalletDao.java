package gr.bc.api.dao;

import gr.bc.api.model.BusinessCard;
import java.util.List;
import org.springframework.dao.DataAccessException;

/**
 *
 * @author Konstantinos Raptis
 */
public interface WalletDao {
    
    boolean saveBusinessCardToWallet(long userId, long businessCardId) throws DataAccessException ;
        
    List<BusinessCard> findAllBusinessCardInWalletByUserId(long id) throws DataAccessException ;
    
    boolean isDuplicate(long userId, long businessCardId) throws DataAccessException;
    
    boolean deleteBusinessCardFromWallet(long userId, long businessCardId) throws DataAccessException ;
    
}
