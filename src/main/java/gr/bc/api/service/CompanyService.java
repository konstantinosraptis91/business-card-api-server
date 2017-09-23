package gr.bc.api.service;

import gr.bc.api.dao.CompanyDao;
import gr.bc.api.model.Company;
import gr.bc.api.service.exception.ConflictException;
import gr.bc.api.service.exception.NotFoundException;
import gr.bc.api.service.exception.ServiceException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

/**
 *
 * @author Konstantinos Raptis
 */
@Service
public class CompanyService {

    @Autowired
    @Qualifier("MySQLCompany")
    private CompanyDao companyDao;

    public long saveCompany(Company company) throws ServiceException {

        long id;

        try {
            id = companyDao.saveCompany(company);
        } catch (DataAccessException ex) {
            throw new ConflictException(ex.getMessage());
        }

        return id;
    }

    public boolean updateCompany(long id, Company company) throws ServiceException {

        boolean result;

        try {
            result = companyDao.updateCompany(id, company);
        } catch (DataAccessException ex) {
            if (ex instanceof EmptyResultDataAccessException) {
                throw new NotFoundException(ex.getMessage());
            }
            throw new ConflictException(ex.getMessage());
        }

        return result;
    }

    public boolean deleteCompanyById(long id) throws ServiceException {
        
        boolean result;

        try {
            result = companyDao.deleteCompanyById(id);
        } catch (DataAccessException ex) {
            if (ex instanceof EmptyResultDataAccessException) {
                throw new NotFoundException(ex.getMessage());
            }
            throw new ConflictException(ex.getMessage());
        }

        return result;
    }

    public Company findById(long id) throws ServiceException {
        
        Company c;

        try {
            c = companyDao.findById(id);
        } catch (DataAccessException ex) {
            throw new NotFoundException(ex.getMessage());
        }
        
        return c;
    }

    public Company findByName(String name) throws ServiceException {
        
        Company c;

        try {
            c = companyDao.findByName(name);
        } catch (DataAccessException ex) {
            throw new NotFoundException(ex.getMessage());
        }
        
        return c;
    }

    public List<Company> searchByName(String name) throws ServiceException {
        
        List<Company> cList;

        try {
            cList = companyDao.searchByName(name);
        } catch (DataAccessException ex) {
            throw new NotFoundException(ex.getMessage());
        }
        
        return cList;
    }
    
    public List<Company> find(String name) throws ServiceException {
        
        List<Company> cList = new ArrayList<>();

        if (name == null) {
            cList = findAllCompanies();
        } else {
            Company c = findByName(name);
            cList.add(c);
        }

        return cList;
    }
    
    public List<Company> findAllCompanies() throws ServiceException {
        
        List<Company> cList;

        try {
            cList = companyDao.findAllCompanies();
        } catch (DataAccessException ex) {
            throw new NotFoundException(ex.getMessage());
        }
        
        return cList;
    }

}
