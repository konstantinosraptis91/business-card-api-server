package gr.bc.api.dao;

import gr.bc.api.model.Profession;
import java.util.List;

/**
 *
 * @author konstantinos
 */
public interface ProfessionDao {
        
    Profession saveProfession(Profession profession);    
        
    Profession findByName(String name);
    
    Profession findById(long id);
    
    List<Profession> findAllProfessions();
    
    boolean deleteProfessionById(long id);
    
    boolean updateProfession(Profession profession);
    
    boolean isProfessionExist(long id);
    
    boolean isProfessionExist(String name);
    
}
