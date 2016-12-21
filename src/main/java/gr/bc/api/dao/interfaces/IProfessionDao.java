/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.dao.interfaces;

import gr.bc.api.entity.Profession;
import java.util.List;

/**
 *
 * @author konstantinos
 */
public interface IProfessionDao {
        
    Profession saveProfession(Profession profession);    
        
    Profession findByName(String name);
    
    Profession findById(long id);
    
    List<Profession> findAllProfessions();
    
    boolean deleteProfessionById(long id);
    
    boolean updateProfession(Profession profession);
    
    boolean isProfessionExist(long id);
    
    boolean isProfessionExist(String name);
    
}
