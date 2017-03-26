package gr.bc.api.service;

import gr.bc.api.model.BusinessCard;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import gr.bc.api.dao.BusinessCardDao;

/**
 *
 * @author Konstantinos Raptis
 */
@Service
public class BusinessCardService {
    
    @Autowired
    @Qualifier("MySQLBusinessCard")
    private BusinessCardDao businessCardDao;

    public BusinessCard saveBusinessCard(BusinessCard businessCard) {
        businessCard.init();
        return businessCardDao.saveBusinessCard(businessCard);
    }
    
    public BusinessCard findByUserId(long id) {
        return businessCardDao.findByUserId(id);
    }
    
    public BusinessCard findById(long id) {
        return businessCardDao.findById(id);
    }
    
    public List<BusinessCard> findByProfessionId(long id) {
        return businessCardDao.findByProfessionId(id);
    }
    
    public BusinessCard findByUserEmail(String email) {
        return businessCardDao.findByUserEmail(email);
    }
       
    public List<BusinessCard> findByUserName(String firstName, String lastName) {
        if (firstName == null) {
            return businessCardDao.findByUserLastName(lastName);
        } else if (lastName == null) {
            return businessCardDao.findByUserFirstName(firstName);
        } else {
            return businessCardDao.findByUserFullName(firstName, lastName);
        }
    }
            
    public boolean isBusinessCardExist(long id) {
        return businessCardDao.isBusinessCardExist(id);
    }
    
    public boolean deleteBusinessCardById(long id) {
        return businessCardDao.deleteBusinessCardById(id);
    }
    
    public boolean deleteBusinessCardByUserId(long id) {
        return businessCardDao.deleteBusinessCardByUserId(id);
    }
    
    public boolean updateBusinessCard(BusinessCard businessCard) {
        return businessCardDao.updateBusinessCard(businessCard);
    }
    
    public boolean isBusinessCardExistByUserId(long id) {
        return businessCardDao.isBusinessCardExistByUserId(id);
    }
    
    public boolean isBusinessCardExistByUserEmail(String email) {
        return businessCardDao.isBusinessCardExistByUserEmail(email);
    }
    
    public boolean isBusinessCardBelongToUserById(long businessCardId, long userId) {
        return businessCardDao.isBusinessCardBelongToUserById(businessCardId, userId);
    }
    
}
