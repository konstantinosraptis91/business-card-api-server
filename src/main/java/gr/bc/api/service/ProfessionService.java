package gr.bc.api.service;

import gr.bc.api.model.Profession;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import gr.bc.api.dao.ProfessionDao;
import org.springframework.dao.DataAccessException;

/**
 *
 * @author konstantinos
 */
@Service
public class ProfessionService {
    
    @Autowired
    @Qualifier("MySQLProfession")
    private ProfessionDao professionDao;
    
    public long saveProfession(Profession profession) throws DataAccessException {
        return professionDao.saveProfession(profession);
    }
    
    public Profession findById(long id) throws DataAccessException {
        return professionDao.findById(id);
    }
    
    public Profession findByName(String name) throws DataAccessException {
        return professionDao.findByName(name);
    }
    
    public List<Profession> findByNameV2(String name) throws DataAccessException {
        return professionDao.findByNameV2(name);
    }
    
    public List<Profession> findAllProfessions() throws DataAccessException {
        return professionDao.findAllProfessions();
    }
    
    public boolean deleteProfessionById(long id) throws DataAccessException {
        return professionDao.deleteProfessionById(id);
    }
    
    public boolean updateProfession(long id, Profession profession) throws DataAccessException {
        return professionDao.updateProfession(id, profession);
    } 
    
}
