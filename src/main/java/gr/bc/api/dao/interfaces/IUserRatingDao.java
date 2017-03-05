/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.dao.interfaces;

import gr.bc.api.model.UserRating;
import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
public interface IUserRatingDao {
    
    UserRating saveUserRating(UserRating userRating);
    
    List<UserRating> findByBusinessCardId(long id);
    
    List<UserRating> findByUserId(long id);
    
    boolean updateUserRating(UserRating userRating);
    
    boolean deleteUserRatingById(long id);
    
    boolean isUserRatingExixst(long id);
    
}
