package gr.bc.api.service;

import gr.bc.api.model.UserRating;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import gr.bc.api.dao.UserRatingDao;

/**
 *
 * @author Konstantinos Raptis
 */
@Service
public class UserRatingService {
   
    @Autowired
    @Qualifier("MySQLUserRating")
    private UserRatingDao userRatingDao;
    
    public UserRating saveUserRating(UserRating userRating) {
        userRating.init();
        return userRatingDao.saveUserRating(userRating);
    }
    
    public List<UserRating> findByBusinessCardId(long id) {
        return userRatingDao.findByBusinessCardId(id);
    }
    
    public List<UserRating> findByUserId(long id) {
        return userRatingDao.findByUserId(id);
    }
    
    public boolean updateUserRating(UserRating userRating) {
        return userRatingDao.updateUserRating(userRating);
    }
    
    public boolean deleteUserRatingById(long id) {
        return userRatingDao.deleteUserRatingById(id);
    }
    
    public boolean isUserRatingExixst(long id) {
        return userRatingDao.isUserRatingExixst(id);
    }
    
}
