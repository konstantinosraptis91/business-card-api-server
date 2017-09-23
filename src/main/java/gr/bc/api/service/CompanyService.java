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

    /**
     * Save a company
     *
     * @param company The company to be saved
     * @return The id of the new saved company
     * @throws ServiceException
     */
    public long saveCompany(Company company) throws ServiceException {

        long id;

        try {
            id = companyDao.saveCompany(company);
        } catch (DataAccessException ex) {
            throw new ConflictException(ex.getMessage());
        }

        return id;
    }

    /**
     * Update a company
     *
     * @param id The id of the company update will affect
     * @param company The company to be updated
     * @return
     * @throws ServiceException
     */
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

    /**
     * Delete a company
     *
     * @param id The id of the company delete will affect
     * @return
     * @throws ServiceException
     */
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

    /**
     * Find a company
     *
     * @param id The company id
     * @return
     * @throws ServiceException
     */
    public Company findById(long id) throws ServiceException {

        Company c;

        try {
            c = companyDao.findById(id);
        } catch (DataAccessException ex) {
            throw new NotFoundException(ex.getMessage());
        }

        return c;
    }

    /**
     * Find a company
     *
     * @param name The company name
     * @return
     * @throws ServiceException
     */
    public Company findByName(String name) throws ServiceException {

        Company c;

        try {
            c = companyDao.findByName(name);
        } catch (DataAccessException ex) {
            throw new NotFoundException(ex.getMessage());
        }

        return c;
    }

    /**
     * Search for a company/companies with a similar name to the given parameter
     *
     * @param name The name of the company
     * @return
     * @throws ServiceException
     */
    public List<Company> searchByName(String name) throws ServiceException {

        List<Company> cList;

        try {
            cList = companyDao.searchByName(name);
        } catch (DataAccessException ex) {
            throw new NotFoundException(ex.getMessage());
        }

        return cList;
    }

    /**
     * This method has to cases
     *
     * (1) For a null name parameter, find all companies
     *
     * (2) For a non null name parameter, find the company for that name
     *
     * @param name The company name
     * @return
     * @throws ServiceException
     */
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

    /**
     * Find all saved companies
     * 
     * @return
     * @throws ServiceException 
     */
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
