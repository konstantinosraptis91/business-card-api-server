/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.service;

import gr.bc.api.dao.interfaces.IProfessionDao;
import gr.bc.api.entity.Profession;
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
    
    public Profession createProfession(Profession profession) {
        return professionDao.createProfession(profession);
    }
    
    public Profession getProfessionById(long id) {
        return professionDao.getProfessionById(id);
    }
    
    public Profession getProfessionByName(String name) {
        return professionDao.getProfessionByName(name);
    }
    
    public boolean isProfessionExist(long id) {
        return professionDao.isProfessionExist(id);
    }
    
}
