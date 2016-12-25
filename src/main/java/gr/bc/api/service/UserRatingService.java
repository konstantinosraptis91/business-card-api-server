/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.service;

import gr.bc.api.dao.interfaces.IUserRatingDao;
import gr.bc.api.entity.UserRating;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author Konstantinos Raptis
 */
@Service
public class UserRatingService {
   
    @Autowired
    @Qualifier("MySQLUserRating")
    private IUserRatingDao userRatingDao;
    
    public UserRating saveUserRating(UserRating userRating) {
        return userRatingDao.saveUserRating(userRating);
    }
    
    public List<UserRating> findByBusinessCardId(long id) {
        return userRatingDao.findByBusinessCardId(id);
    }
    
    public List<UserRating> findByUserId(long id) {
        return userRatingDao.findByUserId(id);
    }
    
    boolean updateUserRating(UserRating userRating) {
        return userRatingDao.updateUserRating(userRating);
    }
    
    boolean deleteUserRatingById(long id) {
        return userRatingDao.deleteUserRatingById(id);
    }
    
}
