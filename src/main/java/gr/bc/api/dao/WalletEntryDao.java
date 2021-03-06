package gr.bc.api.dao;

import gr.bc.api.model.WalletEntry;
import gr.bc.api.model.response.BusinessCardResponse;
import java.util.List;
import org.springframework.dao.DataAccessException;

/**
 *
 * @author Konstantinos Raptis
 */
public interface WalletEntryDao {
    
    boolean saveWalletEntry(WalletEntry entry) throws DataAccessException;
    
    void saveWalletEntries(List<WalletEntry> entries) throws DataAccessException;
    
    List<BusinessCardResponse> findAllBusinessCardsByUserId(long id) throws DataAccessException;
    
    WalletEntry find(WalletEntry entry) throws DataAccessException;
    
    boolean deleteWalletEntry(WalletEntry entry) throws DataAccessException;
   
    boolean deleteWalletEntryByBusinessCardId(long id) throws DataAccessException;
    
}
