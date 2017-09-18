package gr.bc.api.service;

import gr.bc.api.model.BusinessCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import gr.bc.api.dao.BusinessCardDao;
import gr.bc.api.model.response.BusinessCardResponse;
import java.util.List;
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

    public long saveBusinessCard(BusinessCard businessCard) throws DataAccessException {
        return businessCardDao.saveBusinessCard(businessCard);
    }

    public boolean updateBusinessCard(long id, BusinessCard businessCard) throws DataAccessException {
        return businessCardDao.updateBusinessCard(id, businessCard);
    }

    public boolean deleteBusinessCardById(long id) throws DataAccessException {
        return businessCardDao.deleteBusinessCardById(id);
    }

    // v1
    
    public List<BusinessCard> findByUserId(long id) throws DataAccessException {
        return businessCardDao.findByUserId(id);
    }

    public BusinessCard findById(long id) throws DataAccessException {
        return businessCardDao.findById(id);
    }

    public List<BusinessCard> findById(List<Long> idList) throws DataAccessException {
        return businessCardDao.findById(idList);
    }

    public List<BusinessCard> findByUserEmail(String email) throws DataAccessException {
        return businessCardDao.findByUserEmail(email);
    }

    public List<BusinessCard> findByUserName(String firstName, String lastName) throws DataAccessException {
        return businessCardDao.findByUserName(firstName, lastName);
    }

    // v2
    
    public List<BusinessCardResponse> findByUserNameV2(String firstName, String lastName) throws DataAccessException {
        return businessCardDao.findByUserNameV2(firstName, lastName);
    }

    public List<BusinessCardResponse> findByUserIdV2(long id) throws DataAccessException {
        return businessCardDao.findByUserIdV2(id);
    }

    public BusinessCardResponse findByIdV2(long id) throws DataAccessException {
        return businessCardDao.findByIdV2(id);
    }

    public List<BusinessCardResponse> findByIdV2(List<Long> idList) throws DataAccessException {
        return businessCardDao.findByIdV2(idList);
    }

    public List<BusinessCardResponse> findByUserEmailV2(String email) throws DataAccessException {
        return businessCardDao.findByUserEmailV2(email);
    }
    
}
