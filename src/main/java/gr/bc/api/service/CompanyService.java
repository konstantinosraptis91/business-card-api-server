package gr.bc.api.service;

import gr.bc.api.dao.CompanyDao;
import gr.bc.api.model.Company;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
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
    
    public long saveCompany(Company company) throws DataAccessException {
        return companyDao.saveCompany(company);
    }
    
    public Company findById(long id) throws DataAccessException {
        return companyDao.findById(id);
    }
    
    public Company findByName(String name) throws DataAccessException {
        return companyDao.findByName(name);
    }
    
    public List<Company> findAllCompanies() throws DataAccessException {
        return companyDao.findAllCompanies();
    }
    
    public boolean deleteCompanyById(long id) throws DataAccessException {
        return companyDao.deleteCompanyById(id);
    }
    
    public boolean updateCompany(long id, Company company) throws DataAccessException {
        return companyDao.updateCompany(id, company);
    } 
    
}
