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
    
    void saveWalletEntries(List<WalletEntry> entries) throws DataAccessException;
    
    List<BusinessCard> findAllBusinessCardsByUserId(long id) throws DataAccessException;
    
    WalletEntry find(WalletEntry entry) throws DataAccessException;
    
    boolean deleteWalletEntry(WalletEntry entry) throws DataAccessException;
    
    /**
     * This method will be used in order to remove a business card from all wallets, so
     * it can be deleted from business card table without conflicts
     * 
     * @param id Business Card Id
     * @return
     * @throws DataAccessException 
     */
    boolean deleteWalletEntryByBusinessCardId(long id) throws DataAccessException;
    
}
