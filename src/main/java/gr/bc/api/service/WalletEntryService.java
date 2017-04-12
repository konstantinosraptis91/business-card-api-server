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
    
    public long saveWalletEntry(WalletEntry entry) throws DataAccessException {
        return walletDao.saveWalletEntry(entry);
    }
    
    public List<BusinessCard> findAllBusinessCardsByUserId(long id) throws DataAccessException {
        return walletDao.findAllBusinessCardsByUserId(id);
    }
    
    public WalletEntry findById(long id) throws DataAccessException {
        return walletDao.findById(id);
    }
    
    public boolean deleteWalletEntryById(long id) throws DataAccessException {
        return walletDao.deleteWalletEntryById(id);
    }
       
}
