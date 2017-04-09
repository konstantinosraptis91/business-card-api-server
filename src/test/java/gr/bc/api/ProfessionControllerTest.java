package gr.bc.api;

import gr.bc.api.controller.ProfessionController;
import gr.bc.api.model.Profession;
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
public class ProfessionControllerTest {
    
    @Autowired
    private ProfessionController controller;
    private Profession theProfession;
    
    @Before
    public void setUp() {
        theProfession = new Profession();
        theProfession.setName("This is a profession");
    }
    
    @Test
    public void testProfessionController() {
        saveProfession();
        getProfessionById();
        getProfessionByName();
        updateProfession();
        getAllProfessions();
        deleteProfession();
    }
    
    public void saveProfession() {
        
        ResponseEntity<Void> response = controller.saveProfession(theProfession, UriComponentsBuilder.newInstance());
        
        String[] results = response.getHeaders().getLocation().getPath().split("/");
        long id = Long.parseLong(results[results.length - 1]);
        theProfession.setId(id);
        
        Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
    
    public void updateProfession() {
        
        theProfession.setName("The JUnit test profession");
        
        ResponseEntity<Void> response = controller.updateProfession(theProfession.getId(), theProfession);
        
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    public void getAllProfessions() {
        
        ResponseEntity<List<Profession>> response = controller.find(null);
                
        Assert.assertThat(response.getStatusCode(), 
                Matchers.either(Matchers.is(HttpStatus.OK))
                        .or(Matchers.is(HttpStatus.NO_CONTENT)));
    }
    
    public void getProfessionByName() {
        
        ResponseEntity<List<Profession>> response = controller.find(theProfession.getName());
        
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());        
    }
    
    public void getProfessionById() {
        
        ResponseEntity<Profession> response = controller.findById(theProfession.getId());
        theProfession = response.getBody();
        
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    public void deleteProfession() {
        
        ResponseEntity<Void> response = controller.deleteProfessionById(theProfession.getId());
        
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
}
