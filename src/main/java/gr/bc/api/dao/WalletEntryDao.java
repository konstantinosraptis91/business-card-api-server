package gr.bc.api.dao;

import gr.bc.api.model.BusinessCard;
import gr.bc.api.model.WalletEntry;
import java.util.List;
import org.springframework.dao.DataAccessException;

/**
 *
 * @author Konstantinos Raptis
 */
public interface WalletEntryDao {
    
    long saveWalletEntry(WalletEntry entry) throws DataAccessException;
        
    List<BusinessCard> findAllBusinessCardsByUserId(long id) throws DataAccessException;
    
    WalletEntry findById(long id) throws DataAccessException;
    
    boolean deleteWalletEntryById(long id) throws DataAccessException;
    
}
