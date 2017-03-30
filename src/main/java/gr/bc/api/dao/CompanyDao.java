package gr.bc.api.dao;

import gr.bc.api.model.Company;
import java.util.List;
import org.springframework.dao.DataAccessException;

/**
 *
 * @author Konstantinos Raptis
 */
public interface CompanyDao {
    
    Company saveCompany(Company company) throws DataAccessException;
        
    Company findByName(String name) throws DataAccessException;
    
    Company findById(long id) throws DataAccessException;
    
    List<Company> findAllCompanies() throws DataAccessException;
    
    boolean deleteCompanyById(long id) throws DataAccessException;
    
    boolean updateCompany(long id, Company company) throws DataAccessException;
    
}
