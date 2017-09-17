package gr.bc.api.service;

import gr.bc.api.model.BusinessCard;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataAccessException;
import gr.bc.api.dao.WalletEntryDao;
import gr.bc.api.model.WalletEntry;

/**
 *
 * @author Konstantinos Raptis
 */
@Service
public class WalletEntryService {
    
    @Autowired
    @Qualifier("MySQLWallet")
    private WalletEntryDao walletDao;
    
    public boolean saveWalletEntry(WalletEntry entry) throws DataAccessException {
        return walletDao.saveWalletEntry(entry);
    }
    
    public void saveWalletEntries(List<WalletEntry> entries) throws DataAccessException {
        walletDao.saveWalletEntries(entries);
    }
    
    public List<BusinessCard> findAllBusinessCardsByUserId(long id) throws DataAccessException {
        return walletDao.findAllBusinessCardsByUserId(id);
    }
    
    public WalletEntry find(WalletEntry entry) throws DataAccessException {
        return walletDao.find(entry);
    }
    
    public boolean deleteWalletEntry(WalletEntry entry) throws DataAccessException {
        return walletDao.deleteWalletEntry(entry);
    }
    
    public boolean deleteWalletEntryByBusinessCardId(long id) throws DataAccessException {
        return walletDao.deleteWalletEntryByBusinessCardId(id);
    }
    
}
