/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.service;

import gr.bc.api.dao.interfaces.IWalletDao;
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
    
    public long addBusinessCardToWallet(long userId, long businessCardId) {
        return walletDao.saveBusinessCardToWallet(userId, businessCardId);
    }
    
}
