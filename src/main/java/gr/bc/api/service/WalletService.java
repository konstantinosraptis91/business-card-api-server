package gr.bc.api.service;

import gr.bc.api.model.BusinessCard;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import gr.bc.api.dao.WalletDao;
import org.springframework.dao.DataAccessException;

/**
 *
 * @author Konstantinos Raptis
 */
@Service
public class WalletService {
    
    @Autowired
    @Qualifier("MySQLWallet")
    private WalletDao walletDao;
    
    public boolean addBusinessCardToWallet(long userId, long businessCardId) throws DataAccessException {
        return walletDao.saveBusinessCardToWallet(userId, businessCardId);
    }
    
    public List<BusinessCard> findAllBusinessCardInWalletByUserId(long id) throws DataAccessException {
        return walletDao.findAllBusinessCardInWalletByUserId(id);
    }
    
    public boolean deleteBusinessCardFromWallet(long userId, long businessCardId) throws DataAccessException {
        return walletDao.deleteBusinessCardFromWallet(userId, businessCardId);
    }
      
    public boolean isDuplicate(long userId, long businessCardId) throws DataAccessException {
        return walletDao.isDuplicate(userId, businessCardId);
    }
    
}
