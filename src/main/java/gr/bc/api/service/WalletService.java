/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.service;

import gr.bc.api.dao.interfaces.IWalletDao;
import gr.bc.api.model.BusinessCard;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author Konstantinos Raptis
 */
@Service
public class WalletService {
    
    @Autowired
    @Qualifier("MySQLWallet")
    private IWalletDao walletDao;
    
    public boolean addBusinessCardToWallet(long userId, long businessCardId) {
        return walletDao.saveBusinessCardToWallet(userId, businessCardId);
    }
    
    public List<BusinessCard> findAllBusinessCardInWalletByUserId(long id) {
        return walletDao.findAllBusinessCardInWalletByUserId(id);
    }
    
    public boolean deleteBusinessCardFromWallet(long userId, long businessCardId) {
        return walletDao.deleteBusinessCardFromWallet(userId, businessCardId);
    }
    
    public boolean isBusinessCardExistInWallet(long userId, long BusinessCardId) {
        return walletDao.isBusinessCardExistInWallet(userId, BusinessCardId);
    }
    
}
