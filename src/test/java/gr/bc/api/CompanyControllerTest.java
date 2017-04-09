package gr.bc.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import gr.bc.api.controller.CompanyController;
import gr.bc.api.model.Company;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author konstantinos
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class CompanyControllerTest {
        
    @Autowired
    private CompanyController controller;
    private Company theCompany;
    
    @Before
    public void setUp() {
        theCompany = new Company();
        theCompany.setName("Triaps Inc.");
    }
    
    @Test 
    public void testCompanyController() throws JsonProcessingException {
        saveCompany();
        getCompanyById();
        getCompanyByName();
        updateCompany();
        getAllCompanies();
        deleteCompany();
    }
    
    public void saveCompany() throws JsonProcessingException {
        
        ResponseEntity<Void> response = controller.saveCompany(theCompany, UriComponentsBuilder.newInstance());
        
        String[] results = response.getHeaders().getLocation().getPath().split("/");
        long id = Long.parseLong(results[results.length - 1]);
        theCompany.setId(id);
      
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
    
    public void updateCompany() {
       
        theCompany.setName("The Triaps Company");
        
        ResponseEntity<Void> response = controller.updateCompany(theCompany.getId(), theCompany);
        
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    public void deleteCompany() {
        
        ResponseEntity<Void> response = controller.deleteCompanyById(theCompany.getId());
        
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    public void getCompanyById() throws JsonProcessingException {
        
        ResponseEntity<Company> response = controller.findById(theCompany.getId());
        theCompany = response.getBody();
        
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    public void getAllCompanies() {
                
        ResponseEntity<List<Company>> response = controller.find(null);
                
        Assert.assertThat(response.getStatusCode(), 
                Matchers.either(Matchers.is(HttpStatus.OK))
                        .or(Matchers.is(HttpStatus.NO_CONTENT)));
    }
    
    public void getCompanyByName() {
        
        ResponseEntity<List<Company>> response = controller.find(theCompany.getName());
        
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
}
