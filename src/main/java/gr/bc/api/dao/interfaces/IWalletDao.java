/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.dao.interfaces;

import gr.bc.api.entity.BusinessCard;
import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
public interface IWalletDao {
    
    boolean saveBusinessCardToWallet(long userId, long BusinessCardId);
    
    List<BusinessCard> findAllBusinessCardInWalletByUserId(long id);
    
}
