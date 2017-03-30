package gr.bc.api.service;

import gr.bc.api.model.BusinessCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import gr.bc.api.dao.BusinessCardDao;
import org.springframework.dao.DataAccessException;

/**
 *
 * @author Konstantinos Raptis
 */
@Service
public class BusinessCardService {
    
    @Autowired
    @Qualifier("MySQLBusinessCard")
    private BusinessCardDao businessCardDao;

    public BusinessCard saveBusinessCard(BusinessCard businessCard) throws DataAccessException {
        return businessCardDao.saveBusinessCard(businessCard);
    }
    
    public boolean updateBusinessCard(long id, BusinessCard businessCard) throws DataAccessException {
        return businessCardDao.updateBusinessCard(id, businessCard);
    }
    
    public BusinessCard findByUserId(long id) throws DataAccessException {
        return businessCardDao.findByUserId(id);
    }
    
    public BusinessCard findById(long id) throws DataAccessException {
        return businessCardDao.findById(id);
    }
    
    public BusinessCard findByUserEmail(String email) throws DataAccessException {
        return businessCardDao.findByUserEmail(email);
    }
    
    public boolean deleteBusinessCardById(long id) throws DataAccessException {
        return businessCardDao.deleteBusinessCardById(id);
    }
        
}
