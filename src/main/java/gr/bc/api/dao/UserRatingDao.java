package gr.bc.api.dao;

import gr.bc.api.model.UserRating;
import java.util.List;

/**
 *
 * @author Konstantinos Raptis
 */
public interface UserRatingDao {
    
    UserRating saveUserRating(UserRating userRating);
    
    List<UserRating> findByBusinessCardId(long id);
    
    List<UserRating> findByUserId(long id);
    
    boolean updateUserRating(UserRating userRating);
    
    boolean deleteUserRatingById(long id);
    
    boolean isUserRatingExixst(long id);
    
}
