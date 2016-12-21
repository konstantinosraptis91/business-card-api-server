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
      
    Template saveTemplate(Template template);
        
    Template findByName(String name);
    
    Template findByColor(String color);
    
    Template findById(long id);
    
    List<Template> findAllTemplates();
    
    boolean deleteTemplateById(long id);
    
    boolean updateTemplate(Template template);
    
    boolean isTemplateExist(long id);
    
}
