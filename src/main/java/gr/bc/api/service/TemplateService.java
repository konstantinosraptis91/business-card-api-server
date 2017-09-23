package gr.bc.api.service;

import gr.bc.api.model.Template;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import gr.bc.api.dao.TemplateDao;
import gr.bc.api.service.exception.ConflictException;
import gr.bc.api.service.exception.NotFoundException;
import gr.bc.api.service.exception.ServiceException;
import java.util.ArrayList;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;

/**
 *
 * @author konstantinos
 */
@Service
public class TemplateService {

    @Autowired
    @Qualifier("MySQLTemplate")
    private TemplateDao templateDao;

    public long saveTemplate(Template t) throws ServiceException {

        long id;

        try {
            id = templateDao.saveTemplate(t);
        } catch (DataAccessException ex) {
            throw new ConflictException(ex.getMessage());
        }

        return id;
    }

    public Template findByName(String name) throws ServiceException {
        
        Template t;

        try {
            t = templateDao.findByName(name);
        } catch (DataAccessException ex) {
            throw new NotFoundException(ex.getMessage());
        }

        return t;
    }

    public List<Template> findByPrimaryColor(String primaryColor) throws ServiceException {
        
        List<Template> tList = new ArrayList<>();

        try {
            tList = templateDao.findByPrimaryColor(primaryColor);
        } catch (DataAccessException ex) {
            throw new NotFoundException(ex.getMessage());
        }

        return tList;
    }

    public List<Template> findBySecondaryColor(String secondaryColor) throws ServiceException {
        
        List<Template> tList = new ArrayList<>();

        try {
            tList = templateDao.findBySecondaryColor(secondaryColor);
        } catch (DataAccessException ex) {
            throw new NotFoundException(ex.getMessage());
        }

        return tList;
    }

    public Template findById(long id) throws ServiceException {

        Template t;

        try {
            t = templateDao.findById(id);
        } catch (DataAccessException ex) {
            throw new NotFoundException(ex.getMessage());
        }

        return t;
    }

    public List<Template> find(String name) throws ServiceException {
        
        List<Template> tList = new ArrayList<>();

        if (name == null) {
            tList = findAllTemplates();
        } else {
            Template t = findByName(name);
            tList.add(t);
        }

        return tList;
    }
    
    public List<Template> findAllTemplates() throws ServiceException {
        
        List<Template> tList;

        try {
            tList = templateDao.findAllTemplates();
        } catch (DataAccessException ex) {
            throw new NotFoundException(ex.getMessage());
        }

        return tList;
    }

    public boolean deleteTemplateById(long id) throws ServiceException {
       
        boolean result;

        try {
            result = templateDao.deleteTemplateById(id);
        } catch (DataAccessException ex) {

            if (ex instanceof EmptyResultDataAccessException) {
                throw new NotFoundException(ex.getMessage());
            }

            throw new ConflictException(ex.getMessage());
        }

        return result;
    }

    public boolean updateTemplate(long id, Template t) throws ServiceException {
        
        boolean result;

        try {
            result = templateDao.updateTemplate(id, t);
        } catch (DataAccessException ex) {
            if (ex instanceof EmptyResultDataAccessException) {
                throw new NotFoundException(ex.getMessage());
            }
            throw new ConflictException(ex.getMessage());
        }

        return result;
    }

}
