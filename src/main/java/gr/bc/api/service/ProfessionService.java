/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.service;

import gr.bc.api.dao.interfaces.IProfessionDao;
import gr.bc.api.entity.Profession;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author konstantinos
 */
@Service
public class ProfessionService {
    
    @Autowired
    @Qualifier("MySQLProfession")
    private IProfessionDao professionDao;
    
    public Profession saveProfession(Profession profession) {
        return professionDao.saveProfession(profession);
    }
    
    public Profession findById(long id) {
        return professionDao.findById(id);
    }
    
    public Profession findByName(String name) {
        return professionDao.findByName(name);
    }
    
    public List<Profession> findAllProfessions() {
        return professionDao.findAllProfessions();
    }
    
    public boolean deleteProfessionById(long id) {
        return professionDao.deleteProfessionById(id);
    }
    
    public boolean updateProfession(Profession profession) {
        return professionDao.updateProfession(profession);
    } 
    
    public boolean isProfessionExist(long id) {
        return professionDao.isProfessionExist(id);
    }
    
    public boolean isProfessionExist(String name) {
        return professionDao.isProfessionExist(name);
    }
    
}
