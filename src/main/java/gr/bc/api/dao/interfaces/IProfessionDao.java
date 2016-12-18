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
    
    // -------------------------------------------------------------------------
    // CREATE
    // -------------------------------------------------------------------------
    
    Profession createProfession(Profession profession);    
    
    // -------------------------------------------------------------------------
    // GET
    // -------------------------------------------------------------------------
    
    Profession getProfessionByName(String name);
    
    Profession getProfessionById(long professionId);
    
    List<Profession> getAllProfessions();
    
}
