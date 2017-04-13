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
    
    boolean saveWalletEntry(WalletEntry entry) throws DataAccessException;
        
    List<BusinessCard> findAllBusinessCardsByUserId(long id) throws DataAccessException;
    
    WalletEntry find(WalletEntry entry) throws DataAccessException;
    
    boolean deleteWalletEntry(WalletEntry entry) throws DataAccessException;
    
}
