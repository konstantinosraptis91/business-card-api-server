package gr.bc.api.service;

import gr.bc.api.model.Template;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import gr.bc.api.dao.TemplateDao;
import org.springframework.dao.DataAccessException;

/**
 *
 * @author konstantinos
 */
@Service
public class TemplateService {
    
    @Autowired
    @Qualifier("MySQLTemplate")
    private TemplateDao templateDao;

    public long saveTemplate(Template template) throws DataAccessException {
        return templateDao.saveTemplate(template);
    }
    
    public Template findByName(String name) throws DataAccessException {
        return templateDao.findByName(name);
    }
      
    public List<Template> findByPrimaryColor(String primaryColor) throws DataAccessException {
        return templateDao.findByPrimaryColor(primaryColor);
    }
    
    public List<Template> findBySecondaryColor(String secondaryColor) throws DataAccessException {
        return templateDao.findBySecondaryColor(secondaryColor);
    }
    
    public Template findById(long id) throws DataAccessException {
        return templateDao.findById(id);
    }
    
    public List<Template> findAllTemplates() throws DataAccessException {
        return templateDao.findAllTemplates();
    }
    
    public boolean deleteTemplateById(long id) throws DataAccessException {
        return templateDao.deleteTemplateById(id);
    }
    
    public boolean updateTemplate(long id, Template template) throws DataAccessException {
        return templateDao.updateTemplate(id, template);
    }
    
}
