/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.dao.interfaces;

import gr.bc.api.entity.Template;
import java.util.List;

/**
 *
 * @author konstantinos
 */
public interface ITemplateDao {
    
    // -------------------------------------------------------------------------
    // CREATE
    // -------------------------------------------------------------------------
    
    Template createTemplate(Template template);
    
    // -------------------------------------------------------------------------
    // GET
    // -------------------------------------------------------------------------
    
    List<Template> getAllTemplates();
    
    Template getTemplateByName(String name);
    
    Template getTemplateById(long id);
    
}
