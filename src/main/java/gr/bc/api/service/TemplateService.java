/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.bc.api.service;

import gr.bc.api.dao.interfaces.ITemplateDao;
import gr.bc.api.entity.Template;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author konstantinos
 */
@Service
public class TemplateService {
    
    @Autowired
    @Qualifier("MySQLTemplate")
    private ITemplateDao templateDao;

    public Template getTemplateByName(String name) {
        return templateDao.getTemplateByName(name);
    }

    public Template getTemplateById(long id) {
        return templateDao.getTemplateById(id);
    }
    
    public List<Template> getAllTemplates() {
        return templateDao.getAllTemplates();
    }
    
    public Template createTemplate(Template template) {
        return templateDao.createTemplate(template);
    }
   
}
