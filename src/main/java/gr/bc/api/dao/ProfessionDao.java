package gr.bc.api.dao;

import gr.bc.api.model.Profession;
import java.util.List;
import org.springframework.dao.DataAccessException;

/**
 *
 * @author konstantinos
 */
public interface ProfessionDao {
        
    Profession saveProfession(Profession profession) throws DataAccessException;    
        
    Profession findByName(String name) throws DataAccessException;
    
    Profession findById(long id) throws DataAccessException;
    
    List<Profession> findAllProfessions() throws DataAccessException;
    
    boolean deleteProfessionById(long id) throws DataAccessException;
    
    boolean updateProfession(long id, Profession profession) throws DataAccessException;
    
}
