package gr.bc.api.dao;

import gr.bc.api.model.Template;
import java.util.List;
import org.springframework.dao.DataAccessException;

/**
 *
 * @author konstantinos
 */
public interface TemplateDao {
      
    Template saveTemplate(Template template) throws DataAccessException;
        
    Template findByName(String name) throws DataAccessException;
        
    List<Template> findByPrimaryColor(String primaryColor) throws DataAccessException;
    
    List<Template> findBySecondaryColor(String secondaryColor) throws DataAccessException;
    
    Template findById(long id) throws DataAccessException;
    
    List<Template> findAllTemplates() throws DataAccessException;
    
    boolean deleteTemplateById(long id) throws DataAccessException;
    
    boolean updateTemplate(long id, Template template) throws DataAccessException;
    
}
