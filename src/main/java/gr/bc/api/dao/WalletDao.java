package gr.bc.api.dao;

import gr.bc.api.model.BusinessCard;
import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
public interface WalletDao {
    
    boolean saveBusinessCardToWallet(long userId, long businessCardId);
    
    List<BusinessCard> findAllBusinessCardInWalletByUserId(long id);
    
    boolean deleteBusinessCardFromWallet(long userId, long businessCardId);
    
    boolean isBusinessCardExistInWallet(long userId, long businessCardId);
    
}
