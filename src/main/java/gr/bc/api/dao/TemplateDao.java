package gr.bc.api.dao;

import gr.bc.api.model.Template;
import java.util.List;

/**
 *
 * @author konstantinos
 */
public interface TemplateDao {
      
    Template saveTemplate(Template template);
        
    Template findByName(String name);
        
    List<Template> findByPrimaryColor(String primaryColor);
    
    List<Template> findBySecondaryColor(String secondaryColor);
    
    Template findById(long id);
    
    List<Template> findAllTemplates();
    
    boolean deleteTemplateById(long id);
    
    boolean updateTemplate(Template template);
    
    boolean isTemplateExist(long id);
    
    boolean isTemplateExist(String name);
    
}
