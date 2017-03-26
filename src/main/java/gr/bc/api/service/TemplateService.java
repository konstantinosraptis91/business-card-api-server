package gr.bc.api.service;

import gr.bc.api.model.Template;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import gr.bc.api.dao.TemplateDao;

/**
 *
 * @author konstantinos
 */
@Service
public class TemplateService {
    
    @Autowired
    @Qualifier("MySQLTemplate")
    private TemplateDao templateDao;

    public Template saveTemplate(Template template) {
        template.init();
        return templateDao.saveTemplate(template);
    }
    
    public Template findByName(String name) {
        return templateDao.findByName(name);
    }
    
    public List<Template> findByColor(String color) {
        return Stream.concat(templateDao.findByPrimaryColor(color).stream(), templateDao.findBySecondaryColor(color).stream())
                .collect(Collectors.toList());
    }
    
    public List<Template> findByPrimaryColor(String primaryColor) {
        return templateDao.findByPrimaryColor(primaryColor);
    }
    
    public List<Template> findBySecondaryColor(String secondaryColor) {
        return templateDao.findBySecondaryColor(secondaryColor);
    }
    
    public Template findById(long id) {
        return templateDao.findById(id);
    }
    
    public List<Template> findAllTemplates() {
        return templateDao.findAllTemplates();
    }
    
    public boolean deleteTemplateById(long id) {
        return templateDao.deleteTemplateById(id);
    }
    
    public boolean updateTemplate(Template template) {
        return templateDao.updateTemplate(template);
    }
    
    public boolean isTemplateExist(long id) {
        return templateDao.isTemplateExist(id);
    }
    
    public boolean isTemplateExist(String name) {
        return templateDao.isTemplateExist(name);
    }
    
}
